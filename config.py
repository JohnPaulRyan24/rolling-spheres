import sys
import numpy as np

class Sim_Config:

    def __init__(self, num_of_discs, input_file, delta_t, boundrad, boundvel, spinning, discs):
        self.delta_t = delta_t
        self.num_of_discs = num_of_discs
        self.spinning = spinning
        self.positions  = []
        self.velocities = []
        self.thetas = []
        self.theta_vels = []
        inp = open(input_file,"r")
        inpcollisions = inp.readlines()
        for i in range(1,num_of_discs+1):
            collision = inpcollisions[i].split(" ")
            self.positions.append([float(collision[1]),float(collision[2])])
            self.velocities.append([float(collision[4]),float(collision[5])])
            self.thetas.append(0)
            self.theta_vels.append(0)
        count=0
        self.boundpos = [0.0,0.0]
        self.boundvel = [boundvel/1000.0,0.0] #Divide by 1000 due to time scaling
        self.boundrad = boundrad
        self.discs = discs

    def update_positions(self):
   
        for i in range(len(self.positions)):
            self.positions[i][0]+=self.delta_t*self.velocities[i][0]
            self.positions[i][1]+=self.delta_t*self.velocities[i][1]
            self.thetas[i] += self.theta_vels[i]*self.delta_t
        for i in range(2):
            self.boundpos[i]+=self.boundvel[i]*self.delta_t
            
    def update_animation(self, mframe, pink_ball_ang, boundary):
        if not mframe:
            boundary.pos = (self.boundpos[0],self.boundpos[1],0)
            for i in range(len(self.positions)):
                self.discs[i].pos = (self.positions[i][0],self.positions[i][1],0)
                if(self.spinning):
                    self.discs[i].axis = (np.cos(self.thetas[i]),np.sin(self.thetas[i]),0)
                #get position pair in ref frame of M stationary.
        else:
            for i in range(len(self.positions)):
                #First, stabilize the boundary
                newpos = [self.positions[i][0]-self.boundpos[0],self.positions[i][1]-self.boundpos[1]]
                #Then, rotate the system so that the pink ball is in the north pole.
                ang = np.pi/2 - pink_ball_ang
                tmp = newpos[0]*np.cos(ang) - newpos[1]*np.sin(ang)
                newpos[1] = np.sin(ang)*newpos[0]+np.cos(ang)*newpos[1]
                newpos[0] = tmp
                self.discs[i].pos = (newpos[0],newpos[1],0)
                if(self.spinning):
                    self.discs[i].axis = (np.cos(ang-self.thetas[i]+np.pi),np.sin(ang-self.thetas[i]+np.pi),0)


    def update_boundary(self, line):
        self.boundpos[0] = float(line[3])
        self.boundpos[1] = float(line[4])
        self.boundvel[0] = float(line[5])
        self.boundvel[1] = float(line[6])

    def center_of_mass(self):
        toRet = [0.0,0.0]
        for pair in self.discs:
            toRet[0]+=pair.pos[0]
            toRet[1]+=pair.pos[1]
        toRet[0]/=self.num_of_discs
        toRet[1]/=self.num_of_discs
        return (toRet[0],toRet[1],0)

    def change_arrow(self, pointer, histout, pink_ball_pos, pink_ball_ang, mframe):
        centerofmass = [0.0,0.0]
        for pair in self.positions:
            centerofmass[0]+=(pair[0]-self.boundpos[0])
            centerofmass[1]+=(pair[1]-self.boundpos[1])
        centerofmass[0]/=self.num_of_discs
        centerofmass[1]/=self.num_of_discs
        unrotated = [0.0,0.0]
        rotated = [0.0,0.0]
        for i in range(2):
            unrotated[i] = -pink_ball_pos[i]+self.boundpos[i]+centerofmass[i]
        ang=np.pi/2-pink_ball_ang
        rotated[0] = unrotated[0]*np.cos(ang)-unrotated[1]*np.sin(ang)
        rotated[1] = unrotated[0]*np.sin(ang)+unrotated[1]*np.cos(ang)
        pointer.axis = (rotated[0],rotated[1],0)
        pointer.pos=(0,self.boundrad,0)
