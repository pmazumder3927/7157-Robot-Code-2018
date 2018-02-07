package org.usfirst.frc.team7157.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	/* talons for arcade drive */
	WPI_TalonSRX _frontLeftMotor = new WPI_TalonSRX(10); 		/* device IDs here (1 of 2) */
	WPI_TalonSRX _frontRightMotor = new WPI_TalonSRX(13);
	Compressor c;
	/* extra talons for six motor drives */
	WPI_TalonSRX _leftSlave1 = new WPI_TalonSRX(11);
	WPI_TalonSRX _rightSlave1 = new WPI_TalonSRX(12);
	DifferentialDrive _drive = new DifferentialDrive(_frontLeftMotor, _frontRightMotor);
	Encoder cimCoder, cimCoder2;
	AnalogInput pressureSwitch = new AnalogInput(0);
	
	Joystick _joy = new Joystick(0);
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	/* take our extra talons and just have them follow the Talons updated in arcadeDrive */
    	_leftSlave1.follow(_frontLeftMotor);
    	_rightSlave1.follow(_frontRightMotor);
    	c = new Compressor(0);
    	cimCoder = new Encoder(1, 2);
    	cimCoder2 = new Encoder(5,6);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	SmartDashboard.putNumber("pressure", 250*(pressureSwitch.getAverageVoltage()/5)-25);
    	SmartDashboard.putNumber("CimCoder1", cimCoder.getDistance());
    	SmartDashboard.putNumber("CimCoder2", cimCoder2.getDistance());
    	if (_joy.getTriggerPressed())
    		c.setClosedLoopControl(!c.getClosedLoopControl());
    	
    	c.setClosedLoopControl(_joy.getTriggerPressed() ? true: false);
    	
    	if (_joy.getTriggerPressed()) {
    		c.setClosedLoopControl(true);
    	}
    	else c.setClosedLoopControl(false);
    }
    
    public void autonomousInit() {
    	
    }
    
    public void autonomousPeriodic( ) {
    	
    }
}