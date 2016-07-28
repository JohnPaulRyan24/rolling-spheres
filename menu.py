from Tkinter import *
from ttk import Combobox
from numpy import arange
import subprocess


root = Tk()
root['bg']="gray"
m, s, p, v = IntVar(),IntVar(),IntVar(),IntVar()

def run():
    s1 = " mu="+mu_box.get()
    s2 = " wmu="+wmu_box.get()
    s3 = " amp="+amp_box.get()
    s4 = " N="+N_box.get()
    s5 = " mframe="+str(m.get())
    s6 = " spinning="+str(s.get())
    s7 = " pink="+str(p.get())
    s8 = " vector="+str(v.get())
   

    print "make sim"+s1+s2+s3+s4+s5+s6+s7+s8
    subprocess.call("make sim"+s1+s2+s3+s4+s5+s6+s7+s8, shell=True)




title = Label(root, text="Parameters", font=("Helvetica",16,"bold")).grid(row=0)
mu = Label(root, text="Sphere/Sphere friction").grid(row=1, column=0)
wmu = Label(root, text="Wall/Sphere friction").grid(row=2, column=0)
amp = Label(root, text="Amplitude").grid(row=3, column=0)
N = Label(root, text="Number of Spheres").grid(row=4, column=0)
m, s, p, v = IntVar(),IntVar(),IntVar(),IntVar()
title2 = Label(root, text="Options", font=("Helvetica",16,"bold")).grid(row=0, column=3)
mframe = Checkbutton(root, text="M-Frame", command=None, variable=m)
mframe.grid(row=1,column=3)
spin = Checkbutton(root, text="Spinning Discs", command=None, variable=s)
spin.grid(row=2,column=3)
pink = Checkbutton(root, text="Display M (pink ball)", command=None, variable=p)
pink.grid(row=3,column=3)
vector = Checkbutton(root, text="Display center of mass vector", command=None, variable=v)
vector.grid(row=4,column=3)


mu_box = Combobox(root, state="readonly")
mu_box['values'] = tuple(str(i) for i in arange(0,5,0.1))
mu_box.grid(row=1, column=1)
mu_box.current(10)

wmu_box = Combobox(root, state="readonly")
wmu_box['values'] = tuple(str(i) for i in arange(0,5,0.1))
wmu_box.grid(row=2, column=1)
wmu_box.current(1)

amp_box = Combobox(root, state="readonly")
amp_box['values'] = tuple(str(i) for i in arange(0.25,5,0.25))
amp_box.grid(row=3, column=1)
amp_box.current(1)

N_box = Combobox(root, state="readonly")
N_box['values'] = tuple(str(i) for i in arange(1,60,1))
N_box.grid(row=4, column=1)
N_box.current(54)

button = Button(root,text="Run", command=run).grid(row=5, column=2)

mainloop()


















