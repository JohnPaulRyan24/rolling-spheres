from visual import *
import numpy as np
import math
import sys
import config


################################
VERBOSE             = 0
VECTOR              = 0
MFRAME              = 0
DISPLAY_PINK_BALL   = 1
################################

####
# For testing
DISPLAY_PANCAKE     = 0
pancfrequency = -0.02842776953935167/(2*np.pi)
pancradius = 9.2
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
    speed    =4000
    delta_t  = 2
    boxres   = 10
    rotations = 0
    vector, mframe, display_pink_ball, verbose,\
            spinning, num_of_discs, boundrad, boundvel = get_input_args()
    if(mframe):
        speed  = speed/4
    input_file = "input/input.txt"

    discs = []
    sim_config = config.Sim_Config(num_of_discs, input_file,\
                                   delta_t,boundrad, boundvel, spinning, discs)
    
    #Set up displays   
    scene2 = display(title = "Animation",x=900)
    if(vector):
        scene4 = display(title = "Vector", x=300)
        circle = ring(pos = (0,0,0), axis=(0,0,-1), radius = boundrad, thickness=0.1)
        pointer = arrow(pos=(0,0,1),axis=(5,0,0), shaftwidth=0.1)
        scene2.select()
        
    #Initialize discs    
    count = 0
    
    if(spinning):
        for p in sim_config.positions:
            discs.append(sphere(pos=(p[0],p[1],0), radius=1, material=tex))#, make_trail=True))

    else:
        for p in sim_config.positions:
            if(count==0):
                discs.append(sphere(pos=(p[0],p[1],0), radius=1, color=color.blue))#, make_trail=True))
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
    boundary = ring(pos = (0,0,0), axis=(0,0,-1), radius = boundrad, thickness=0.1, material=None)
    if(DISPLAY_PANCAKE):
        boundary.color=color.black
    
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
        if(DISPLAY_PANCAKE):
            panc.pos = sim_config.center_of_mass()
            omega = (1/1000.0)*(2*np.pi)*pancfrequency
            if(mframe):
                omega-=(np.pi/6000.0)
            rot.pos = (panc.pos[0]+pancradius*np.cos(omega*totalTime), \
                                              panc.pos[1]+pancradius*np.sin(omega*totalTime),0)

        if(vector):
            sim_config.change_arrow(pointer, histout, pink_ball_pos, pink_ball_ang, mframe)
        #Update Pink Ball
        pink_ball_ang = (-np.pi/2)+np.pi*totalTime/6000.0
        while(pink_ball_ang>2*np.pi):
            pink_ball_ang-=(2*np.pi)
       
        temp1 = np.cos(pink_ball_ang)
        temp2 = np.sin(pink_ball_ang)    
        pink_ball_pos = (sim_config.boundpos[0]+boundrad*temp1,sim_config.boundpos[1]+boundrad*temp2,0)
        if display_pink_ball and not mframe:
            pink_ball.pos = pink_ball_pos
        #Process collision data
        l = collision.split(" ")
        ID = int(l[2])
        if(ID==-1): #swirl event
            nonce = int(l[8])
            time = float(l[3])
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
                  float(l[4]), float(l[6]), float(l[8]), float(l[10]), float(l[12]), float(l[14])
            if(len(l)>20):
                bound = False
                ID2, posx2, posy2, velx2, vely2, theta2, theta_vel2, time, nonce = \
                     int(l[17]), float(l[19]), float(l[21]), float(l[23]),\
                     float(l[25]), float(l[27]), float(l[29]), float(l[31]), float(l[32]) 
            else:
                bound = True
                time = float(l[16])
                nonce = int(l[17])

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

