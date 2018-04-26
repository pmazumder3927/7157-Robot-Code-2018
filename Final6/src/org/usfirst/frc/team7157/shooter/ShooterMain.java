package org.usfirst.frc.team7157.shooter;

import org.usfirst.frc.team7157.robot.Constants;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Spark;

public class ShooterMain {
	WPI_TalonSRX shooterTalon = new WPI_TalonSRX(Constants.kTalonShooter);
	WPI_TalonSRX shooterSlave = new WPI_TalonSRX(Constants.kTalonShooterSlave);
	Spark shooterSpark = new Spark(Constants.kSparkShooter);
	Spark shooterSpark2 = new Spark(Constants.kSparkShooter2);
	
	public void SetShooter(double speed) {
		shooterTalon.set(speed);
		shooterSpark.set(speed);
		shooterSpark2.set(speed);
	}
	
	public void shooterInit() {
		shooterTalon.setInverted(true);
		shooterSlave.setInverted(true);
		shooterSlave.follow(shooterTalon);
		shooterSpark.setInverted(true);
		shooterSpark2.setInverted(true);
	}
	
	void Shoot() {
		
	}
}
