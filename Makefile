mu 		= 0.2
wmu		= 0
boundvel 	= 2
N		= 13
verbose 	= 0


compile:
	test -d bin || mkdir bin
	javac src/simulation/*.java -d bin


run:
	java -classpath bin simulation/Simulation ${mu} ${wmu} ${boundvel} ${N} 0
	python plot.py &
	python animation.py ${verbose} ${boundvel} ${N}

test:
	java -classpath bin simulation/Simulation ${mu} ${wmu} ${boundvel} ${N} 1
