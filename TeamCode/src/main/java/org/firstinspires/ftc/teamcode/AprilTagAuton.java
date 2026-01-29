package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@Autonomous(name = "AprilTag Autonomous", group = "Linear Opmode")
public class AprilTagAuton extends LinearOpMode {

    // Robot controls instance
    private RobotControls robot;

    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    @Override
    public void runOpMode() {
        // Initialize robot controls
        robot = new RobotControls();
        robot.initHardware(hardwareMap);

        // Initialize AprilTag Processor
        aprilTag = AprilTagProcessor.easyCreateWithDefaults();

        // Initialize Vision Portal
        visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "camera"), aprilTag);

        telemetry.addData("Status", "Initialized. Looking for AprilTags...");
        telemetry.update();

        int detectedTagID = -1;

        // Detect tags during initialization
        while (!isStarted() && !isStopRequested()) {
            List<AprilTagDetection> currentDetections = aprilTag.getDetections();

            if (currentDetections.size() > 0) {
                for (AprilTagDetection detection : currentDetections) {
                    if (detection.metadata != null) {
                        detectedTagID = detection.id;
                        telemetry.addData("Tag Found", "ID %d (%s)", detection.id, detection.metadata.name);
                    } else {
                        telemetry.addData("Tag Found", "ID %d (Unknown Metadata)", detection.id);
                    }
                }
            } else {
                telemetry.addData("Status", "No Tags Detected Yet");
            }
            telemetry.update();
            sleep(20);
        }

        waitForStart();

        if (opModeIsActive()) {
            // Display final detection result
            telemetry.addData("Status", "Executing for Tag ID: " + detectedTagID);
            telemetry.update();

            // Execute steps based on the detected tag
            // Placeholder cases for 3 options
            switch (detectedTagID) {
                case 1:
                    executeTag1Steps();
                    break;
                case 2:
                    executeTag2Steps();
                    break;
                case 3:
                    executeTag3Steps();
                    break;
                default:
                    telemetry.addData("Error", "Unexpected Tag ID: %d. Running default steps.", detectedTagID);
                    telemetry.update();
                    executeDefaultSteps();
                    break;
            }
        }

        // Close vision portal
        visionPortal.close();
    }

    private void executeTag1Steps() {
        telemetry.addData("Action", "Executing Tag 1 steps: Intensive Intake");
        telemetry.update();

        // Placeholder: Run intake for 2 seconds
        robot.setIntakePower(0.5);
        sleep(2000);
        robot.setIntakePower(0);

        // Final action placeholder
        robot.setOuttakePower(1.0);
        sleep(1000);
        robot.setOuttakePower(0);
    }

    private void executeTag2Steps() {
        telemetry.addData("Action", "Executing Tag 2 steps: Balanced Mode");
        telemetry.update();

        // Placeholder: Drive forward slightly
        robot.setWheelPower(0.3, 0.3, 0.3, 0.3);
        sleep(1000);
        robot.stopDrive();

        // Open finger
        robot.openFingerServo();
        sleep(500);
    }

    private void executeTag3Steps() {
        telemetry.addData("Action", "Executing Tag 3 steps: Rapid Outtake");
        telemetry.update();

        // Placeholder: Outtake immediately
        robot.setOuttakePower(0.8);
        sleep(3000);
        robot.setOuttakePower(0);
    }

    private void executeDefaultSteps() {
        telemetry.addData("Action", "Executing Default steps");
        telemetry.update();
        // Safe default behavior
    }

}
