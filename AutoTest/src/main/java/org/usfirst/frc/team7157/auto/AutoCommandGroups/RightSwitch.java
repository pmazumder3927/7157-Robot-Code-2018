/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team7157.auto.AutoCommandGroups;

import org.usfirst.frc.team7157.auto.AutoCommands.*;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightSwitch extends CommandGroup {
  /**
   * Add your docs here.
   */
  public RightSwitch() {
    addSequential(new RunFollower("midRSw"));
    addSequential(new RunShooterTime(3, -0.45));
    addParallel(new ActuateKicker(true));
    addSequential(new RunFollower("midRSwIntk"));
    addParallel(new ActuateKicker(false));
    addParallel(new IntakeWheelSpin(-1, true));
    addSequential(new RunFollower("midRSw2"));    

    // Add Commands here:
    // e.g. addSequential(new Command1());
    // addSequential(new Command2());
    // these will run in order.

    // To run multiple commands at the same time,
    // use addParallel()
    // e.g. addParallel(new Command1());
    // addSequential(new Command2());
    // Command1 and Command2 will run in parallel.

    // A command group will require all of the subsystems that each member
    // would require.
    // e.g. if Command1 requires chassis, and Command2 requires arm,
    // a CommandGroup containing them would require both the chassis and the
    // arm.
    
  }
}
