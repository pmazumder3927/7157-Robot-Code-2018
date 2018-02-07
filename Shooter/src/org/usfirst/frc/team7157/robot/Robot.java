/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team7157.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private String m_autoSelected;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	
	public DoubleSolenoid shooterSolenoid;
	public TalonSRX shooterTalon;
	public TalonSRX talonSlave;
	public Spark qSpark;
	public Spark qSpark2;
	public Compressor c;
	
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private Joystick m_stick = new Joystick(0);
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	boolean manualControl = false;
	
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto modes", m_chooser);
		shooterSolenoid = new DoubleSolenoid(5, 6);
		qSpark = new Spark(0);
		qSpark2 = new Spark(1);
		shooterTalon = new TalonSRX(20);
		talonSlave = new TalonSRX(21);
		talonSlave.follow(shooterTalon);
		c = new Compressor(0);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		switch (m_autoSelected) {
			case kCustomAuto:
				// Put custom auto code here
				break;
			case kDefaultAuto:
			default:
				// Put default auto code here
				break;
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		// Drive arcade style
		// The motors will be updated every 5ms
		if (m_stick.getRawButton(8)) {
			manualControl = !manualControl;
		}
		if (manualControl)
		shooterTalon.set(ControlMode.PercentOutput, m_stick.getY());
		else shooterTalon.set(ControlMode.PercentOutput, 1);
		qSpark.set(m_stick.getRawAxis(3));
		qSpark2.set(m_stick.getRawAxis(3));
		if (m_stick.getTriggerPressed()) {
			shooterSolenoid.set(DoubleSolenoid.Value.kForward);
		}
		else shooterSolenoid.set(DoubleSolenoid.Value.kReverse);
		if (m_stick.getRawButtonPressed(7)) {
			c.setClosedLoopControl(!c.getClosedLoopControl());
		}
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
