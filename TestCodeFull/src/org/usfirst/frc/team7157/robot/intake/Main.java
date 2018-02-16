package org.usfirst.frc.team7157.robot.intake;

import edu.wpi.first.wpilibj.Spark;

public class Main {
public float intakeSpeed;
private Spark intakeSpark;
private Spark intakeSpark2;

public void SetIntake(boolean state) {
	if (state) {
	intakeSpark.set(intakeSpeed);	
	}
}

}
