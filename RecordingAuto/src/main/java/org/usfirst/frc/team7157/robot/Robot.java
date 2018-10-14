/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
//odd: RLR evem LRL
package org.usfirst.frc.team7157.robot;

import org.usfirst.frc.team7157.auto.AutoController;
import org.usfirst.frc.team7157.auto.AutoStrings;
import org.usfirst.frc.team7157.auto.RobotPose;
import org.usfirst.frc.team7157.drive.MainDrive;
import org.usfirst.frc.team7157.intake.IntakeMain;
import org.usfirst.frc.team7157.pneumatics.MainPneumatics;
import org.usfirst.frc.team7157.shooter.ShooterMain;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//                            FeEd Me SpAgHeTtI 
// ________                                                         ________
//         \                                                      /
//          \                    ____________                    /
//           \                  /            \                  /
//            \                /  _        _  \                /
//             \              /--|0|------|0|--\              /
//              \             \       ___      /             /
//               \             \     |___|    /             /
//                \             \            /             /
//                 \ ____________ ----------- ____________/
//                               |           |
//                               |           |
//                               |           |
//                               |           |
//                               |           |
//                               -------------
//								/     / \     \
//                             /     /   \     \
//                            /     /     \     \
//                           /     /       \     \
//                          /     /         \     \
//                         /     /           \     \
//                      /--      \           /     --\
//                     /__________\         /_________\
//__________________________________________________________________________

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	//Auto instance
	//Auto Choices
	boolean emergencyLeft = true;
	boolean baseline = false;
	
	double shooterTime = 10;
	double autoWaitTime = 0;
	double autoDriveTime = 1.4;
	double autoDriveTime2 = 1;
	
	boolean shootingFacing = false;

	boolean willShoot = true;
	
    private enum Sides {
        Left,
        Right,
        Middle,
    }
    
    private enum AutoModes {
    	Switch,
    	Scale,
		SwitchScale,
		Baseline
    }
	
	private String m_autoSelected;
	
	//booleans for directions of objectives
	boolean switchFacing = true;
	boolean scaleFacing;
	
    boolean shootSide = false;
    private Timer autoTime = new Timer();
    
    String gameData;
    
    SendableChooser<AutoModes> autoSelector = new SendableChooser<>();
    SendableChooser<Sides> sideSelector = new SendableChooser<>();
	
	
	//instantiating subsystems
	public static final MainDrive Drive = new MainDrive();
	public static final OI OI = new OI();
	public static final MainPneumatics Pneumatics = new MainPneumatics();
	public static final ShooterMain Shooter = new ShooterMain();
	public static final IntakeMain Intake = new IntakeMain();
	public static final RobotPose pose = new RobotPose();
	public static final AutoController AutoController = new AutoController();

	Notifier	autoNotifier = new Notifier(() -> {
		OI.driveL.update();
		OI.driveR.update();
		OI.operateStick.update();
		Input();
	});

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	 //                            FeEd Me SpAgHeTtI 
// ________                                                         ________
//         \                                                      /
//          \                    ____________                    /
//           \                  /            \                  /
//            \                /  _        _  \                /
//             \              /--|0|------|0|--\              /
//              \             \       ___      /             /
//               \             \     |___|    /             /
//                \             \            /             /
//                 \ ____________ ----------- ____________/
//                               |           |
//                               |           |
//                               |           |
//                               |           |
//                               |           |
//                               -------------
//								/     / \     \
//                             /     /   \     \
//                            /     /     \     \
//                           /     /       \     \
//                          /     /         \     \
//                         /     /           \     \
//                      /--      \           /     --\
//                     /__________\         /_________\
//__________________________________________________________________________

	@Override
	public void robotInit() {
		CameraServer.getInstance().startAutomaticCapture(0);
		CameraServer.getInstance().startAutomaticCapture(1);
		
		Pneumatics.PneumaticsInit();
		Drive.DriveInit();
		Shooter.shooterInit();
		Intake.IntakeInit();

		Notifier odometryNotifier = new Notifier(() -> {
			pose.updatePose();
	});

	odometryNotifier.startPeriodic(0.01);
		OI.driveL.setSuffix("_l");
		OI.driveR.setSuffix("_r");
		OI.operateStick.setSuffix("_oi");
		
		SmartDashboard.putString("autoRecording", "value");
		autoSelector.addDefault("Switch", AutoModes.Switch);
		autoSelector.addObject("Scale", AutoModes.Scale);
		autoSelector.addObject("Switch + Scale", AutoModes.SwitchScale);
		autoSelector.addObject("Baseline", AutoModes.Baseline);

		SmartDashboard.putData("Auto Chooser", autoSelector);
		
        sideSelector.addObject("Left", Sides.Left); // Left Side
        sideSelector.addObject("Right", Sides.Right); // Right Side
        sideSelector.addDefault("Middle", Sides.Middle);
        SmartDashboard.putData("Side Selector", sideSelector);
		//TODO
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

	 //                            FeEd Me SpAgHeTtI 
// ________                                                         ________
//         \                                                      /
//          \                    ____________                    /
//           \                  /            \                  /
//            \                /  _        _  \                /
//             \              /--|0|------|0|--\              /
//              \             \       ___      /             /
//               \             \     |___|    /             /
//                \             \            /             /
//                 \ ____________ ----------- ____________/
//                               |           |
//                               |           |
//                               |           |
//                               |           |
//                               |           |
//                               -------------
//								/     / \     \
//                             /     /   \     \
//                            /     /     \     \
//                           /     /       \     \
//                          /     /         \     \
//                         /     /           \     \
//                      /--      \           /     --\
//                     /__________\         /_________\
//__________________________________________________________________________

	@Override
	public void autonomousInit() {
		baseline = autoSelector.getSelected() == AutoModes.Baseline;
		Pneumatics.sTarTcAncER();
		Timer.delay(0.1);
        if (!baseline) {
			String gameDataTemp = DriverStation.getInstance().getGameSpecificMessage();
			if(gameDataTemp != null) {
				gameDataTemp = gameDataTemp.substring(0,2);
			}
			else {
				gameDataTemp = "";
			}
			String currentAuto = getAutoMode(gameDataTemp);
			//gameDataTemp = gameData;

		OI.driveL.setPlay(currentAuto);
		OI.driveR.setPlay(currentAuto);
		OI.operateStick.setPlay(currentAuto);
		autoNotifier.startPeriodic(0.05);
	}
	autoTime.reset();
	autoTime.start();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		if (baseline) baselinePeriodic();
	}
	

	/**
	 * This function is called periodically during operator control.
	 */
	
	@Override
	public void teleopPeriodic() {
		OI.driveL.update();
		OI.driveR.update();
		OI.operateStick.update();
		
		SmartDashboard.putNumber("Left Encoder", Drive.getLeftInches());
		SmartDashboard.putNumber("Right Encoder", Drive.getRightInches());
		SmartDashboard.putNumber("angle", Drive.getAngle());
		

		Input();
		
		if(OI.driveL.getRisingEdge(1)) {
			OI.driveL.setRecord(SmartDashboard.getString("autoRecording", "filler"));
			OI.driveR.setRecord(SmartDashboard.getString("autoRecording", "filler"));
			OI.operateStick.setRecord(SmartDashboard.getString("autoRecording", "filler"));
			System.out.println("recording");
		}
		/*
		 * Call stopRecord to stop recording
		 * This example uses the button B on the xbox
		 */
		
		if(OI.driveR.getRisingEdge(1) && OI.driveL.getRecording()) {
			OI.driveL.stopRecord();
			OI.driveR.stopRecord();
			OI.operateStick.stopRecord();
			System.out.println("Stopped");
		}
	}
//                            FeEd Me SpAgHeTtI 
// ________                                                         ________
//         \                                                      /
//          \                    ____________                    /
//           \                  /            \                  /
//            \                /  _        _  \                /
//             \              /--|0|------|0|--\              /
//              \             \       ___      /             /
//               \             \     |___|    /             /
//                \             \            /             /
//                 \ ____________ ----------- ____________/
//                               |           |
//                               |           |
//                               |           |
//                               |           |
//                               |           |
//                               -------------
//								/     / \     \
//                             /     /   \     \
//                            /     /     \     \
//                           /     /       \     \
//                          /     /         \     \
//                         /     /           \     \
//                      /--      \           /     --\
//                     /__________\         /_________\
//__________________________________________________________________________

	/**
	 * This function is called periodically during test mode.
	 * 		if (autoTime.get() >= autoWaitTime) {
			if (autoTime.get() <= (autoWaitTime + autoDriveTime)) {
				Drive.drive.arcadeDrive(0.8, 0); // Left and Right speeds, 20% power;
			}
			else if (autoTime.get() <= (autoWaitTime + autoDriveTime + autoDriveTime2) && (autoTime.get() >= (autoWaitTime + autoDriveTime))) {
				Drive.drive.arcadeDrive(0, 0);
			}
			else Drive.drive.arcadeDrive(0, 0);
			
			if (autoTime.get() > (autoWaitTime + autoDriveTime + autoDriveTime2) && shootSide && autoTime.get() < (autoWaitTime + autoDriveTime + 2)) {
				Shooter.SetShooter(-0.3);
			}
			else {
				Shooter.SetShooter(0);
			}
	 */
	@Override
	public void testPeriodic() {
	}
	
	public void Input() {
		double yl = OI.driveL.getY();
		double yr = OI.driveR.getY();
		
		Drive.tankDrive(-yl, -yr, true);
		
		double shooterSpeed = -OI.normalize(OI.operateStick.getRawAxis(2), 1, -1, 0, 1);
		double intakeSpeed = OI.operateStick.getRawAxis(4);

		if (OI.operateStick.getRawButton(8) || OI.operateStick.getTrigger()) {
			Pneumatics.intakeWheels(true);
		} else Pneumatics.intakeWheels(false);
		
		if (OI.operateStick.getRawButton(3)) {
			shooterSpeed = -shooterSpeed;
		}
		
		SmartDashboard.putNumber("Shooter Power", shooterSpeed);
    	if (OI.operateStick.getTrigger()) {
			Shooter.SetShooter(shooterSpeed);
    	}
    	else {
    		Shooter.SetShooter(0);
    	}
			Intake.SetIntake(intakeSpeed);
			  	
    	if (OI.operateStick.getRawButtonPressed(4)) {
    		Pneumatics.toggleShooterAngle();
    	}
    	
    	if (OI.operateStick.getRawButtonPressed(10)) {
    		Pneumatics.togglePump();
    	}
    	if (OI.operateStick.getRawButton(2)) {
    		Pneumatics.actuateIntake(Value.kForward);
    	}
    	else {
    		Pneumatics.actuateIntake(Value.kReverse);
    	}
	}
	
	public void disabledInit() {
	}
	
	public void teleopInit() {
		autoNotifier.stop();
		Drive.InvertTalons(false);
		OI.driveL.stopPlay();
		OI.driveR.stopPlay();
		OI.operateStick.stopPlay();
	}
	
	public void baselinePeriodic() {
		willShoot = true;
		if (autoTime.get() >= autoWaitTime) {
			if (autoTime.get() <= (1.7)) {
				Drive.tankDrive(0.7, 0.7, true);; // Left and Right speeds, 20% power;		
			}
			else {
				Drive.tankDrive(0, 0, true);
				if (willShoot)
				Pneumatics.actuateIntake(Value.kForward);
			}
			if (autoTime.get() < 3) {
				if (willShoot)
				Shooter.SetShooter(-0.4);
			}
			else {
				Shooter.SetShooter(0);
			}
			
			if (autoTime.get() > (autoWaitTime + autoDriveTime) && shootSide && autoTime.get() < (autoWaitTime + autoDriveTime + 2)) {
				
			}
			else {
				
			}
		}
	}
	//HELLUR 
//                            FeEd Me SpAgHeTtI 
// ________                                                         ________
//         \                                                      /
//          \                    ____________                    /
//           \                  /            \                  /
//            \                /  _        _  \                /
//             \              /--|0|------|0|--\              /
//              \             \       ___      /             /
//               \             \     |___|    /             /
//                \             \            /             /
//                 \ ____________ ----------- ____________/
//                               |           |
//                               |           |
//                               |           |
//                               |           |
//                               |           |
//                               -------------
//								/     / \     \
//                             /     /   \     \
//                            /     /     \     \
//                           /     /       \     \
//                          /     /         \     \
//                         /     /           \     \
//                      /--      \           /     --\
//                     /__________\         /_________\
//__________________________________________________________________________

	private String getAutoMode(String gameDataTemp) {
		String currentAuto = "";
		switch (gameDataTemp) {
            case "LR": // GOOD!
        	  if (sideSelector.getSelected() == Sides.Left) {
				  shootSide = true;
            	  if (autoSelector.getSelected() == AutoModes.Switch) {
            		  currentAuto = AutoStrings.leftScaleRight;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.Scale) {
            		  currentAuto = AutoStrings.leftScaleRight;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.SwitchScale) {
            		  currentAuto = AutoStrings.leftScaleRight;
            	  }
        	  }
        	  
        	  if (sideSelector.getSelected() == Sides.Middle) {
            	  if (autoSelector.getSelected() == AutoModes.Switch) {
            		  currentAuto = AutoStrings.middleSwitchLeft;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.Scale) {
            		  currentAuto = AutoStrings.middleSwitchLeft;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.SwitchScale) {
            		  currentAuto = AutoStrings.middleSwitchLeft;
            	  }
        	  }
        	  
        	  if (sideSelector.getSelected() == Sides.Right) {
				  shootSide = false;
            	  if (autoSelector.getSelected() == AutoModes.Switch) {
            		  currentAuto = AutoStrings.rightScaleRight;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.Scale) {
            		  currentAuto = AutoStrings.rightScaleRight;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.SwitchScale) {
            		  currentAuto = AutoStrings.rightScaleRight;
            	  }
        	  }
        	  break;
        	  
            case "LL": // GOOD!
        	  if (sideSelector.getSelected() == Sides.Left) {
				  shootSide = true;
            	  if (autoSelector.getSelected() == AutoModes.Switch) {
            		  currentAuto = AutoStrings.leftScaleSwitch;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.Scale) {
            		  currentAuto = AutoStrings.leftScaleLeft;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.SwitchScale) {
            		  currentAuto = AutoStrings.leftScaleSwitch;
            	  }
        	  }
        	  
        	  if (sideSelector.getSelected() == Sides.Middle) {
            	  if (autoSelector.getSelected() == AutoModes.Switch) {
            		  currentAuto = AutoStrings.middleSwitchLeft;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.Scale) {
            		  currentAuto = AutoStrings.middleSwitchLeft;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.SwitchScale) {
            		  currentAuto = AutoStrings.middleSwitchLeft;
            	  }
			  }
			  //HI HOMIEZ
//                            FeEd Me SpAgHeTtI 
// ________                                                         ________
//         \                                                      /
//          \                    ____________                    /
//           \                  /            \                  /
//            \                /  _        _  \                /
//             \              /--|0|------|0|--\              /
//              \             \       ___      /             /
//               \             \     |___|    /             /
//                \             \            /             /
//                 \ ____________ ----------- ____________/
//                               |           |
//                               |           |
//                               |           |
//                               |           |
//                               |           |
//                               -------------
//								/     / \     \
//                             /     /   \     \
//                            /     /     \     \
//                           /     /       \     \
//                          /     /         \     \
//                         /     /           \     \
//                      /--      \           /     --\
//                     /__________\         /_________\
//__________________________________________________________________________
        	  
        	  if (sideSelector.getSelected() == Sides.Right) {
				  shootSide = false;
            	  if (autoSelector.getSelected() == AutoModes.Switch) {
            		  currentAuto = AutoStrings.rightScaleLeft;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.Scale) {
            		  currentAuto = AutoStrings.rightScaleLeft;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.SwitchScale) {
            		  currentAuto = AutoStrings.rightScaleLeft;
            	  }
        	  }
        	  break;
        	  
            case "RL": // GOOD!
        	  if (sideSelector.getSelected() == Sides.Left) {
				  shootSide = false;
            	  if (autoSelector.getSelected() == AutoModes.Switch) {
            		  currentAuto = AutoStrings.leftScaleLeft;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.Scale) {
            		  currentAuto = AutoStrings.leftScaleLeft;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.SwitchScale) {
            		  currentAuto = AutoStrings.leftScaleLeft;
            	  }
        	  }
        	  
        	  if (sideSelector.getSelected() == Sides.Middle) {
            	  if (autoSelector.getSelected() == AutoModes.Switch) {
            		  currentAuto = AutoStrings.middleSwitchRight;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.Scale) {
            		  currentAuto = AutoStrings.middleSwitchRight;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.SwitchScale) {
            		  currentAuto = AutoStrings.middleSwitchRight;
            	  }
        	  }
        	  
        	  if (sideSelector.getSelected() == Sides.Right) {
				  shootSide = true;
            	  if (autoSelector.getSelected() == AutoModes.Switch) {
            		  currentAuto = AutoStrings.rightScaleLeft;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.Scale) {
            		  currentAuto = AutoStrings.rightScaleLeft;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.SwitchScale) {
            		  currentAuto = AutoStrings.rightScaleLeft;
            	  }
        	  }
        	  break;
        	  
            case "RR": // GOOD!
        	  if (sideSelector.getSelected() == Sides.Left) {
				  shootSide = false;
            	  if (autoSelector.getSelected() == AutoModes.Switch) {
            		  currentAuto = AutoStrings.leftScaleRight;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.Scale) {
            		  currentAuto = AutoStrings.leftScaleRight;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.SwitchScale) {
            		  currentAuto = AutoStrings.leftScaleRight;
            	  }
        	  }
        	  
        	  if (sideSelector.getSelected() == Sides.Middle) {
            	  if (autoSelector.getSelected() == AutoModes.Switch) {
            		  currentAuto = AutoStrings.middleSwitchRight;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.Scale) {
            		  currentAuto = AutoStrings.middleSwitchRight;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.SwitchScale) {
            		  currentAuto = AutoStrings.middleSwitchRight;
            	  }
        	  }
        	  
        	  if (sideSelector.getSelected() == Sides.Right) {
				  shootSide = true;
            	  if (autoSelector.getSelected() == AutoModes.Switch) {
            		  currentAuto = AutoStrings.rightScaleRight;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.Scale) {
            		  currentAuto = AutoStrings.rightScaleRight;
            	  }
            	  else if (autoSelector.getSelected() == AutoModes.SwitchScale) {
            		  currentAuto = AutoStrings.rightScaleRight;
            	  }
        	  }
        	  break;
		}
		return currentAuto;
	}
}

//                            FeEd Me SpAgHeTtI 
// ________                                                         ________
//         \                                                      /
//          \                    ____________                    /
//           \                  /            \                  /
//            \                /  _        _  \                /
//             \              /--|0|------|0|--\              /
//              \             \       ___      /             /
//               \             \     |___|    /             /
//                \             \            /             /
//                 \ ____________ ----------- ____________/
//                               |           |
//                               |           |
//                               |           |
//                               |           |
//                               |           |
//                               -------------
//								/     / \     \
//                             /     /   \     \
//                            /     /     \     \
//                           /     /       \     \
//                          /     /         \     \
//                         /     /           \     \
//                      /--      \           /     --\
//                     /__________\         /_________\
//__________________________________________________________________________
