package org.usfirst.frc.team7157.robot;

import org.usfirst.frc.team7157.auto.Controller;

public class OI {
	public Controller driveR = new Controller(0);
	public Controller driveL = new Controller(1);
	public Controller operateStick = new Controller(2);
	
	public double normalize(double toNormalize, double fromHigh, double fromLow, double toHigh, double toLow) {
        double factor = (toHigh - toLow) / (fromHigh - fromLow);
        double add = toLow - fromLow * factor;
        return toNormalize * factor + add;
        }
}
