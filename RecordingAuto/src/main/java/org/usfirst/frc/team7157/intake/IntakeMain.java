package org.usfirst.frc.team7157.intake;

import org.usfirst.frc.team7157.robot.Constants;
import edu.wpi.first.wpilibj.Spark;

public class IntakeMain {
	Spark shooterSpark = new Spark(Constants.kIntake);
	Spark shooterSpark2 = new Spark(Constants.kIntake2);
	
	public void IntakeInit() {

	}
	
	public void SetIntake(double speed) {
		shooterSpark.set(speed);
		shooterSpark2.set(speed);
	}
}