package org.usfirst.frc.team7157.robot.chassis;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class PneumaticsBase {
	public DoubleSolenoid shooterSolenoidLeft;
	public void togglePump(Compressor c) {
		c.setClosedLoopControl(!c.getClosedLoopControl());
	}
	
	public void ToggleAiming() {
		
	}
	
	public void Pneumaticsinit() {
		shooterSolenoidLeft = new DoubleSolenoid(5,6);
	}
	
	public void ToggleShooterAngle(boolean toggle) {
		if (toggle) {
			shooterSolenoidLeft.set(Value.kForward);
		}
		else shooterSolenoidLeft.set(Value.kReverse);
	}
}
