package org.usfirst.frc.team7157.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	Compressor c;
	/* talons for arcade drive */
	WPI_TalonSRX _frontLeftMotor = new WPI_TalonSRX(10); 		/* device IDs here (1 of 2) */
	WPI_TalonSRX _frontRightMotor = new WPI_TalonSRX(13);
	double speed = 1;
	/* extra talons for six motor drives */
	DoubleSolenoid shooterAngle;
	WPI_TalonSRX _leftSlave1 = new WPI_TalonSRX(11);
	WPI_TalonSRX _rightSlave1 = new WPI_TalonSRX(12);
	WPI_TalonSRX shooterTalon = new WPI_TalonSRX(20);
	WPI_TalonSRX shooterSlave = new WPI_TalonSRX(21);
	Spark shooterSpark;
	Spark shooterSpark2;
	Spark intakeSpark;
	Spark intakeSpark2;
	DifferentialDrive _drive;
	Joystick _joy = new Joystick(0);
	Joystick _drivejoy = new Joystick(1);
     /* This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	/* take our extra talons and just have them follow the Talons updated in arcadeDrive */
    	//_drive = new DifferentialDrive(_frontLeftMotor, _frontRightMotor);
    }
    public void teleopInit() {
    	shooterSpark = new Spark(0);
    	shooterSpark2 = new Spark(3);
    	shooterAngle = new DoubleSolenoid(0, 3);
    	_leftSlave1.follow(_frontLeftMotor);
    	_rightSlave1.follow(_frontRightMotor);
    	intakeSpark = new Spark(2);
    	intakeSpark2 = new Spark(1);
    	shooterSlave.follow(shooterTalon);
    	_drive = new DifferentialDrive(_frontLeftMotor, _frontRightMotor);
    	c = new Compressor(0);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	SmartDashboard.putNumber("intakeSpeed", speed);
    	_drive.arcadeDrive(_joy.getY(), _joy.getZ());
    	speed = _joy.getRawAxis(3);
    	
    	if (_joy.getTrigger()) {
    		shooterTalon.set(ControlMode.PercentOutput, speed);
    		shooterSpark.set(speed);
    		shooterSpark2.set(speed);
    	}
    	else {
    		shooterTalon.set(0);
    		shooterSpark.set(0);
    		shooterSpark2.set(0);
    	}
    	if (_joy.getRawButton(2)) {
    		intakeSpark.set(speed);
    		intakeSpark2.set(speed);
    	}
    	else {
    		intakeSpark.set(0);
    		intakeSpark2.set(0);
    	}
    	if (_joy.getRawButtonPressed(9)) {
    		shooterAngle.set(Value.kForward);
    	}
    	if (_joy.getRawButtonPressed(10)) {
    		shooterAngle.set(Value.kReverse);
    	}
    	
    	if (_joy.getRawButtonPressed(11)) {
    		c.setClosedLoopControl(!c.getClosedLoopControl());
    	}
    		
    }
    
    public void autonomousInit() {
    	
    }
    
    public void autonomousPeriodic( ) {
    }
}