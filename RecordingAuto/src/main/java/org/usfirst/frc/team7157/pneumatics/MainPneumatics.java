package org.usfirst.frc.team7157.pneumatics;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class MainPneumatics {
	private DoubleSolenoid shooterAngle = new DoubleSolenoid(4,5);
	private DoubleSolenoid wheelPistons = new DoubleSolenoid(2, 3);
	private DoubleSolenoid intakeRamp = new DoubleSolenoid(0,1);
	private Compressor c = new Compressor(0);
	
	public boolean angled = false;

	public void sTarTcAncER() {
		shooterAngle.set(Value.kReverse);
		angled = false;
	}
	
	public void sToPcAncER() {
		shooterAngle.set(Value.kForward);
		angled = true;
	}
	
	public void toggleShooterAngle() {
		if (angled) {
			shooterAngle.set(Value.kReverse);
		}
		else shooterAngle.set(Value.kForward);

		angled = !angled;
	}
	
	public void actuateIntake(Value value) {
		intakeRamp.set(value);
	}
	
	public void togglePump() {
		c.setClosedLoopControl(!c.getClosedLoopControl());
	}

	public void intakeWheels(boolean state) {
		wheelPistons.set(!state ? Value.kForward : Value.kReverse);
	}

	public void PneumaticsInit() {
		intakeRamp.set(Value.kReverse);
		shooterAngle.set(Value.kReverse);
		angled = false;
	}
}
