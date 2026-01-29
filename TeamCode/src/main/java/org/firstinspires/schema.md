Driver Hub Configuration:

4 motors configured in a mecanum drive configuration, names are `frontLeft`, `frontRight`, `backLeft`, `backRight`

one motor named `intakeMotor`

one motor names `outtakeMotor`

one servo named `fingerServo`

one Camera named `camera`


Robot Context:

the robot has to intake balls and throw them. 

One gamepad, gamepad1, should be used for driving the robot.

The left joystick should control the robot direction, amnd the root should be able to move omnidirectionally/holonomically
the right joystick x should control the robot to turn


gamepad 2 will control the ball intake/outtake

The left joystick y should control the intake motor, both direction depending on the sign of the y coord
the right joystick y should control the outtake motor, both direction depending on the sign of the y coord

the right trigger should control the servo to open the finger (1.0)
the left trigger should control the servo to close the finger (0.0)

that is what it will do during the teleop mode

___
for the auton mode it will have to:

- detect what april tag is being seen in the camera upon init, and display it through telemetry

- accordingly, execute a series of steps of intake/outtake

for now, add placeholders for which steps to execute based on which april tag is seen, there are only 3 options for april tag
