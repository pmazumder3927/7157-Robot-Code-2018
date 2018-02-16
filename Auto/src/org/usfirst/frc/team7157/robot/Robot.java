/**
 * This Java FRC robot application is meant to demonstrate an example using the Motion Profile control mode
 * in Talon SRX.  The CANTalon class gives us the ability to buffer up trajectory points and execute them
 * as the roboRIO streams them into the Talon SRX.
 * 
 * There are many valid ways to use this feature and this example does not sufficiently demonstrate every possible
 * method.  Motion Profile streaming can be as complex as the developer needs it to be for advanced applications,
 * or it can be used in a simple fashion for fire-and-forget actions that require precise timing.
 * 
 * This application is an IterativeRobot project to demonstrate a minimal implementation not requiring the command 
 * framework, however these code excerpts could be moved into a command-based project.
 * 
 * The project also includes instrumentation.java which simply has debug printfs, and a MotionProfile.java which is generated
 * in @link https://docs.google.com/spreadsheets/d/1PgT10EeQiR92LNXEOEe3VGn737P7WDP4t0CQxQgC8k0/edit#gid=1813770630&vpid=A1
 * or find Motion Profile Generator.xlsx in the Project folder.
 * 
 * Logitech Gamepad mapping, use left y axis to drive Talon normally.  
 * Press and hold top-left-shoulder-button5 to put Talon into motion profile control mode.
 * This will start sending Motion Profile to Talon while Talon is neutral. 
 * 
 * While holding top-left-shoulder-button5, tap top-right-shoulder-button6.
 * This will signal Talon to fire MP.  When MP is done, Talon will "hold" the last setpoint position
 * and wait for another button6 press to fire again.
 * 
 * Release button5 to allow PercentOutput control with left y axis.
 */

package org.usfirst.frc.team7157.robot;

