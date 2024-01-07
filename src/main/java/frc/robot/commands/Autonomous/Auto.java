package frc.robot.commands.Autonomous;

import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Drivetrain.MotionProfiling;
import frc.robot.subsystems.Shooter.SpinFeedShooter;
import frc.robot.RobotContainer;

public class Auto {
    RobotContainer robotContainer;
    public Auto(RobotContainer m_robotContainer){
        this.robotContainer = m_robotContainer;
    }

    public static enum AutoList {
        CYCLES_TEST,
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

            case CYCLES_TEST:
            return Commands.sequence(
                new PrintCommand("Cycles test"),
                new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
                    new Pose2d(0,0, new Rotation2d(0))), 
                    robotContainer.swerveDrive),
                // front 3  
                // robotContainer.swerveDrive.moveAndTurn(1, 0, Rotation2d.fromDegrees(0)),
                // SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 1, () -> 0.4, () -> -40),
                new ParallelCommandGroup(
                    robotContainer.intake.spinIntake(() -> 35), 
                    robotContainer.feeder.spinFeeder(() -> -45)),
                // robotContainer.swerveDrive.moveAndTurn(1.5, 0, Rotation2d.fromDegrees(0)),
                // robotContainer.swerveDrive.moveAndTurn(1, 0, Rotation2d.fromDegrees(0)),
                SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 1, () -> 0.4, () -> -40));

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
                new InstantCommand(() -> robotContainer.shooterPivot.setIdle(NeutralModeValue.Brake)),
                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(-5.9, 0.2, Rotation2d.fromDegrees(-25)),
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 5),
                    SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 1, () -> 0.4, () -> -40)),                
                robotContainer.swerveDrive.moveAndTurn(-5.69, 0.2, Rotation2d.fromDegrees(-25)),
                new ParallelCommandGroup(
                    robotContainer.intake.spinIntake(() -> 35), 
                    robotContainer.feeder.spinFeeder(() -> -45)),
                // robotContainer.swerveDrive.moveAndTurn(-5.9, 0.2, Rotation2d.fromDegrees(-25)),

                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(-5.95, 1.5, Rotation2d.fromDegrees(0)),
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 5),
                    SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 1, () -> 0.4, () -> -40)),
                robotContainer.swerveDrive.moveAndTurn(-5.5, 1.5, Rotation2d.fromDegrees(0)),
                
                new ParallelCommandGroup(
                    robotContainer.intake.spinIntake(() -> 35), 
                    robotContainer.feeder.spinFeeder(() -> -45)),

                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(-5.95, 2.6, Rotation2d.fromDegrees(25)),
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 3.5),
                SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 1, () -> 0.4, () -> -40)));
                    // robotContainer.swerveDrive.moveAndTurn(-5.9, 1.5, Rotation2d.fromDegrees(0)),
                // robotContainer.swerveDrive.moveAndTurn(-5.65, 1.5, Rotation2d.fromDegrees(0)),
                // robotContainer.swerveDrive.moveAndTurn(-5.9, 1.5, Rotation2d.fromDegrees(0)),

                // robotContainer.swerveDrive.moveAndTurn(-5.9, 2.6, Rotation2d.fromDegrees(25)),
                // robotContainer.swerveDrive.moveAndTurn(-5.55, 2.6, Rotation2d.fromDegrees(25)),
                // robotContainer.swerveDrive.moveAndTurn(-5.9, 2.6, Rotation2d.fromDegrees(25)));

            case BLUE_FULL:
            return Commands.sequence(
                new PrintCommand("Blue full"),
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
