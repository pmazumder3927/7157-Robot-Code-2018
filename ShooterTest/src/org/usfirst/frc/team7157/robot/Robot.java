/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team7157.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing the use of the RobotDrive class. The
 * SampleRobot class is the base of a robot application that will automatically
 * call your Autonomous and OperatorControl methods at the right time as
 * controlled by the switches on the driver station or the field controls.
 *
 * <p>The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 *
 * <p>WARNING: While it may look like a good choice to use for your code if
 * you're inexperienced, don't. Unless you know what you are doing, complex code
 * will be much more difficult under this system. Use IterativeRobot or
 * Command-Based instead if you're new.
 */
public class Robot extends SampleRobot {
	public DoubleSolenoid shooterSolenoid;
	public TalonSRX shooterTalon;
	public TalonSRX talonSlave;
	public Spark qSpark;
	public Spark qSpark2;
	public Compressor c;
	public Encoder cimCoder;
	
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private Joystick m_stick = new Joystick(0);
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	boolean manualControl = false;
	public Robot() {
	}

	@Override
	public void robotInit() {
		c = new Compressor(0);
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto modes", m_chooser);
		shooterSolenoid = new DoubleSolenoid(5, 6);
		qSpark = new Spark(0);
		qSpark2 = new Spark(1);
		shooterTalon = new TalonSRX(20);
		talonSlave = new TalonSRX(21);
		talonSlave.follow(shooterTalon);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the if-else structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 *
	 * <p>If you wanted to run a similar autonomous mode with an IterativeRobot
	 * you would write:
	 *
	 * <blockquote><pre>{@code
	 * Timer timer = new Timer();
	 *
	 * // This function is run once each time the robot enters autonomous mode
	 * public void autonomousInit() {
	 *     timer.reset();
	 *     timer.start();
	 * }
	 *
	 * // This function is called periodically during autonomous
	 * public void autonomousPeriodic() {
	 * // Drive for 2 seconds
	 *     if (timer.get() < 2.0) {
	 *         myRobot.drive(-0.5, 0.0); // drive forwards half speed
	 *     } else if (timer.get() < 5.0) {
	 *         myRobot.drive(-1.0, 0.0); // drive forwards full speed
	 *     } else {
	 *         myRobot.drive(0.0, 0.0); // stop robot
	 *     }
	 * }
	 * }</pre></blockquote>
	 */
	@Override
	public void autonomous() {
		String autoSelected = m_chooser.getSelected();
		// String autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);

		// MotorSafety improves safety when motors are updated in loops
		// but is disabled here because motor updates are not looped in
		// this autonomous mode.
		}

	/**
	 * Runs the motors with arcade steering.
	 *
	 * <p>If you wanted to run a similar teleoperated mode with an IterativeRobot
	 * you would write:
	 *
	 * <blockquote><pre>{@code
	 * // This function is called periodically during operator control
	 * public void teleopPeriodic() {
	 *     myRobot.arcadeDrive(stick);
	 * }
	 * }</pre></blockquote>
	 */
	@Override
	public void operatorControl() {
		while (isOperatorControl() && isEnabled()) {
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
	}

	/**
	 * Runs during test mode.
	 */
	@Override
	public void test() {
	}
}
