# Rolling Collisions
All code by John Ryan - john.ryan@nyu.edu
As part of research project with Prof. Miranda Holmes-Cerfon in CIMS - NYU
# What is needed to run the simulation:
If you haven't already, download and install the Java Development Kit (JDK), which allows you to compile and run programs in Java. It can be downloaded and installed here: http://www.oracle.com/technetwork/java/javase/downloads/index.html

# What you need to run the animation:
VPython. It can be installed with pip, or Python Installer Package. If you don't have pip, I highly recommend it. It can be installed via the instructions here: https://pip.pypa.io/en/stable/installing/
In fact, you may already have it, depending on what Python version you have on your machine. 

Once you have pip, VPython is installed with 

```pip install vpython```

It should take seconds.
# Running the animation
Once you have the above packages and have downloaded the repository, navigate in terminal to the directory containing the Makefile. Then execute the following commands:

```make compile```

to compile the Java code and 

```make run```

to perform the simulation and launch the animation.

#Current Status
The boundary can be made to move along a regular n-gonal path with some velocity, and the physics of the collisions have been worked out for this. The current problem: energy is not conserved in this setting, so the velocity of the spheres can and does explode. An upcoming attempt at a workaround will include adding a damping to the spheres, and including this in the computations of collision times.

Here is a video demo: https://www.youtube.com/watch?v=Qc0Vp8sCNq4

Thank you for checking out this project, please reach out to me with comments or questions at john.ryan@nyu.edu
