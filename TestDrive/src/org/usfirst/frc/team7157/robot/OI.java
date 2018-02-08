package org.usfirst.frc.team7157.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class OI {
	public Joystick m_driveStick = new Joystick(0);
	public Joystick m_operateStick = new Joystick(1);
	
	public void AddButton(int joy, int button) {
		if (m_driveStick.getRawButton(button)) {
			
		}
	}
}
