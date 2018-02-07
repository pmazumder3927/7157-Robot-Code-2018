package org.usfirst.frc.team7157.robot.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Main {
	TalonSRX shooterLeft;
	TalonSRX shooterSlave;
	
	public void Shoot(boolean Scale) {
		if (Scale) {
			
		}
		else {
			
		}
	}
	
	public void RevWheels(double rpm) {
	shooterLeft.set(ControlMode.Velocity, rpm);
	}
	
	public void ShooterInit() {
		shooterSlave.follow(shooterLeft);
	}
}
