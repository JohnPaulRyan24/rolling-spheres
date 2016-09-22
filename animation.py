from visual import *
import numpy as np
import math
import sys
import config

DISPLAY_PANCAKE     = 1
stabilize_pancake = 0

speed     = 4000
delta_t   = 3

################################
VERBOSE             = 0
VECTOR              = 0
MFRAME              = 0
DISPLAY_PINK_BALL   = 1
################################

####
# For testing
pancradius = 9.4336748
pancfrequency =  -0.0139027445/(2*np.pi)# (1-(10.0/pancradius))/12.0
#
####

checkerboard = ( ((0,1,1),(0,1,1)), 
                 ((1,0,0),(1,0,0)) )
tex = materials.texture(data=checkerboard,
                     mapping="rectangular",
                     interpolate=False)


def get_input_args():
    if(len(sys.argv)<2):
        arr = open("args.txt","r").readlines()
        arr[7] = VERBOSE
        arr[6] = VECTOR
        arr[5] = MFRAME
        arr[4] = DISPLAY_PINK_BALL
    else:
        arr = sys.argv[1:]
        
    boundvel = float(arr[0])
    num_of_discs = int(arr[1])
    R = float(arr[2])

    spinning = False
    if(int(arr[3])>0):
        spinning = True
    
    display_pink_ball   = False
    if(DISPLAY_PANCAKE==0 and int(arr[4])>0):
        display_pink_ball = True

    mframe = False
    if(int(arr[5])>0):
        mframe = True

    vector = False
    if(int(arr[6])>0):
        vector = True
 
    verbose = False
    if(int(arr[7])>0):
        verbose = True

    return vector, mframe, display_pink_ball, \
           verbose,spinning,num_of_discs, R, boundvel

if __name__ == '__main__':
    input_file = "input/input.txt"
    
    #Set variable from input arguments, either command line or txt file
    vector, mframe, display_pink_ball, verbose,\
            spinning, num_of_discs, boundrad, boundvel = get_input_args()
    
    if(mframe):
        speed  = speed/4

    #Initialize discs
    discs = []
    #Set up config object
    sim_config = config.Sim_Config(num_of_discs, input_file,\
                                   delta_t,boundrad, boundvel, spinning, discs)
    
    #Set up displays   
    scene2 = display(title = "Animation",x=900)
    if(vector):
        scene4 = display(title = "Vector", x=300)
        circle = ring(pos = (0,0,0), axis=(0,0,-1), radius = boundrad, thickness=0.1)
        pointer = arrow(pos=(0,0,1),axis=(5,0,0), shaftwidth=0.1)
        scene2.select()
        

    count = 0
    #If spinning, give special texture
    if(spinning):
        for p in sim_config.positions:
            discs.append(sphere(pos=(p[0],p[1],0), radius=1, material=tex))#, make_trail=True))

    else:
        for p in sim_config.positions:
            #make three different colors
            if(count==0):
                discs.append(sphere(pos=(p[0],p[1],0), radius=1, color=color.blue))
            elif(count==1):
                discs.append(sphere(pos=(p[0],p[1],0), radius=1, color=color.red))
            elif(count==2):
                discs.append(sphere(pos=(p[0],p[1],0), radius=1,  color=color.green))
            count+=1
            count = count%3

    #Initialize boundary elements
    totalTime     = 0
    pink_ball_ang = 0
    pink_ball_pos = (0,10,0)
    if(display_pink_ball):
        pink_ball = sphere(pos=pink_ball_pos, radius=1, color=color.magenta, material=None)
