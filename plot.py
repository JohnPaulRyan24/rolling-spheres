import matplotlib.pyplot as plt
import sys
import time

yaxis ="Density"# "Discs not in pancake"
f = open(sys.argv[1], "r")
lines = f.readlines()
times = []
energies = []
for line in lines:
    l = line.split(" ")
    times.append(float(l[0]))
    energies.append(float(l[1]))

plt.plot(times,energies,'-r')
#plt.xscale('log')
#plt.yscale('log')
plt.title("Default: Mu=1 WMu=0.1 Amp=0.5 N=55")
plt.xlabel("Number of Spheres")
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
