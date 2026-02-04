package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Mecanum TeleOp", group = "Linear Opmode")
public class MecanumTeleOp extends LinearOpMode {

    // Robot controls instance
    private RobotControls robot;
    
    // Finger servo toggle state
    private boolean fingerToggleState = false;
    private boolean lastXButtonState = false;
    
    // Outtake speed sticky states
    private double stickyOuttakePower = 0.0;
    private boolean lastYButtonState = false;
    private boolean lastBButtonState = false;
    private boolean lastAButtonState = false;

    @Override
    public void runOpMode() {
        // Initialize robot controls
        robot = new RobotControls();
        robot.initHardware(hardwareMap);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        // Run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Gamepad 1: Driving
            double y = -gamepad1.left_stick_y; // Invert stick Y
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Use robot controls for mecanum drive
            robot.setDriveDirection(y, x, rx);
            
            // Update odometry from dead wheel pods
            robot.updateOdometry();

            // Gamepad 2: Intake/Outtake/Finger
            // Intake motor controlled by left joystick y
            double intakePower = -gamepad2.left_stick_y; // Stick Y is negative upwards
            robot.setIntakePower(intakePower);

            // Outtake motor controlled by right joystick y OR sticky button states
            double outtakePower = -gamepad2.right_stick_y;
            
            // Check if joystick is being used (override sticky state)
            boolean joystickActive = Math.abs(gamepad2.right_stick_y) > 0.1;
            
            if (!joystickActive) {
                // Use sticky outtake power when joystick is not active
                outtakePower = stickyOuttakePower;
            }
            
            // Button controls for sticky outtake speed (only when joystick is not active)
            if (!joystickActive) {
                // Y button: top speed (1.0)
                if (gamepad2.y && !lastYButtonState) {
                    stickyOuttakePower = 1.0;
                    outtakePower = stickyOuttakePower;
                }
                // B button: mid speed (0.5)
                else if (gamepad2.b && !lastBButtonState) {
                    stickyOuttakePower = 0.5;
                    outtakePower = stickyOuttakePower;
                }
                // A button: off (0.0)
                else if (gamepad2.a && !lastAButtonState) {
                    stickyOuttakePower = 0.0;
                    outtakePower = stickyOuttakePower;
                }
            }
            
            // Update last button states
            lastYButtonState = gamepad2.y;
            lastBButtonState = gamepad2.b;
            lastAButtonState = gamepad2.a;
            
            robot.setOuttakePower(outtakePower);

            // Finger servo controlled by triggers OR X button toggle
            if (gamepad2.right_trigger > 0.1) {
                robot.openFingerServo(); // Open
            } else if (gamepad2.left_trigger > 0.1) {
                robot.closeFingerServo(); // Close
            }
            
            // X button: servo up then down with intake timing
            if (gamepad2.x && !lastXButtonState) {
                robot.openFingerServo(); // Up
                sleep(1500); // Wait 1.5 seconds
                robot.closeFingerServo(); // Down
                sleep(1500); // Wait 1.5 seconds more
                robot.setIntakePower(-1.0); // Intake inwards
                sleep(500); // Run intake for 0.5 seconds
                robot.setIntakePower(0.0); // Stop intake
            }
            lastXButtonState = gamepad2.x;
            
            // Right bumper: servo up and down quickly
            if (gamepad2.right_bumper) {
                robot.openFingerServo();
                sleep(1000); // Wait 200ms
                robot.closeFingerServo();
            }

            // Telemetry
            telemetry.addData("Status", "Running");
            telemetry.addData("Drive Motors", "FL:%.2f, FR:%.2f, BL:%.2f, BR:%.2f",
                    y, x, rx, 0.0);
            telemetry.addData("Motors", "Intake:%.2f, Outtake:%.2f", intakePower, outtakePower);
            telemetry.addData("Servo", "Finger:%.2f", robot.getFingerPosition());
            
            // Odometry telemetry - displacement vector from origin
            double[] position = robot.getPosition();
            double displacementMagnitude = Math.sqrt(position[0] * position[0] + position[1] * position[1]);
            double displacementAngle = Math.toDegrees(Math.atan2(position[1], position[0]));
            
            telemetry.addData("Position", "X: %.2f\" Y: %.2f\"", position[0], position[1]);
            telemetry.addData("Displacement Vector", "Mag: %.2f\" Angle: %.1f°", displacementMagnitude, displacementAngle);
            telemetry.update();
        }
    }
}
