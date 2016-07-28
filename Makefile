mu 		= 1
wmu		= 0.1
amp 		= 0.5
N		= 55
verbose 	= 0
vector		= 0
mframe		= 0
pink		= 0
spinning 	= 0
R		= 10


compile:
	test -d bin || mkdir bin
	javac src/simulation/*.java -d bin

shrink:
	java -classpath bin simulation/ShrinkBoundary ${R}
hist:
	# Histogram Mode
	# Sphere/Sphere Friction: (mu): ${mu}
	# Wall/Sphere Friction: (wmu): ${wmu}
	# Amplitude: (amp): ${amp}
	# Number of Discs (N): ${N}
	# Vector-${vector} M-Frame-${mframe} 
	java -classpath bin simulation/Simulation ${mu} ${wmu} ${amp} ${N} ${spinning} ${R} 0 1
	python hist.py


sim:
	# Simulation Mode
	# Sphere/Sphere Friction: (mu): ${mu}
	# Wall/Sphere Friction: (wmu): ${wmu}
	# Amplitude: (amp): ${amp}
	# Number of Discs (N): ${N}
	# Vector-${vector} M-Frame-${mframe} 
	java -classpath bin simulation/Simulation ${mu} ${wmu} ${amp} ${N} ${spinning} ${R} 0 0

run:
	# Simulation/Animation Mode
	# Sphere/Sphere Friction: (mu): ${mu}
	# Wall/Sphere Friction: (wmu): ${wmu}
	# Amplitude: (amp): ${amp}
	# Number of Discs (N): ${N}
	# Vector-${vector} M-Frame-${mframe} 
	java -classpath bin simulation/Simulation ${mu} ${wmu} ${amp} ${N} ${spinning} ${R} 0 0
	python animation.py ${amp} ${N} ${R} ${spinning} ${pink} ${mframe} ${vector} ${verbose}

test:
	# Testing Mode
	java -classpath bin simulation/Simulation ${mu} ${wmu} ${amp} ${N} ${spinning} ${R} 1 0
