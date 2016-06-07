mu 		= 0.3
wall_mu		= 0.1
verbose 	= 0
path 		= 14
boundvel 	= 0.5
bound_interval = 0.2
compile:
	test -d bin || mkdir bin
	javac src/simulation/*.java -d bin
run:
	java -classpath bin simulation/Simulation ${path} ${mu} ${wall_mu} ${boundvel} ${bound_interval}
	python plot.py ${path} energies &
	python plot.py ${path} momenta &
	python animation.py ${path} ${mu} ${verbose}
show:
	python plot.py ${path} energies &
	python plot.py ${path} momenta &
	python animation.py ${path} ${mu} ${verbose}
