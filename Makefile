mu = 0.2
verbose = 0
path = 1
compile:
	javac src/simulation/*.java -d bin
run:
	java -classpath bin simulation/Simulation ${path} ${mu}
	python plot.py ${path} &
	python animation.py ${path} ${mu} ${verbose}
show:
	python plot.py ${path} &
	python animation.py ${path} ${mu} ${verbose}