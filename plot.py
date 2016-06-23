import matplotlib.pyplot as plt
import sys
import time



f = open("examples/14/momenta.txt", "r")
lines = f.readlines()
times = []
energies = []
for line in lines:
    l = line.split(" ")
    times.append(float(l[0]))
    energies.append(float(l[1]))
plt.plot(times,energies,'-r')
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