import com.ctre.phoenix.motorcontrol.can.*;
import com.kauailabs.navx.frc.AHRS;
import com.ctre.phoenix.motion.*;
import com.ctre.phoenix.motorcontrol.*;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	/** The Talon we want to motion profile. */
	AHRS ahrs;
	WPI_TalonSRX talonLeft = new WPI_TalonSRX(Constants.kTalonLeftID);
	WPI_TalonSRX talonLeftSlave = new WPI_TalonSRX(Constants.kTalonLeftSlave);
	WPI_TalonSRX talonRight = new WPI_TalonSRX(Constants.kTalonRightID);
	WPI_TalonSRX talonRightSlave = new WPI_TalonSRX(Constants.kTalonRightSlave);
	
	/** some example logic on how one can manage an MP */
	MotionProfileExample leftProfile = new MotionProfileExample(talonLeft, "rightSwitch_left.csv");
	MotionProfileExample rightProfile = new MotionProfileExample(talonRight, "rightSwitch_right.csv");
	
	DifferentialDrive drive;
	
	/** joystick for testing */
	Joystick _joy = new Joystick(0);

	/**
	 * cache last buttons so we can detect press events. In a command-based
	 * project you can leverage the on-press event but for this simple example,
	 * lets just do quick compares to prev-btn-states
	 */
	boolean[] _btnsLast = {false, false, false, false, false, false, false, false, false, false};

	/** run once after booting/enter-disable */
	public void disabledInit() {
		talonLeftSlave.follow(talonLeft);
		talonRightSlave.follow(talonRight);
		talonDisabledInit(talonLeft);
		talonDisabledInit(talonRight);
	}
	
	public void talonDisabledInit(WPI_TalonSRX _talon) {

		_talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		_talon.setSensorPhase(true); /* keep sensor and motor in phase */
		_talon.configNeutralDeadband(Constants.kNeutralDeadband, Constants.kTimeoutMs);

		_talon.config_kF(0, 0.076, Constants.kTimeoutMs);
		_talon.config_kP(0, 2.000, Constants.kTimeoutMs);
		_talon.config_kI(0, 0.0, Constants.kTimeoutMs);
		_talon.config_kD(0, 20.0, Constants.kTimeoutMs);

		/* Our profile uses 10ms timing */
		_talon.configMotionProfileTrajectoryPeriod(10, Constants.kTimeoutMs); 
		/*
		 * status 10 provides the trajectory target for motion profile AND
		 * motion magic
		 */
		_talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kTimeoutMs);
	}

	/** function is called periodically during operator control */
	public void teleopPeriodic() {
		NavXReport();
		/* get buttons */
		boolean[] btns = new boolean[_btnsLast.length];
		for (int i = 1; i < _btnsLast.length; ++i)
			btns[i] = _joy.getRawButton(i);

		/* get the left joystick axis on Logitech Gampead */
		double leftYjoystick = -1 * _joy.getY(); /* multiple by -1 so joystick forward is positive */

		/*
		 * call this periodically, and catch the output. Only apply it if user
		 * wants to run MP.
		 */
		leftProfile.control();
		rightProfile.control();

		/* Check button 5 (top left shoulder on the logitech gamead). */
		if (btns[5] == false) {
			/*
			 * If it's not being pressed, just do a simple drive. This could be
			 * a RobotDrive class or custom drivetrain logic. The point is we
			 * want the switch in and out of MP Control mode.
			 */

			/* button5 is off so straight drive */
			talonLeft.set(ControlMode.PercentOutput, leftYjoystick);
			talonRight.set(ControlMode.PercentOutput, leftYjoystick);
			leftProfile.reset();
			rightProfile.reset();
		} else {
			/*
			 * Button5 is held down so switch to motion profile control mode =>
			 * This is done in MotionProfileControl. When we transition from
			 * no-press to press, pass a "true" once to MotionProfileControl.
			 */

			SetValueMotionProfile setOutputLeft = leftProfile.getSetValue();
			SetValueMotionProfile setOutputRight = rightProfile.getSetValue();

			talonLeft.set(ControlMode.MotionProfile, setOutputLeft.value);
			talonRight.set(ControlMode.MotionProfile, setOutputRight.value);
			/*
			 * if btn is pressed and was not pressed last time, In other words
			 * we just detected the on-press event. This will signal the robot
			 * to start a MP
			 */
			if ((btns[6] == true) && (_btnsLast[6] == false)) {
				/* user just tapped button 6 */

				// --- We could start an MP if MP isn't already running ----//
				leftProfile.startMotionProfile();
				rightProfile.startMotionProfile();
			}
		}

		/* save buttons states for on-press detection */
		for (int i = 1; i < 10; ++i)
			_btnsLast[i] = btns[i];

	}

	/** function is called periodically during disable */
	public void disabledPeriodic() {
		/*
		 * it's generally a good idea to put motor controllers back into a known
		 * state when robot is disabled. That way when you enable the robot
		 * doesn't just continue doing what it was doing before. BUT if that's
		 * what the application/testing requires than modify this accordingly
		 */
		/* clear our buffer and put everything into a known state */
		leftProfile.reset();
		rightProfile.reset();
	}
	
	public void teleopInit() {
		talonLeftSlave.follow(talonLeft);
		talonRightSlave.follow(talonRight);
		drive = new DifferentialDrive(talonLeft, talonRight);
	}
	
	public void Drive() {
		
	}
	
	public void NavXReport() {
        
        Timer.delay(0.020);		/* wait for one motor update time period (50Hz)     */
        
        boolean zero_yaw_pressed = false; //stick.getTrigger();
        if ( zero_yaw_pressed ) {
            ahrs.zeroYaw();
        }

        /* Display 6-axis Processed Angle Data                                      */
        SmartDashboard.putBoolean(  "IMU_Connected",        ahrs.isConnected());
        SmartDashboard.putBoolean(  "IMU_IsCalibrating",    ahrs.isCalibrating());
        SmartDashboard.putNumber(   "IMU_Yaw",              ahrs.getYaw());
        SmartDashboard.putNumber(   "IMU_Pitch",            ahrs.getPitch());
        SmartDashboard.putNumber(   "IMU_Roll",             ahrs.getRoll());
        
        /* Display tilt-corrected, Magnetometer-based heading (requires             */
        /* magnetometer calibration to be useful)                                   */
        
        SmartDashboard.putNumber(   "IMU_CompassHeading",   ahrs.getCompassHeading());
        
        /* Display 9-axis Heading (requires magnetometer calibration to be useful)  */
        SmartDashboard.putNumber(   "IMU_FusedHeading",     ahrs.getFusedHeading());

        /* These functions are compatible w/the WPI Gyro Class, providing a simple  */
        /* path for upgrading from the Kit-of-Parts gyro to the navx MXP            */
        
        SmartDashboard.putNumber(   "IMU_TotalYaw",         ahrs.getAngle());
        SmartDashboard.putNumber(   "IMU_YawRateDPS",       ahrs.getRate());

        /* Display Processed Acceleration Data (Linear Acceleration, Motion Detect) */
        
        SmartDashboard.putNumber(   "IMU_Accel_X",          ahrs.getWorldLinearAccelX());
        SmartDashboard.putNumber(   "IMU_Accel_Y",          ahrs.getWorldLinearAccelY());
        SmartDashboard.putBoolean(  "IMU_IsMoving",         ahrs.isMoving());
        SmartDashboard.putBoolean(  "IMU_IsRotating",       ahrs.isRotating());

        /* Display estimates of velocity/displacement.  Note that these values are  */
        /* not expected to be accurate enough for estimating robot position on a    */
        /* FIRST FRC Robotics Field, due to accelerometer noise and the compounding */
        /* of these errors due to single (velocity) integration and especially      */
        /* double (displacement) integration.                                       */
        
        SmartDashboard.putNumber(   "Velocity_X",           ahrs.getVelocityX());
        SmartDashboard.putNumber(   "Velocity_Y",           ahrs.getVelocityY());
        SmartDashboard.putNumber(   "Displacement_X",       ahrs.getDisplacementX());
        SmartDashboard.putNumber(   "Displacement_Y",       ahrs.getDisplacementY());
        
        /* Display Raw Gyro/Accelerometer/Magnetometer Values                       */
        /* NOTE:  These values are not normally necessary, but are made available   */
        /* for advanced users.  Before using this data, please consider whether     */
        /* the processed data (see above) will suit your needs.                     */
        
        SmartDashboard.putNumber(   "RawGyro_X",            ahrs.getRawGyroX());
        SmartDashboard.putNumber(   "RawGyro_Y",            ahrs.getRawGyroY());
        SmartDashboard.putNumber(   "RawGyro_Z",            ahrs.getRawGyroZ());
        SmartDashboard.putNumber(   "RawAccel_X",           ahrs.getRawAccelX());
        SmartDashboard.putNumber(   "RawAccel_Y",           ahrs.getRawAccelY());
        SmartDashboard.putNumber(   "RawAccel_Z",           ahrs.getRawAccelZ());
        SmartDashboard.putNumber(   "RawMag_X",             ahrs.getRawMagX());
        SmartDashboard.putNumber(   "RawMag_Y",             ahrs.getRawMagY());
        SmartDashboard.putNumber(   "RawMag_Z",             ahrs.getRawMagZ());
        SmartDashboard.putNumber(   "IMU_Temp_C",           ahrs.getTempC());
        SmartDashboard.putNumber(   "IMU_Timestamp",        ahrs.getLastSensorTimestamp());
        
        /* Omnimount Yaw Axis Information                                           */
        /* For more info, see http://navx-mxp.kauailabs.com/installation/omnimount  */
        AHRS.BoardYawAxis yaw_axis = ahrs.getBoardYawAxis();
        SmartDashboard.putString(   "YawAxisDirection",     yaw_axis.up ? "Up" : "Down" );
        SmartDashboard.putNumber(   "YawAxis",              yaw_axis.board_axis.getValue() );
        
        /* Sensor Board Information                                                 */
        SmartDashboard.putString(   "FirmwareVersion",      ahrs.getFirmwareVersion());
        
        /* Quaternion Data                                                          */
        /* Quaternions are fascinating, and are the most compact representation of  */
        /* orientation data.  All of the Yaw, Pitch and Roll Values can be derived  */
        /* from the Quaternions.  If interested in motion processing, knowledge of  */
        /* Quaternions is highly recommended.                                       */
        SmartDashboard.putNumber(   "QuaternionW",          ahrs.getQuaternionW());
        SmartDashboard.putNumber(   "QuaternionX",          ahrs.getQuaternionX());
        SmartDashboard.putNumber(   "QuaternionY",          ahrs.getQuaternionY());
        SmartDashboard.putNumber(   "QuaternionZ",          ahrs.getQuaternionZ());
        
        /* Connectivity Debugging Support                                           */
        SmartDashboard.putNumber(   "IMU_Byte_Count",       ahrs.getByteCount());
        SmartDashboard.putNumber(   "IMU_Update_Count",     ahrs.getUpdateCount());
	}
}