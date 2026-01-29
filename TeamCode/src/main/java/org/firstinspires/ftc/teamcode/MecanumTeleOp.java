package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Mecanum TeleOp", group = "Linear Opmode")
public class MecanumTeleOp extends LinearOpMode {

    // Robot controls instance
    private RobotControls robot;

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

            // Outtake motor controlled by right joystick y
            double outtakePower = -gamepad2.right_stick_y;
            robot.setOuttakePower(outtakePower);

            // Finger servo controlled by triggers
            if (gamepad2.right_trigger > 0.1) {
                robot.openFingerServo(); // Open
            } else if (gamepad2.left_trigger > 0.1) {
                robot.closeFingerServo(); // Close
            }
            
            // Right bumper: servo up and down quickly
            if (gamepad2.right_bumper) {
                robot.openFingerServo();
                sleep(200); // Wait 200ms
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
