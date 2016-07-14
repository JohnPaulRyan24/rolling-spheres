mu 		= 3
wmu		= 0
amp 		= 0.5
N		= 55
verbose 	= 0
pixels		= 0
vector		= 1
mframe		= 0
pink		= 1

compile:
	test -d bin || mkdir bin
	javac src/simulation/*.java -d bin


run:
	java -classpath bin simulation/Simulation ${mu} ${wmu} ${amp} ${N} 0
	# Sphere/Sphere Friction: (mu): ${mu}
	# Wall/Sphere Friction: (wmu): ${wmu}
	# Amplitude: (amp): ${amp}
	# Number of Discs (N): ${N}
	# Pixels-${pixels} Vector-${vector} M-Frame-${mframe} 
	python animation.py ${verbose} ${amp} ${N} ${pixels} ${vector} ${mframe} ${pink}

test:
	java -classpath bin simulation/Simulation ${mu} ${wmu} ${amp} ${N} 1
