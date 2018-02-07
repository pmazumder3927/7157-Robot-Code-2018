package org.usfirst.frc.team7157.robot.autonomous;

import edu.wpi.first.wpilibj.DriverStation;

public class Main {
	
	public enum AutoRoutes{
		
	}
	
	private void AutoQueuer() {
		
	}
	
	private void ReadGameData() {
		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		if(gameData.charAt(0) == 'L')
		{
			//Put left auto code here
		} else {
			//Put right auto code here
		}
	}
}
