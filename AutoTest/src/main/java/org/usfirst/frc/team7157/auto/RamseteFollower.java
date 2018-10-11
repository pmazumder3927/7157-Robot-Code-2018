package org.usfirst.frc.team7157.auto;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Segment;

import java.io.File;

import org.usfirst.frc.team7157.robot.Constants;
import org.usfirst.frc.team7157.robot.Robot;

@SuppressWarnings("FieldCanBeLocal")
public class RamseteFollower {

    //Should be greater than zero and this increases correction
    private static final double b = 1.0;

    //Should be between zero and one and this increases dampening
    private static final double zeta = 0.2;

    //Holds what segment we are on
    private int segmentIndex;
    private Segment current;

    //The trajectory to follow
    private Trajectory trajectory;

    //The robot's x and y position and angle
    private RobotPose odometry;


    //Variables used to calculate linear and angular velocity
    private double lastTheta, nextTheta;
    private double k, thetaErr, sinThetaErrorOverThetaError;
    private double desiredAngularVelocity, linearVelocity, angularVelocity;
    private double odometryError;

    private double lastLVOutput; //last known linear velocity output
    private double LVacceleration;

    //Constants
    private static final double doublepi = 2 * Math.PI;

    //Variable for holding velocity for robot to drive on
    private DriveCommand driveCommand;
    private DriveCommand lastCommand;
    private double left, right;

    public RamseteFollower(String trajectoryName){
        trajectory = Pathfinder.readFromFile(new File("/home/lvuser/paths/" + trajectoryName + "_source_Jaci.traj"));
        segmentIndex = 0;
        odometry = Robot.pose;

     driveCommand = new DriveCommand();
     lastCommand = new DriveCommand();
     lastCommand.setLeftVel(0);
     lastCommand.setRightVel(0);
    }

    public DriveCommand getNextDriveSignal(){
        left = 0;
        right = 0;

        current = trajectory.get(segmentIndex);

        desiredAngularVelocity = calculateDesiredAngular();

        linearVelocity = getLinearVel(current.x, current.y, current.heading, current.velocity, desiredAngularVelocity);
        angularVelocity = getAngularVel(current.x, current.y, current.heading, current.velocity, desiredAngularVelocity);

        left = (-(angularVelocity * Constants.wheelBase) + (2 * linearVelocity)) / 2;
        right = ((angularVelocity * Constants.wheelBase) + (2 * linearVelocity)) / 2;

        driveCommand.setLeftVel(left);
        driveCommand.setRightVel(right);

        driveCommand.setLeftAccel((-lastCommand.getLeftSignal() + left)/0.05);
        driveCommand.setRightAccel((-lastCommand.getRightSignal() + right)/0.05);

        segmentIndex++;

        LVacceleration = linearVelocity - lastLVOutput;

        lastLVOutput = linearVelocity;

        return driveCommand;
    }

    private double calculateDesiredAngular(){
        if(segmentIndex < trajectory.length() - 1){
            lastTheta = trajectory.get(segmentIndex).heading;
            nextTheta = trajectory.get(segmentIndex + 1).heading;
            return (nextTheta - lastTheta) / trajectory.get(segmentIndex).dt;
        }else{
            return 0;
        }
    }

    private double getLinearVel(double desiredX, double desiredY, double desiredTheta, double desiredLinearVelocity, double desiredAngularVelocity){
        k = calculateKConstant(desiredLinearVelocity, desiredAngularVelocity);
        thetaErr = boundHalfRadians(desiredTheta - odometry.getTheta());
        odometryError = calculateOdometryError(odometry.getTheta(), desiredX, odometry.getX(), desiredY, odometry.getY());
        return (desiredLinearVelocity * Math.cos(thetaErr)) + (k * odometryError);
    }

    private double getAngularVel(double desiredX, double desiredY, double desiredTheta, double desiredLinearVelocity, double desiredAngularVelocity){
        k = calculateKConstant(desiredLinearVelocity, desiredAngularVelocity);
        thetaErr = boundHalfRadians(desiredTheta - odometry.getTheta());

        if(Math.abs(thetaErr) < Constants.Math.EPSILON){
            sinThetaErrorOverThetaError = 1;
        }else{
            sinThetaErrorOverThetaError = Math.sin(thetaErr)/thetaErr;
        }

        odometryError = calculateOdometryError(odometry.getTheta(), desiredX, odometry.getX(), desiredY, odometry.getY());

        return desiredAngularVelocity + (b * desiredLinearVelocity * sinThetaErrorOverThetaError * odometryError) + (k * thetaErr);
    }

    private double calculateOdometryError(double theta, double desiredX, double x, double desiredY, double y){
        return (Math.cos(theta) * (desiredX - x)) + (Math.sin(theta) * (desiredY - y));
    }

    private double calculateKConstant(double desiredLVelocity, double desiredAVelocity){
        //Calculates k from equation 5.12
        return 2 * zeta * Math.sqrt(Math.pow(desiredAVelocity, 2) + (b * Math.pow(desiredLVelocity, 2)));
    }

    private double boundHalfRadians(double radians){
        while (radians >= Math.PI) radians -= doublepi;
        while (radians < -Math.PI) radians += doublepi;
        return radians;
    }

    public void changeInitialPose(){
        odometry.setX(trajectory.get(0).x);
        odometry.setY(trajectory.get(0).y);
        odometry.setTheta(trajectory.get(0).heading);
    }

    public boolean isFinished(){
        return segmentIndex == trajectory.length();
    }

    public class DriveCommand {
        private double leftVel = 0;
        private double rightVel = 0;
        private double rightAccel = 0;
        private double leftAccel = 0;

    
        public void setLeftVel(double speed) {
            leftVel = speed;
        }
    
        public void setRightVel(double speed) {
            rightVel = speed;
        }

        public void setLeftAccel(double speed) {
            leftAccel = speed;
        }

        public void setRightAccel(double speed) {
            rightAccel = speed;
        }

        public double getLeftSignal() {
            return leftVel;
        }

        public double getRightSignal() {
            return rightVel;
        }
    }
}