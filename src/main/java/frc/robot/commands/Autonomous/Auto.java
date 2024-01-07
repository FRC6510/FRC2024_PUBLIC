package frc.robot.commands.Autonomous;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Drivetrain.MotionProfiling;
import frc.robot.RobotContainer;

// import frc.robot.subsystems.Lift.LiftReal;
// import frc.robot.subsystems.ArmReal;
// import frc.robot.subsystems.Wrist;
// import frc.robot.subsystems.OuttakeReal;
public class Auto {
    RobotContainer robotContainer;

    public Auto(RobotContainer m_robotContainer){
        this.robotContainer = m_robotContainer;
    }

    public static enum AutoList {
        FIGURE_EIGHTS,
        TEST_TURNING,
        BLUE_FIRST_THREE,
        BLUE_FULL;
    }

    public Command getAuto(AutoList auto){

        switch(auto) {
            case FIGURE_EIGHTS:
            return Commands.sequence(
                new PrintCommand("Figure 8s"),
                new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
                    new Pose2d(0, 0, new Rotation2d(0))), 
                    robotContainer.swerveDrive),
                robotContainer.swerveDrive.moveTo(1, 0),
                robotContainer.swerveDrive.moveTo(1, -1),
                robotContainer.swerveDrive.moveTo(2, -1),
                robotContainer.swerveDrive.moveTo(2, 0),
                robotContainer.swerveDrive.moveTo(1, 0),
                robotContainer.swerveDrive.moveTo(1, -1),
                robotContainer.swerveDrive.moveTo(0, -1),
                robotContainer.swerveDrive.moveTo(0, 0));

            case TEST_TURNING:
            return Commands.sequence(
                new PrintCommand("Test turning"),
                new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
                    new Pose2d(0, 0, new Rotation2d(0))), 
                    robotContainer.swerveDrive),
                robotContainer.swerveDrive.moveTo(1, 0),
                robotContainer.swerveDrive.turnTo(Rotation2d.fromDegrees(-90)));

            case BLUE_FIRST_THREE:
            return Commands.sequence(
                new PrintCommand("Blue test"),
                new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
                    new Pose2d(-7.1, 1.5, new Rotation2d(0))), 
                    robotContainer.swerveDrive),
                // front 3  
                robotContainer.swerveDrive.moveAndTurn(-5.9, 0.2, Rotation2d.fromDegrees(-25)),
                robotContainer.swerveDrive.moveAndTurn(-5.55, 0.2, Rotation2d.fromDegrees(-25)),
                robotContainer.swerveDrive.moveAndTurn(-5.9, 0.2, Rotation2d.fromDegrees(-25)),
                
                robotContainer.swerveDrive.moveAndTurn(-5.9, 1.5, Rotation2d.fromDegrees(0)),
                robotContainer.swerveDrive.moveAndTurn(-5.65, 1.5, Rotation2d.fromDegrees(0)),
                robotContainer.swerveDrive.moveAndTurn(-5.9, 1.5, Rotation2d.fromDegrees(0)),

                robotContainer.swerveDrive.moveAndTurn(-5.9, 2.6, Rotation2d.fromDegrees(25)),
                robotContainer.swerveDrive.moveAndTurn(-5.55, 2.6, Rotation2d.fromDegrees(25)),
                robotContainer.swerveDrive.moveAndTurn(-5.9, 2.6, Rotation2d.fromDegrees(25)));

            case BLUE_FULL:
            return Commands.sequence(
                new PrintCommand("Blue test"),
                new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
                    new Pose2d(-7.1, 1.5, new Rotation2d(0))), 
                    robotContainer.swerveDrive),
                // front 3  
                robotContainer.swerveDrive.moveAndTurn(-5.9, 0.2, Rotation2d.fromDegrees(-25)),
                robotContainer.swerveDrive.moveAndTurn(-5.55, 0.2, Rotation2d.fromDegrees(-25)),
                robotContainer.swerveDrive.moveAndTurn(-5.9, 0.2, Rotation2d.fromDegrees(-25)),

                robotContainer.swerveDrive.moveAndTurn(-5.9, 1.5, Rotation2d.fromDegrees(0)),
                robotContainer.swerveDrive.moveAndTurn(-5.65, 1.5, Rotation2d.fromDegrees(0)),
                robotContainer.swerveDrive.moveAndTurn(-5.9, 1.5, Rotation2d.fromDegrees(0)),

                robotContainer.swerveDrive.moveAndTurn(-5.9, 2.6, Rotation2d.fromDegrees(25)),
                robotContainer.swerveDrive.moveAndTurn(-5.55, 2.6, Rotation2d.fromDegrees(25)),
                robotContainer.swerveDrive.moveAndTurn(-5.9, 2.6, Rotation2d.fromDegrees(25)),

                // far left note  
                robotContainer.swerveDrive.moveAndTurn(-0.1, 3.2, Rotation2d.fromDegrees(0)),
                robotContainer.swerveDrive.moveAndTurn(0.2, 3.2, Rotation2d.fromDegrees(0)),
                robotContainer.swerveDrive.moveAndTurn(-2.5, 3, Rotation2d.fromDegrees(15)),
                
                // far middle note  
                robotContainer.swerveDrive.moveAndTurn(-0.1, 1.7, Rotation2d.fromDegrees(0)),
                robotContainer.swerveDrive.moveAndTurn(0.2, 1.7, Rotation2d.fromDegrees(0)),
                robotContainer.swerveDrive.moveAndTurn(-2.5, 2.2, Rotation2d.fromDegrees(10)),
                
                // go to far right
                robotContainer.swerveDrive.moveAndTurn(-0.3, 2, Rotation2d.fromDegrees(0)),
                robotContainer.swerveDrive.moveAndTurn(-0.3, 0, Rotation2d.fromDegrees(0)));

            default:
            return Commands.none();
        }
    }
}