#  white = sphere(pos=(0,0,0), radius=1, color=color.white, material=None)

    boundary = ring(pos = (0,0,0), axis=(0,0,-1), radius = boundrad, thickness=0.1, material=None)

    if(DISPLAY_PANCAKE):
        boundary.color=color.black #Boundary invisible if showing pancake
    
    #Open file with simulation data
    sim_data = open("input/sim_data.txt","r")
    collisions = sim_data.readlines()
    #Open file for histogram data output
    histout = open("vector_data.txt","w")
    t, prev_nonce, mintime = 0, -1, 10

    if(DISPLAY_PANCAKE):            
        panc = ring(pos = (2,0,0), axis=(0,0,-1), radius =9.2, thickness=0.1, color=color.magenta)
        rot = sphere(pos=(0,0,0), radius=1, color=color.magenta, material=None)

      
    for collision in collisions:
        # white.pos=sim_config.center_of_mass()
        
        if(DISPLAY_PANCAKE):
            panc.pos = sim_config.center_of_mass()
            omega = (1/1000.0)*(2*np.pi)*pancfrequency
            if(mframe):
                omega-=(np.pi/6000.0)
            rot.pos = (panc.pos[0]+pancradius*np.cos(omega*totalTime), \
                                              panc.pos[1]+pancradius*np.sin(omega*totalTime),0)
            #endif

        if(vector):
            sim_config.change_arrow(pointer, histout, pink_ball_pos, pink_ball_ang, mframe)

        #Update Pink Ball
        if(not stabilize_pancake):
            pink_ball_ang = (-np.pi/2)+2*np.pi*totalTime*(1/12000.0)
        #stabilizing pancake, just give pink_ball_ang the pancake frequency and use m-frame
        else:
            pink_ball_ang = (-np.pi/2)+2*np.pi*totalTime*(pancfrequency/1000)




        while(pink_ball_ang>2*np.pi):
            pink_ball_ang-=(2*np.pi)
       
        temp1 = np.cos(pink_ball_ang)
        temp2 = np.sin(pink_ball_ang)    
        pink_ball_pos = (sim_config.boundpos[0]+boundrad*temp1,sim_config.boundpos[1]+boundrad*temp2,0)
        if display_pink_ball and not mframe:
            pink_ball.pos = pink_ball_pos
        #Process collision data
        l = collision.split(" ")
        ID = int(l[1])
        if(ID==-1): #swirl event
            nonce = int(l[7])
            time = float(l[2])
            if(nonce!=prev_nonce):
                while(t<time):
                    sim_config.update_positions() #important
                    sim_config.update_animation(mframe, pink_ball_ang, boundary)
                    pink_ball_ang+=(delta_t*np.pi)/6000.0
                    t+=delta_t
                    rate(speed)
               
            sim_config.update_boundary(l)
            prev_nonce=nonce
            t=0
        else:
            posx, posy, velx, vely, theta, theta_vel =\
                  float(l[3]), float(l[5]), float(l[7]), float(l[9]), float(l[11]), float(l[13])
            
            if(len(l)>19):
                bound = False
                ID2, posx2, posy2, velx2, vely2, theta2, theta_vel2, time, nonce = \
                     int(l[15]), float(l[17]), float(l[19]), float(l[21]),\
                     float(l[23]), float(l[25]), float(l[27]), float(l[29]), float(l[30])
                
            else:
                bound = True
                time = float(l[15])
                nonce = int(l[16])
                

            if(verbose and time<mintime):
                mintime=time
                print ("New smallest time between collisions: ",str(mintime))
                
            if(nonce!=prev_nonce):
                while(t<time):
                    sim_config.update_positions()
                    sim_config.update_animation(mframe, pink_ball_ang, boundary)
                    pink_ball_ang+=(delta_t*np.pi)/6000.0
                    t+=delta_t
                    rate(speed)
               
            t=0
            sim_config.positions[ID] = [posx, posy]
            sim_config.velocities[ID] = [velx, vely]
            sim_config.thetas[ID] = theta
            sim_config.theta_vels[ID] = theta_vel
            if(not bound):
                sim_config.positions[ID2] = [posx2, posy2]
                sim_config.velocities[ID2] = [velx2, vely2]
                sim_config.thetas[ID2] = theta2
                sim_config.theta_vels[ID2] = theta_vel2
            prev_nonce = nonce
        totalTime+=time

