import matplotlib.pyplot as plt
import sys

path = "examples/"+sys.argv[1]

f = open(path+"/energies.txt", "r")
lines = f.readlines()
times = []
energies = []
for line in lines:
    l = line.split(" ")
    times.append(float(l[0]))
    energies.append(float(l[1]))
plt.plot(times,energies,'-r')
plt.show()
