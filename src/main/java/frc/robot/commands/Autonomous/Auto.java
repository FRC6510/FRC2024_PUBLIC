package frc.robot.commands.Autonomous;

import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.robot.subsystems.Shooter.SpinFeedShooter;
import frc.robot.RobotContainer;

public class Auto {
    RobotContainer robotContainer;
    public Auto(RobotContainer m_robotContainer){
        this.robotContainer = m_robotContainer;
    }

    public static enum AutoList {
        TEST_INTAKING,
        FIGURE_EIGHTS,
        TEST_TURNING,
        BLUE_FIVE_W1,
        BLUE_FIVE_W2,
        RED_FIVE_W1,
        RED_FIVE_W2,
        BLUE_JUST_MOVE;
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

            case TEST_INTAKING:
            return Commands.sequence(
                new PrintCommand("Cycles test"),
                new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
                    new Pose2d(0,0, new Rotation2d(0))), 
                    robotContainer.swerveDrive),
                // front 3  
                // robotContainer.swerveDrive.moveAndTurn(1, 0, Rotation2d.fromDegrees(0)),
                // SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 1, () -> 0.4, () -> -40),
                new ParallelCommandGroup(
                    robotContainer.intake.spinIntake(() -> 35, robotContainer.lift), 
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

            case BLUE_FIVE_W1:
            return Commands.sequence(
                robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(1),
                new PrintCommand("Blue test"),
                new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
                    new Pose2d(-7.1, 1.5, new Rotation2d(0))), 
                    robotContainer.swerveDrive),
                // front 3  
                new InstantCommand(() -> robotContainer.shooterPivot.setIdle(NeutralModeValue.Brake), robotContainer.shooterPivot),
                //shoot preload
                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(-6.3, 0.2, Rotation2d.fromDegrees(-35)),
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 13),
                    SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 0.8, () -> 0.4, () -> -40)).withTimeout(3),                
                
                //go to 1st note, intake 1st note, shoot 1st note
                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(-5.75, 0.2, Rotation2d.fromDegrees(-25)),
                    robotContainer.intake.spinIntake(() -> 40, robotContainer.lift), 
                    robotContainer.feeder.spinFeeder(() -> -40),
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 15),
                    SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 0.8, () -> 0.4, () -> -40)).withTimeout(3),

                //shoot 1st note, go to 2nd note
                robotContainer.swerveDrive.moveAndTurn(-6.3, 1.5, Rotation2d.fromDegrees(0)).withTimeout(2),
                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(-5.7, 1.5, Rotation2d.fromDegrees(0)),
                    robotContainer.intake.spinIntake(() -> 40, robotContainer.lift), 
                    robotContainer.feeder.spinFeeder(() -> -40),
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 13.5),
                    SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 0.8, () -> 0.4, () -> -40)).withTimeout(3),
            
                //shoot 2nd note, go to 3rd note 
                robotContainer.swerveDrive.moveAndTurn(-6.1, 2.8, Rotation2d.fromDegrees(25)).withTimeout(2),
                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(-5.7, 2.8, Rotation2d.fromDegrees(25)),
                    robotContainer.intake.spinIntake(() -> 40, robotContainer.lift), 
                    robotContainer.feeder.spinFeeder(() -> -40),
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 15),
                    SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 0.8, () -> 0.4, () -> -40)).withTimeout(3),
                
                //shoot 3rd note, go to 4th note 
                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(0, 3.5, Rotation2d.fromDegrees(0)),
                    robotContainer.intake.spinIntake(() -> 40, robotContainer.lift), 
                    robotContainer.feeder.spinFeeder(() -> -40)).withTimeout(2).withTimeout(3.5),
                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(-4, 2.5, Rotation2d.fromDegrees(12)),
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 20.8),
                    SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 1, () -> 0.4, () -> -40)).withTimeout(3),
                robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(1.5));

                case RED_FIVE_W1:
                return Commands.sequence(
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(1),
                    new PrintCommand("Blue test"),
                    new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
                        new Pose2d(7.1, 1.5, new Rotation2d(0))), 
                        robotContainer.swerveDrive),
                    // front 3  
                    new InstantCommand(() -> robotContainer.shooterPivot.setIdle(NeutralModeValue.Brake), robotContainer.shooterPivot),
                    //shoot preload
                    new ParallelCommandGroup(
                        robotContainer.swerveDrive.moveAndTurn(6.3, 0.2, Rotation2d.fromDegrees(35)),
                        robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 13),
                        SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 0.8, () -> 0.4, () -> -40)).withTimeout(3),                
                    
                    //go to 1st note, intake 1st note, shoot 1st note
                    new ParallelCommandGroup(
                        robotContainer.swerveDrive.moveAndTurn(5.75, 0.2, Rotation2d.fromDegrees(25)),
                        robotContainer.intake.spinIntake(() -> 40, robotContainer.lift), 
                        robotContainer.feeder.spinFeeder(() -> -40),
                        robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 15),
                        SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 0.8, () -> 0.4, () -> -40)).withTimeout(3),
    
                    //shoot 1st note, go to 2nd note
                    robotContainer.swerveDrive.moveAndTurn(6.3, 1.5, Rotation2d.fromDegrees(0)).withTimeout(2),
                    new ParallelCommandGroup(
                        robotContainer.swerveDrive.moveAndTurn(5.7, 1.5, Rotation2d.fromDegrees(0)),
                        robotContainer.intake.spinIntake(() -> 40, robotContainer.lift), 
                        robotContainer.feeder.spinFeeder(() -> -40),
                        robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 13.5),
                        SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 0.8, () -> 0.4, () -> -40)).withTimeout(3),
                
                    //shoot 2nd note, go to 3rd note 
                    robotContainer.swerveDrive.moveAndTurn(6.1, 2.8, Rotation2d.fromDegrees(-25)).withTimeout(2),
                    new ParallelCommandGroup(
                        robotContainer.swerveDrive.moveAndTurn(5.7, 2.8, Rotation2d.fromDegrees(-25)),
                        robotContainer.intake.spinIntake(() -> 40, robotContainer.lift), 
                        robotContainer.feeder.spinFeeder(() -> -40),
                        robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 15),
                        SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 0.8, () -> 0.4, () -> -40)).withTimeout(3),
                    
                    //shoot 3rd note, go to 4th note 
                    new ParallelCommandGroup(
                        robotContainer.swerveDrive.moveAndTurn(0, 3.5, Rotation2d.fromDegrees(0)),
                        robotContainer.intake.spinIntake(() -> 40, robotContainer.lift), 
                        robotContainer.feeder.spinFeeder(() -> -40)).withTimeout(2).withTimeout(3.5),
                    new ParallelCommandGroup(
                        robotContainer.swerveDrive.moveAndTurn(4, 2.5, Rotation2d.fromDegrees(-12)),
                        robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 20.8),
                        SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 1, () -> 0.4, () -> -40)).withTimeout(3),
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(1.5));
    
            case BLUE_FIVE_W2:
            return Commands.sequence(
                robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0),
                new PrintCommand("Blue test"),
                new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
                    new Pose2d(-7.1, 1.5, new Rotation2d(0))), 
                    robotContainer.swerveDrive),
                // front 3  
                new InstantCommand(() -> robotContainer.shooterPivot.setIdle(NeutralModeValue.Brake), robotContainer.shooterPivot),
                //shoot preload
                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(-6.3, 0.2, Rotation2d.fromDegrees(-35)),
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 13),
                    SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 0.8, () -> 0.4, () -> -40)).withTimeout(3),                
                
                //go to 1st note, intake 1st note, shoot 1st note
                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(-5.75, 0.2, Rotation2d.fromDegrees(-25)),
                    robotContainer.intake.spinIntake(() -> 40, robotContainer.lift), 
                    robotContainer.feeder.spinFeeder(() -> -40),
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 15),
                    SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 0.8, () -> 0.4, () -> -40)).withTimeout(3),

                //shoot 1st note, go to 2nd note
                robotContainer.swerveDrive.moveAndTurn(-6.3, 1.5, Rotation2d.fromDegrees(0)).withTimeout(2),
                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(-5.7, 1.5, Rotation2d.fromDegrees(0)),
                    robotContainer.intake.spinIntake(() -> 40, robotContainer.lift), 
                    robotContainer.feeder.spinFeeder(() -> -40),
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 13.5),
                    SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 0.8, () -> 0.4, () -> -40)).withTimeout(3),
            
                //shoot 2nd note, go to 3rd note 
                robotContainer.swerveDrive.moveAndTurn(-6.1, 2.8, Rotation2d.fromDegrees(25)).withTimeout(2),
                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(-5.7, 2.8, Rotation2d.fromDegrees(25)),
                    robotContainer.intake.spinIntake(() -> 40, robotContainer.lift), 
                    robotContainer.feeder.spinFeeder(() -> -40),
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 15),
                    SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 0.8, () -> 0.4, () -> -40)).withTimeout(3),
                
                //shoot 3rd note, go to 4th note 
                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(0, 1.7, Rotation2d.fromDegrees(-20)),
                    robotContainer.intake.spinIntake(() -> 40, robotContainer.lift), 
                    robotContainer.feeder.spinFeeder(() -> -40)).withTimeout(2.5),
                robotContainer.swerveDrive.moveAndTurn(-4, 2.5, Rotation2d.fromDegrees(-10)).withTimeout(2),
                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(-4, 2.2, Rotation2d.fromDegrees(12)),
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 20.8),
                    SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 1, () -> 0.4, () -> -40)).withTimeout(2.5),
                robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(2));

            case RED_FIVE_W2:
            return Commands.sequence(
                robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0),
                new PrintCommand("Blue test"),
                new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
                    new Pose2d(7.1, 1.5, new Rotation2d(0))), 
                    robotContainer.swerveDrive),
                // front 3  
                new InstantCommand(() -> robotContainer.shooterPivot.setIdle(NeutralModeValue.Brake), robotContainer.shooterPivot),
                //shoot preload
                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(6.3, 0.2, Rotation2d.fromDegrees(35)),
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 13),
                    SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 0.8, () -> 0.4, () -> -40)).withTimeout(3),                
                
                //go to 1st note, intake 1st note, shoot 1st note
                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(5.75, 0.2, Rotation2d.fromDegrees(25)),
                    robotContainer.intake.spinIntake(() -> 40, robotContainer.lift), 
                    robotContainer.feeder.spinFeeder(() -> -40),
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 15),
                    SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 0.8, () -> 0.4, () -> -40)).withTimeout(3),

                //shoot 1st note, go to 2nd note
                robotContainer.swerveDrive.moveAndTurn(6.3, 1.5, Rotation2d.fromDegrees(0)).withTimeout(2),
                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(5.7, 1.5, Rotation2d.fromDegrees(0)),
                    robotContainer.intake.spinIntake(() -> 40, robotContainer.lift), 
                    robotContainer.feeder.spinFeeder(() -> -40),
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 13.5),
                    SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 0.8, () -> 0.4, () -> -40)).withTimeout(3),
            
                //shoot 2nd note, go to 3rd note 
                robotContainer.swerveDrive.moveAndTurn(6.1, 2.8, Rotation2d.fromDegrees(-25)).withTimeout(2),
                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(5.7, 2.8, Rotation2d.fromDegrees(-25)),
                    robotContainer.intake.spinIntake(() -> 40, robotContainer.lift), 
                    robotContainer.feeder.spinFeeder(() -> -40),
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 15),
                    SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 0.8, () -> 0.4, () -> -40)).withTimeout(3),
                
                //shoot 3rd note, go to 4th note 
                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(0, 1.7, Rotation2d.fromDegrees(-20)),
                    robotContainer.intake.spinIntake(() -> 40, robotContainer.lift), 
                    robotContainer.feeder.spinFeeder(() -> -40)).withTimeout(2.5),
                robotContainer.swerveDrive.moveAndTurn(4, 2.5, Rotation2d.fromDegrees(10)).withTimeout(2),
                new ParallelCommandGroup(
                    robotContainer.swerveDrive.moveAndTurn(4, 2.2, Rotation2d.fromDegrees(-12)),
                    robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 20.8),
                    SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 1, () -> 0.4, () -> -40)).withTimeout(2.5),
                robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(2));

            case BLUE_JUST_MOVE:
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
