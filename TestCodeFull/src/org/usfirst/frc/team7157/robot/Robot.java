package org.usfirst.frc.team7157.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import org.usfirst.frc.team7157.*;
import org.usfirst.frc.team7157.robot.drivetrain.Drive;
<<<<<<< HEAD
import org.usfirst.frc.team7157.robot.intake.Main;
=======
import org.usfirst.frc.team7157.robot.shooter.Main;
>>>>>>> 5b62f40eddb3c8fcfd882746a4b68a34f0df5939

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	
	public Drive drive = new Drive();
	org.usfirst.frc.team7157.robot.intake.Main Intake = new Main();
	public static OI oi = new OI();
	public org.usfirst.frc.team7157.robot.shooter.Main Shooter = new Main();
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	drive.DriveInit();
<<<<<<< HEAD
    	assignButtons();
=======
    	Shooter.ShooterInit();
>>>>>>> 5b62f40eddb3c8fcfd882746a4b68a34f0df5939
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	drive.DrivePeriodic(oi.m_driveStick);
    }
    
    @Override
    public void autonomousInit() {
    	// TODO Auto-generated method stub
    	super.autonomousInit();
    }
    
    @Override
    public void autonomousPeriodic() {
    	// TODO Auto-generated method stub
    	super.autonomousPeriodic();
    }
    
    public void assignButtons() {
<<<<<<< HEAD
    	if (oi.m_operateStick.getRawButtonPressed(3)) {
    		Intake.SetIntake(true);
    	}
=======
    	if(oi.m_driveStick.getRawButton(0)) {
    		Shooter.RevWheels(543);
    	}
    	if(oi.m_driveStick.getRawButton(1)) {
    		
    	}
    	if(oi.m_driveStick.getRawButton(2)) {
    		
    	}
    	
>>>>>>> 5b62f40eddb3c8fcfd882746a4b68a34f0df5939
    }
}