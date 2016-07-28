# Simulation in 2D of discs inside a swirling boundary
As the title suggests, the aim of this project is the physical simulation in two dimensions of discs inside a swirling boundary. A video demo can be viewed [here](https://www.youtube.com/watch?v=2OBc-yOkbhs).

The simulation is event-driven. A detailed description of how it works can be found HERE. This README will explain how to get it up and running.

### Dependency

The animation in this project uses Classic VPython. To install, go to the VPython [website](http://vpython.org/index.html) and click on the appropriate download link in the left-hand sidebar.

The Python files in this project are written for Python 2.7.10. Depending on what version of Python you are using, you may not be able to run the files using VPython from the terminal (see Workaround below). To test this, type

```python``` 

in the terminal (whereupon you should see info about the Python that you are running) and then try

```import visual``` 

If you receive some error message like "No module named visual", then the version you're running isn't accessing the modules installed by the VPython installer. You could try to use a different version of Python for this project using an alias like ```alias python="path/to/version2.7/python"``` (my path is /usr/local/bin/python, and it seems that /usr/local/bin may be an interesting directory to search for python2.7 - I'm pretty new to this). If you've used virtualenv before, that may also work. Once you successfully import visual, you know you've got the right version.

### Workaround
If you can't import the visual module with your version of Python, then you will need to use VIDLE to run the animation. 


![Menu Image](/menu_img.png?raw=true "Menu")
