mu 		= 0.2
wmu		= 0
verbose 	= 0
path 		= 14
boundvel 	= 2
bound_interval = 1
test 		= spherenum
testnum		= ${1000}
N		= 13

compile:
	test -d bin || mkdir bin
	javac src/simulation/*.java -d bin
sim: 
	java -classpath bin simulation/Simulation ${path} ${mu} ${wmu} ${boundvel} ${bound_interval} ${N}
run:
	java -classpath bin simulation/Simulation ${path} ${mu} ${wmu} ${boundvel} ${bound_interval} ${test} ${N}
	#python plot.py ${path} energies &
	python plot.py ${path} cmomenta ${test} ${testnum} &
	python animation.py ${path} ${mu} ${verbose} ${boundvel} ${N}
show:
	#python plot.py ${path} energies &
	python plot.py ${path} momenta ${test} ${testnum} &
	python animation.py ${path} ${mu} ${verbose} ${boundvel}
