# Simulation in 2D of discs inside a swirling boundary
As the title suggests, the aim of this project is the simulation and animation in two dimensions of discs inside a swirling boundary. A video demo can be viewed [here](https://www.youtube.com/watch?v=2OBc-yOkbhs).

The simulation is written in Java and is found in src/simulation. The animation is written in Python and is found in animation.py and config.py. A detailed description of how it all works can be found HERE. This README will explain how to get it up and running.

Comments, question, and suggests should go to john.ryan@nyu.edu. Thanks for checking out this project!

### Dependencies

Numpy is required for the Python programs.

The animation in this project uses Classic VPython. To install, go to the VPython [website](http://vpython.org/index.html) and click on the appropriate download link in the left-hand sidebar.

### Setting up
Prepare a folder to contain the files of this project, and clones this repository. You can do this by navigating to that directory and running ```git clone https://github.com/jpryan1/rolling-spheres.git .``` or by clicking the "Clone this repository" button on the Github page. Then, navigate to the directory containing the Makefile.

### Running the simulation. 
Compile the java files by running 

`make compile`

then run the simulation and animation by with

`make run`

If you get an error message saying "No module named visual", please see "VPython from the Terminal" below. 

### Easier usage - the menu.

To make changing the parameters easier, the menu.py program allows you to select the configuration that you want for the simulation in a nice GUI. 

![Menu Image](/menu_img.png?raw=true "Menu")

If you received the "No module named visual" error, be sure to check the box for "No animation".

### VPython from the Terminal

The Python files in this project are written for Python 2.7.10. Depending on what version of Python you are using, you may not be able to run the files using VPython from the terminal (see Workaround below). To test this, type 

`python` 

in the terminal (whereupon you should see info about the Python that you are running) and then try

`import visual` 

If you get an error message that the module was not found, then your python interpreter isn't accessing the files installed by the VPython installer. This is a problem that I don't completely know how to solve, and would appreciate any suggestions. Worst case scenario, there is a Workaround (see below).

If you know of another python interpreter (such as /usr/local/bin/python if you downloaded from python.org) that may work, you can change the Makefile to reflect this - change `py=python` to `py=/path/to/different/python`. Another option would be to move the modules to the same folder as the animation.py file. For this reason, I've put the modules at https://github.com/jpryan1/rs-dependencies.

Once you successfully import visual, you know you've got the right version.

### Workaround
If you can't import the visual module with your interpreter of Python, then you will need to use VIDLE to run the animation.
Instead of `make run`, use `make sim`. Once that is completed, open the animation.py file in VIDLE and click Run Module. You can toggle the display options in the animation.py file via changing between 0s and 1s in lines 9-12. 

