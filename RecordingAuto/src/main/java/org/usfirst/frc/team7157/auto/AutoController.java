package org.usfirst.frc.team7157.auto;

import org.usfirst.frc.team7157.auto.AutoCommandGroups.RightSwitch;
import org.usfirst.frc.team7157.robot.Robot;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Scheduler;
import jaci.pathfinder.Pathfinder;

public class AutoController {
    private RamseteFollower follower;
    private int actionCounter = 0;
    private String[] actionList;
    public void setAuto(String[] autoList) {
        actionList = autoList;
        follower = new RamseteFollower(autoList[actionCounter]);
    }

    @Deprecated
    public void RunAuto() {
        Robot.Drive.SetDriveSpeed(follower.getNextDriveSignal().getLeftSignal(), follower.getNextDriveSignal().getRightSignal());
        if (actionList == AutoActionLists.SwitchRight) SwitchRight();
    }

    public void RunAutoCommand() {
        RightSwitch rs = new RightSwitch();
        rs.start();
    }

    public void RunAutoPeriodic() {
        Scheduler.getInstance().run();
    }
    @Deprecated
    public void SwitchRight() {
        if (follower.isFinished()) {
            follower = new RamseteFollower(actionList[actionCounter]);
            
            //one time actions on end of current path
            switch (actionCounter) {
                case 0:
                Robot.Pneumatics.actuateIntake(Value.kForward);
                Robot.Shooter.SetShooter(-0.45);
                break;

                case 1:
                Robot.Intake.SetIntake(-1);
                break;
            }
            actionCounter++;
        }
        //Repeated actions starting from beginning of current path
        if (actionCounter == 2) {
            Robot.Pneumatics.intakeWheels(false);
        }
    }
}