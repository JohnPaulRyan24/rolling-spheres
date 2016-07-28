# Simulation in 2D of discs inside a swirling boundary
As the title suggests, the aim of this project is the physical simulation in two dimensions of discs inside a swirling boundary. A video demo can be viewed [here](https://www.youtube.com/watch?v=2OBc-yOkbhs).

The simulation is event-driven. A detailed description of how it works can be found HERE. This README will explain how to get it up and running.

Comments, question, and suggests should go to john.ryan@nyu.edu. Thanks for checking out this project!

### Dependency

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

If you receive some error message like "No module named visual", then the interpreter you're running isn't accessing the modules installed by the VPython installer. You could try to use a different interpreter of Python for this project using an alias. If you know the location of Version 2.7 of Python on your computer, type

`alias python="path/to/version2.7/python"` 

(my path is /usr/local/bin/python). Using a [virtual environment](http://docs.python-guide.org/en/latest/dev/virtualenvs/) may also work. Again, using the Python interpreter in /usr/local/bin/ worked with the VPython installation for me. I am new to the usage of different modules with different interpreters, so if you have insight, feel free to drop me an email. 

Once you successfully import visual, you know you've got the right version.

### Workaround
If you can't import the visual module with your interpreter of Python, then you will need to use VIDLE to run the animation.
Instead of `make run`, use `make sim`. Once that is completed, open the animation.py file in VIDLE and click Run Module. You can toggle the display options in the animation.py file via changing between 0s and 1s in lines 9-12. 

