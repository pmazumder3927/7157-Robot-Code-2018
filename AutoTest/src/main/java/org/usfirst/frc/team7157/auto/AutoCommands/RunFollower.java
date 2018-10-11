/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team7157.auto.AutoCommands;

import org.usfirst.frc.team7157.auto.RamseteFollower;
import org.usfirst.frc.team7157.auto.RamseteFollower.DriveCommand;
import org.usfirst.frc.team7157.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class RunFollower extends Command {
  String _auto;
  RamseteFollower follower;
  public RunFollower(String auto) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    _auto = auto;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    follower = new RamseteFollower(_auto);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    DriveCommand dc = follower.getNextDriveSignal();
    Robot.Drive.SetDriveSpeed(dc.getLeftSignal(), dc.getRightSignal());
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return follower.isFinished();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.Drive.SetDriveSpeed(0, 0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
