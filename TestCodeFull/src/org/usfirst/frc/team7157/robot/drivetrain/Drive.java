package org.usfirst.frc.team7157.robot.drivetrain;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drive extends IterativeRobot{
	public WPI_TalonSRX leftFront = new WPI_TalonSRX(0);
	public WPI_TalonSRX leftBack = new WPI_TalonSRX(1);
	public WPI_TalonSRX rightFront = new WPI_TalonSRX(2);
	public WPI_TalonSRX rightBack = new WPI_TalonSRX(3);
	
	private DifferentialDrive _drive;
			
	
	public void DrivePeriodic(Joystick joy) {
		_drive.arcadeDrive(joy.getX(), joy.getY());
	}
	
	public void DriveInit() {
		leftBack.follow(leftFront);
		rightBack.follow(rightFront);
		_drive = new DifferentialDrive(leftFront, rightFront);
	}
}
