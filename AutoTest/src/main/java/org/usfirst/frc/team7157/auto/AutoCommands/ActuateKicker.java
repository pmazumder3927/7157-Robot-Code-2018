/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team7157.auto.AutoCommands;

import org.usfirst.frc.team7157.robot.Robot;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.InstantCommand;
/**
 * Add your docs here.
 */
public class ActuateKicker extends InstantCommand {
  /**
   * Add your docs here.
   */

  boolean _state;
  public ActuateKicker(boolean state) {
    super();
    _state = state;
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    Robot.Pneumatics.actuateIntake(_state ? Value.kForward : Value.kReverse);
  }

}
