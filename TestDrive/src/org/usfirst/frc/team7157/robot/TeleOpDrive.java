package org.usfirst.frc.team7157.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class TeleOpDrive {
	public WPI_TalonSRX mainLeft = new WPI_TalonSRX(10);
	public WPI_TalonSRX mainRight = new WPI_TalonSRX(13);
	public TalonSRX bottomLeft = new TalonSRX(11);
	public TalonSRX bottomRight = new TalonSRX(12);
	public DifferentialDrive drive;
	public int percentSpeed;
	public Spark intakeSpark;
	public Spark intakeSpark2;
	public Encoder cimCoder, cimCoder2;
	public AnalogInput pressureSwitch;
	public WPI_TalonSRX shooterLeft = new WPI_TalonSRX(0);
	public WPI_TalonSRX shooterRight = new WPI_TalonSRX(1);
	
	public void TeleDriveInit() {
		cimCoder = new Encoder(1, 2);
		cimCoder2 = new Encoder(3, 4);
		intakeSpark = new Spark(2);
		intakeSpark = new Spark(3);
		pressureSwitch = new AnalogInput(0);
		percentSpeed = 100;
		bottomLeft.follow(mainLeft);
		bottomRight.follow(mainRight);
		drive = new DifferentialDrive(mainLeft, mainRight);
	}
	
	public void Drive(Joystick j) {
		// TODO Auto-generated method stub
		drive.arcadeDrive(j.getX(), j.getY());
	}

	public void Start() {
		mainLeft.set(ControlMode.PercentOutput, percentSpeed);
		mainRight.set(ControlMode.PercentOutput, percentSpeed);
	}
	
	public void Stop() {
		mainLeft.set(ControlMode.PercentOutput, 0);
		mainRight.set(ControlMode.PercentOutput, 0);
	}
	
	public void StartShooter() {
		shooterLeft.set(ControlMode.PercentOutput, percentSpeed);
		shooterRight.set(ControlMode.PercentOutput, percentSpeed);
	}
	
	public void ForceStop() {
		Stop();
		shooterLeft.set(ControlMode.PercentOutput, 0);
		shooterRight.set(ControlMode.PercentOutput, 0);
	}
	


}
