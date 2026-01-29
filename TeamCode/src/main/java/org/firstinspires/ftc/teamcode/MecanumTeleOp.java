package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Mecanum TeleOp", group = "Linear Opmode")
public class MecanumTeleOp extends LinearOpMode {

    // Declare hardware variables
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;
    private DcMotor intakeMotor = null;
    private DcMotor outtakeMotor = null;
    private Servo fingerServo = null;

    @Override
    public void runOpMode() {
        // Initialize hardware
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        outtakeMotor = hardwareMap.get(DcMotor.class, "outtakeMotor");
        fingerServo = hardwareMap.get(Servo.class, "fingerServo");

        // Set motor directions (usually one side needs to be reversed)
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

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

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when at least
            // one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1.0);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);

            // Gamepad 2: Intake/Outtake/Finger
            // Intake motor controlled by left joystick y
            double intakePower = -gamepad2.left_stick_y; // Stick Y is negative upwards
            intakeMotor.setPower(intakePower);

            // Outtake motor controlled by right joystick y
            double outtakePower = -gamepad2.right_stick_y;
            outtakeMotor.setPower(outtakePower);

            // Finger servo controlled by triggers
            if (gamepad2.right_trigger > 0.1) {
                fingerServo.setPosition(1.0); // Open
            } else if (gamepad2.left_trigger > 0.1) {
                fingerServo.setPosition(0.0); // Close
            }

            // Telemetry
            telemetry.addData("Status", "Running");
            telemetry.addData("Drive Motors", "FL:%.2f, FR:%.2f, BL:%.2f, BR:%.2f",
                    frontLeftPower, frontRightPower, backLeftPower, backRightPower);
            telemetry.addData("Motors", "Intake:%.2f, Outtake:%.2f", intakePower, outtakePower);
            telemetry.addData("Servo", "Finger:%.2f", fingerServo.getPosition());
            telemetry.update();
        }
    }
}
