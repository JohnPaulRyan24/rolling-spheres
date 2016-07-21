mu 		= 1
wmu		= 0.1
amp 		= 0.5
N		= 55
verbose 	= 0
vector		= 0
mframe		= 0
pink		= 1
spinning 	= 0
R		= 10

compile:
	test -d bin || mkdir bin
	javac src/simulation/*.java -d bin

shrink:
	java -classpath bin simulation/ShrinkBoundary ${R}
sim:
	java -classpath bin simulation/Simulation ${mu} ${wmu} ${amp} ${N} 0 ${spinning} ${R}

run:
	java -classpath bin simulation/Simulation ${mu} ${wmu} ${amp} ${N} 0 ${spinning} ${R}
	# Sphere/Sphere Friction: (mu): ${mu}
	# Wall/Sphere Friction: (wmu): ${wmu}
	# Amplitude: (amp): ${amp}
	# Number of Discs (N): ${N}
	# Pixels-${pixels} Vector-${vector} M-Frame-${mframe} 
	python animation.py ${verbose} ${amp} ${N} ${vector} ${mframe} ${pink} ${spinning} ${R}

test:
	java -classpath bin simulation/Simulation ${mu} ${wmu} ${amp} ${N} 1 ${spinning} ${R}
