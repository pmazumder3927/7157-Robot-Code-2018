package org.usfirst.frc.team7157.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.kauailabs.navx.frc.AHRS;

import org.usfirst.frc.team7157.auto.RobotPose;
import org.usfirst.frc.team7157.robot.Constants;
import org.usfirst.frc.team7157.robot.LazyTalonSRX;
import org.usfirst.frc.team7157.robot.Robot;

import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import jaci.pathfinder.Pathfinder;

public class MainDrive {
	public DriveChar leftChar;
	public DriveChar rightChar;

	public DifferentialDrive drive;
	public LazyTalonSRX _leftSlave1 = new LazyTalonSRX(Constants.kTalonLeftSlave);
	public LazyTalonSRX _rightSlave1 = new LazyTalonSRX(Constants.kTalonRightSlave);
	public LazyTalonSRX _frontLeftMotor = new LazyTalonSRX(Constants.kTalonLeft); 		/* device IDs here (1 of 2) */
	public LazyTalonSRX _frontRightMotor = new LazyTalonSRX(Constants.kTalonRight);

	private RobotPose odometry = Robot.pose;
	private AHRS ahrs = new AHRS(Port.kMXP);

	public MainDrive() {

	}

	public void DriveInit() {
		ahrs.reset();

		_leftSlave1.follow(_frontLeftMotor);
		_rightSlave1.follow(_frontRightMotor);
		_leftSlave1.setSafetyEnabled(false);
		_frontLeftMotor.setSafetyEnabled(false);
		_rightSlave1.setSafetyEnabled(false);
		_frontRightMotor.setSafetyEnabled(false);
	
		_frontLeftMotor.config_kP(0, Constants.kP, 10);
		_frontLeftMotor.config_kI(0, Constants.kI, 10);
		_frontLeftMotor.config_kD(0, Constants.kD, 10);
		_frontLeftMotor.config_kF(0, Constants.kF, 10);
		

		_frontRightMotor.config_kP(0, Constants.kP, 10);
		_frontRightMotor.config_kI(0, Constants.kI, 10);
		_frontRightMotor.config_kD(0, Constants.kD, 10);
		_frontRightMotor.config_kF(0, Constants.kF, 10);

		_frontLeftMotor.setSensorPhase(false);
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

	public int getLeftEncoder() {
		return -_frontLeftMotor.getSelectedSensorPosition(0);
	}

	public float getRightEncoder() {
		return _frontRightMotor.getSelectedSensorPosition(0);
	}

	public float getAvgEncoder() {
		return (_frontRightMotor.getSelectedSensorPosition(0) + _frontLeftMotor.getSelectedSensorPosition(0)) /2;
	}

	public double getLeftInches() {
		return getLeftEncoder() * Math.PI * 6 / Constants.kSensorUnits;
	}

	public double getRightInches() {
		return getRightEncoder() * Math.PI * 6 / Constants.kSensorUnits;
	}

	public double getAvgVelocity() {
		return (_frontLeftMotor.getSelectedSensorVelocity(0) + _frontRightMotor.getSelectedSensorVelocity(0)) / 2;
	}

	public double getLeftVel() {
		return _frontLeftMotor.getSelectedSensorVelocity(0);
	}

	public double getRightVel() {
		return _frontRightMotor.getSelectedSensorVelocity(0);
	}

	public void resetEncoders() {
		_frontRightMotor.setSelectedSensorPosition(0, 0, 0);
		_frontLeftMotor.setSelectedSensorPosition(0, 0, 0);
	}
	
	public void SetDriveSpeed(double left, double right) {
	    _frontLeftMotor.set(ControlMode.Velocity, (left));
	    _frontRightMotor.set(ControlMode.Velocity, -(right));
	}

	public void SetDriveVoltage(double left, double right) {
		_frontLeftMotor.set(ControlMode.PercentOutput, (left/12));
		_frontRightMotor.set(ControlMode.Velocity, -(right/12));
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
			
	public void tankDriveSpeed(double leftSpeed, double rightSpeed, boolean squaredInputs) {
				SetDriveSpeed(leftSpeed, rightSpeed);
	}

	public double getAngle() {
		return ahrs.getAngle();
	}

	public void setLeft(double speed) {
		_frontLeftMotor.set(ControlMode.Velocity, (speed));
	}

	public void setRight(double speed) {
		_frontRightMotor.set(ControlMode.Velocity, -(speed));
	}

	public void setVapp(double leftVel, double rightVel, double leftAcc, double rightAcc) {
		//double left = 
		//SetDriveVoltage(left, right);
	}
}
