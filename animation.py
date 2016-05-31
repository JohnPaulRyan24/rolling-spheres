from visual import *
import numpy as np
import math
import sys

MU = float(sys.argv[2])

scene2 = display(title = "Animation - Mu="+str(MU),x=900)
verb = int(sys.argv[3])
if(verb==1):
    verbose = True
else:
    verbose = False
path = sys.argv[1]
DELTA = 0.005
positions = []
velocities = []

boundpos = [0.0,0.0]
boundvel = [0,0.0]
SPEED = 2000
BOUNDRAD = 5

rin = ring(pos = (0,0,0), axis=(0,0,-1), radius = BOUNDRAD, thickness=0.1)

def update_positions():
    for i in range(len(positions)):
        positions[i][0]+=DELTA*velocities[i][0]
        positions[i][1]+=DELTA*velocities[i][1]
        spheres[i].pos = (positions[i][0],positions[i][1],0)
    for i in range(2):
        boundpos[i]+=boundvel[i]*DELTA
    rin.pos = (boundpos[0],boundpos[1],0)

def anorm(arr):
    return np.sqrt(arr[0]**2+arr[1]**2)
def dist(arr,arr2):
    a = [0,0]
    a[0] = arr[0] - arr2[0]
    a[1] = arr[1] - arr2[1]
    return anorm(a)
    

def update_velocities():
    for i in range(len(velocities)):
        norm = anorm(velocities[i])
        if(norm==0):
            continue
        temp = velocities[i][0]
        velocities[i][0]-=MU*(velocities[i][0]/norm)
        if(temp*velocities[i][0]<=0):
            velocities[i][0]=temp
            if(temp>0):
                velocities[i][0] = 0.01
            else:
                velocities[i][0] = -0.01
        temp = velocities[i][1]
        velocities[i][1]-=MU*(velocities[i][1]/norm)
        if(temp*velocities[i][1]<=0):
            velocities[i][1]=temp
            if(temp>0):
                velocities[i][1] = 0.01
            else:
                velocities[i][1] = -0.01

inp = open("examples/"+path+"/input.txt","r")
inplines = inp.readlines()
num_of_spheres = int(inplines[2])
for i in range(4,len(inplines)):
    line = inplines[i].split(" ")
    positions.append([float(line[1]),float(line[2])])
    velocities.append([float(line[4]),float(line[5])])
spheres = []
flag=True
for p in positions:
    if(flag):
        spheres.append(sphere(pos=(p[0],p[1],0), radius=1, color=color.blue, make_trail=True))
        flag=False
    else:
        spheres.append(sphere(pos=(p[0],p[1],0), radius=1, color=color.red))

out = open("examples/"+path+"/output.txt","r")
lines = out.readlines()
t = 0
prev_nonce=-1
mintime=10
totalTime=0
interval=0
for line in lines:
    l = line.split(" ")
    ID = int(l[2])
    if(ID==-1): #swirl event
        nonce = int(l[8])
        time = float(l[3])
        while(t<time):
            update_positions()
            t+=DELTA
            rate(SPEED)
        boundpos[0] = float(l[4])
        boundpos[1] = float(l[5])
        boundvel[0] = float(l[6])
        boundvel[1] = float(l[7])
        prev_nonce=nonce
        t=0
        
    else:
        posx = float(l[4])
        posy = float(l[6])
        
        velx = float(l[8])
        vely = float(l[10])
        if(len(l)>14):
            bound = False
            ID2 = int(l[13])
            posx2 = float(l[15])
            posy2 = float(l[17])
            velx2 = float(l[19])
            vely2 = float(l[21])
            time = float(l[23])
            nonce = int(l[24])
        else:
            bound = True
            time = float(l[12])
            nonce = int(l[13])
        if(verbose and time<mintime):
            mintime=time
            print "New smallest time between collisions: ",str(mintime)
        if(nonce!=prev_nonce):
            while(t<time):
                update_positions()
                t+=DELTA
                rate(SPEED)
        t=0
        positions[ID] = [ posx, posy]
        velocities[ID] = [ velx, vely]
        if(not bound):
            positions[ID2] = [posx2, posy2]
            velocities[ID2] = [velx2, vely2]
            update_velocities()
        prev_nonce = nonce
        #print str(dist(positions[1],positions[0])), str(velocities[1]),str(velocities[0])
##        print nonce
    totalTime+=time
    if(verbose and totalTime>interval):
        print "Time: ",str(interval)
        interval+=5


