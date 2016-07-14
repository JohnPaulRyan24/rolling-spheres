from visual import *
import numpy as np
import math
import sys
import config

checkerboard = ( ((0,1,1),(0,1,1)), 
                 ((1,0,0),(1,0,0)) )
tex = materials.texture(data=checkerboard,
                     mapping="rectangular",
                     interpolate=False)

def get_input_args():
    pixels              = False
    vector              = False
    mframe              = False
    display_pink_ball   = False
    verbose             = False
    if(int(sys.argv[1])>0):
        verbose = True
    if(int(sys.argv[4])>0):
        pixels = True
    if(int(sys.argv[5])>0):
        vector = True
    if(int(sys.argv[6])>0):
        mframe = True
    if(int(sys.argv[7])>0):
        display_pink_ball = True
    num_of_discs = int(sys.argv[3])
    return pixels, vector, mframe, display_pink_ball, verbose, num_of_discs

if __name__ == '__main__':           
    speed    = 4000
    delta_t  = 2
    boxres   = 10
    boundrad = 10
    pixels, vector, mframe, display_pink_ball, verbose, num_of_discs = get_input_args()
    if(mframe):
        speed  = speed/4
    input_file = "input/input.txt"
    sim_config = config.Sim_Config(num_of_discs, input_file, delta_t)

    #Set up displays   
    scene2 = display(title = "Animation",x=900)
    if(vector):
        scene4 = display(title = "Vector", x=300)
        circle = ring(pos = (0,0,0), axis=(0,0,-1), radius = boundrad, thickness=0.1)
        pointer = arrow(pos=(0,0,1),axis=(5,0,0), shaftwidth=0.1)
        scene2.select()
        
    #Initialize discs    
    count = 0
    discs = []
    for p in sim_config.positions:
        if(count==0):
            discs.append(sphere(pos=(p[0],p[1],0), radius=1,  material=tex))#, make_trail=True))
        elif(count==1):
            discs.append(sphere(pos=(p[0],p[1],0), radius=1,  material=tex))
        elif(count==2):
            discs.append(sphere(pos=(p[0],p[1],0), radius=1,  material=tex))
        count+=1
        count = count%3

    #Initialize boundary elements
    totalTime     = 0
    pink_ball_ang = 0
    pink_ball_pos = (0,10,0)
    pink_ball = sphere(pos=pink_ball_pos, radius=1, color=color.magenta, material=None)
    boundary = ring(pos = (0,0,0), axis=(0,0,-1), radius = boundrad, thickness=0.1, material=None)
    
    #Open file with simulation data
    sim_data = open("input/output.txt","r")
    collisions = sim_data.readlines()
    #Open file for histogram data output
    histout = open("2hist_data.txt","w")
    t, prev_nonce, mintime = 0, -1, 10
       
    for collision in collisions:
        if(vector):
            sim_config.change_arrow(pointer, histout, pink_ball_pos, pink_ball_ang)
        #Update Pink Ball
        pink_ball_ang = (-np.pi/2)+np.pi*totalTime/6000.0
        while(pink_ball_ang>2*np.pi):
            pink_ball_ang-=(2*np.pi)
        if(display_pink_ball):
            temp1 = np.cos(pink_ball_ang)
            temp2 = np.sin(pink_ball_ang)    
            pink_ball_pos = (sim_config.boundpos[0]+10*temp1,sim_config.boundpos[1]+10*temp2,0)
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
                    sim_config.update_animation(mframe, pink_ball_ang, discs, boundary)
                    t+=delta_t
                    rate(speed)
                if(pixels):
                    scene3.select()
                    update_box_colors_density()
                    scene2.select()
            sim_config.update_boundary(l)
            prev_nonce=nonce
            t=0
        else:
            posx, posy, velx, vely = float(l[4]), float(l[6]), float(l[8]), float(l[10])
            if(len(l)>20):
                bound = False
                ID2, posx2, posy2, velx2, vely2, time, nonce = \
                     int(l[17]), float(l[19]), float(l[21]), float(l[23]),\
                     float(l[25]), float(l[31]), float(l[32]) 
            else:
                bound = True
                time = float(l[16])
                nonce = int(l[17])

            if(verbose and time<mintime):
                mintime=time
                print "New smallest time between collisions: ",str(mintime)
                
            if(nonce!=prev_nonce):
                while(t<time):
                    sim_config.update_positions()
                    sim_config.update_animation(mframe, pink_ball_ang, discs, boundary)
                    t+=delta_t
                    rate(speed)
                if(pixels):
                    scene3.select()
                    update_box_colors_density()
                    scene2.select()
            t=0
            sim_config.positions[ID] = [ posx, posy]
            sim_config.velocities[ID] = [ velx, vely]
            if(not bound):
                sim_config.positions[ID2] = [posx2, posy2]
                sim_config.velocities[ID2] = [velx2, vely2]
            prev_nonce = nonce
        totalTime+=time

