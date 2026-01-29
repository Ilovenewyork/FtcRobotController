I am adding odometry pods and the horizontal one will use the encoder port of the backleft motor, and the vertical one will use the encoder port of thre backrighgt motor (but leave those as vars to be reconfigured in the roibotcontrols doc)


Show the vector fo displacement from oroginal in the telemtry for teleop. FOr auton, make the paths be like drive to positions first vertical, then horizontal, then turning I guess. Add functions in the robot control which are like "drivex" some distance, "drivey" some distance, etc.