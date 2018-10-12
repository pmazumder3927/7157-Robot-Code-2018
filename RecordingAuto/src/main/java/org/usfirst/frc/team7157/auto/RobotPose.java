package org.usfirst.frc.team7157.auto;

import org.usfirst.frc.team7157.drive.MainDrive;
import org.usfirst.frc.team7157.robot.Robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;

public class RobotPose {
    private volatile double x, y, theta;
    private volatile double currentDistance, lastDistance, deltaDistance, lastTheta, deltaX, deltaY;
    private MainDrive _drive;
    public RobotPose(){
        this.x = 0;
        this.y = 0;
        this.theta = 0;
        _drive = Robot.Drive;

        this.currentDistance = _drive.getAvgEncoder();
        this.lastDistance = _drive.getAvgEncoder();
        this.deltaDistance = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getTheta() {
        return theta % (2 * Math.PI);
    }

    public synchronized void addX(double x){
        this.x += x;
    }

    public synchronized void addY(double y){
        this.y += y;
    }

    public synchronized void setTheta(double theta){
        lastTheta = this.theta;
        this.theta = theta;
    }

    public synchronized void setX(double x){
      this.x = x;
    }

    public synchronized void setY(double y){
      this.y = y;
    }

    public double getCurrentDistance() {
        return currentDistance;
    }

    public void setCurrentDistance(double currentDistance) {
        this.currentDistance = currentDistance;
    }

    public double getLastDistance() {
        return lastDistance;
    }

    public void setLastDistance(double lastDistance) {
        this.lastDistance = lastDistance;
    }

    public double getDeltaDistance() {
        return deltaDistance;
    }

    public void setDeltaDistance(double deltaDistance) {
        this.deltaDistance = deltaDistance;
    }

    public String toString(){
        return "X Position: " + x + " Y Position: " + y + " Heading: " + theta;
    }

    public double getLastTheta() {
        return lastTheta;
    }

    public void calculateDpos() {
        double d = 1/2*(lastTheta - theta);
    }

    public void updatePose() {
        setCurrentDistance(Robot.Drive.getAvgEncoder());
        setDeltaDistance(getCurrentDistance() - getLastDistance());
        
        setTheta(Math.toRadians(Pathfinder.boundHalfDegrees(Robot.Drive.getAngle())));

        SmartDashboard.putNumber("X", getX());
        SmartDashboard.putNumber("Y", getY());
        
        double dTheta = getTheta() - lastTheta;

        deltaX = Math.sin(dTheta) * getDeltaDistance();
        deltaY = Math.cos(dTheta) * getDeltaDistance();
        //rotates x and y around oldangle
        addX(deltaX*Math.cos(getTheta()) - deltaY*Math.sin(getTheta()));
        addY(deltaX*Math.sin(getTheta()) + deltaY*Math.cos(getTheta()));
        
        setLastDistance(getCurrentDistance());
    }
    
}