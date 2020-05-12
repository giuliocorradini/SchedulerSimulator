# CPU Scheduler simulator

This project aims to simulate a rela time operating system CPU scheduler.

Using the _strategy_ design pattern it's possible to implement a class with the SchedulerPolicy interface and define
a new behaviour for the low-level scheduler.

Currently these scheduling policies are implemented:

•   First come first served

•   Round robin

The software comes with a graphical user interface, designed for the FlatLaf Darcula Theme (requires the jar artifact).
If you want to implement the functionality in your simulator/program you can use the Scheduler class which implements
only the scheduler logic.