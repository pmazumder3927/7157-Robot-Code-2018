package org.usfirst.frc.team7157.drive;

import org.usfirst.frc.team7157.robot.Constants;
import org.usfirst.frc.team7157.robot.LazyTalonSRX;

import com.ctre.phoenix.motorcontrol.ControlMode;


import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.hal.HAL;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tInstances;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType;

public class MainDrive {
	public DifferentialDrive drive;
	public LazyTalonSRX _leftSlave1 = new LazyTalonSRX(Constants.kTalonLeftSlave);
	public LazyTalonSRX _rightSlave1 = new LazyTalonSRX(Constants.kTalonRightSlave);
	public LazyTalonSRX _frontLeftMotor = new LazyTalonSRX(Constants.kTalonLeft); 		/* device IDs here (1 of 2) */
	public LazyTalonSRX _frontRightMotor = new LazyTalonSRX(Constants.kTalonRight);
	
	public void DriveInit() {
		_leftSlave1.follow(_frontLeftMotor);
		_rightSlave1.follow(_frontRightMotor);
		_leftSlave1.setSafetyEnabled(false);
		_frontLeftMotor.setSafetyEnabled(false);
		_rightSlave1.setSafetyEnabled(false);
		_frontRightMotor.setSafetyEnabled(false);
		/*
		_frontLeftMotor.config_kP(0, 0.005, 10);
		_frontLeftMotor.config_kI(0, 0.00, 10);
		_frontLeftMotor.config_kD(0, 0.00, 10);
		_frontLeftMotor.config_kF(0, 0.4, 10);
		

		_frontRightMotor.config_kP(0, 0.005, 10);
		_frontRightMotor.config_kI(0, 0.00, 10);
		_frontRightMotor.config_kD(0, 0.00, 10);
		_frontRightMotor.config_kF(0, 0.4, 10); */
	}
	
	public void StartDrive() {
		drive = new DifferentialDrive(_frontLeftMotor, _frontRightMotor);
	}
	
	public void InvertTalons(boolean inverted) {
		_rightSlave1.setInverted(inverted);
		_frontRightMotor.setInverted(inverted);
	}
	
	public void arcadeDrive(double xSpeed, double zRotation, boolean squaredInputs) {
		  // while permitting full power.
	    if (squaredInputs) {
	      xSpeed = Math.copySign(xSpeed * xSpeed, xSpeed);
	      zRotation = Math.copySign(zRotation * zRotation, zRotation);
	    }

	    double leftMotorOutput;
	    double rightMotorOutput;

	    double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);

	    if (xSpeed >= 0.0) {
	      // First quadrant, else second quadrant
	      if (zRotation >= 0.0) {
	        leftMotorOutput = maxInput;
	        rightMotorOutput = xSpeed - zRotation;
	      } else {
	        leftMotorOutput = xSpeed + zRotation;
	        rightMotorOutput = maxInput;
	      }
	    } else {
	      // Third quadrant, else fourth quadrant
	      if (zRotation >= 0.0) {
	        leftMotorOutput = xSpeed + zRotation;
	        rightMotorOutput = maxInput;
	      } else {
	        leftMotorOutput = maxInput;
	        rightMotorOutput = xSpeed - zRotation;
	      }
	    }

	    _frontLeftMotor.set(ControlMode.PercentOutput, (leftMotorOutput));
	    _frontRightMotor.set(ControlMode.PercentOutput, -(rightMotorOutput));

	}
	
	public void SetDriveSpeed(double left, double right) {
	    _frontLeftMotor.set(ControlMode.Velocity, (left));
	    _frontRightMotor.set(ControlMode.Velocity, -(right));
	}
	
	  public void tankDrive(double leftSpeed, double rightSpeed, boolean squaredInputs) {
		    leftSpeed = applyDeadband(leftSpeed, 0.02);
		    
		    rightSpeed = applyDeadband(rightSpeed, 0.02);

		    // Square the inputs (while preserving the sign) to increase fine control
		    // while permitting full power.
		    if (squaredInputs) {
		      leftSpeed = Math.copySign(leftSpeed * leftSpeed, leftSpeed);
		      rightSpeed = Math.copySign(rightSpeed * rightSpeed, rightSpeed);
		    }

		    _frontLeftMotor.set(ControlMode.PercentOutput, leftSpeed);
		    _frontRightMotor.set(ControlMode.PercentOutput, -rightSpeed);
		  }
	  
	  protected double applyDeadband(double value, double deadband) {
		    if (Math.abs(value) > deadband) {
		      if (value > 0.0) {
		        return (value - deadband) / (1.0 - deadband);
		      } else {
		        return (value + deadband) / (1.0 - deadband);
		      }
		    } else {
		      return 0.0;
		    }
		  }
	
}
