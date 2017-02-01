import matplotlib.pyplot as plt
import sys
import time

yaxis = "Percent error"
f = open("data.txt", "r")
#g = open("data1.txt", "r")
#h = open("data2.txt", "r")

lines = f.readlines()
times = []
energies = []
energies1 = []
energies2 = []
for line in lines:
    l = line.split(" ")
    times.append(float(l[0]))
    energies.append(float(l[1]))
#
#lines1 = g.readlines()
#for line in lines1:
#    l = line.split(" ")
#    energies1.append(float(l[1]))
#
#lines2 = h.readlines()
#for line in lines2:
#    l = line.split(" ")
#    energies2.append(float(l[1]))

plt.plot(times[20:],energies[20:],'-r')#, label = "WMu=0")
#plt.plot(times,energies1,'-b', label = "WMu=1")
#plt.plot(times,energies2,'-g',label = "WMu=2" )
#plt.legend()
#plt.xscale('log')
#plt.yscale('log')
plt.title("Error of predicted angular velocity")
plt.xlabel("N")
plt.ylabel(yaxis)
#plt.title(test+": "+testnum)
plt.show()
#
#tempfile = open("cmomenta/"+test+"/temp.txt","r")
#lines = tempfile.read()
#tempfile.close()
#filename = "cmomenta/"+test+"/plots/t"+testnum+".png"
#time.sleep(1)
#plt.savefig(filename)
#results = open("cmomenta/"+test+"/results.txt","a")
#results.write(testnum+": "+lines)
#
#results.close()
plt.show()
