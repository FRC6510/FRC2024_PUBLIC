package frc.robot.commands.autonomous;

import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Feeder.Feeder;
import frc.robot.subsystems.Intake.Intake;

import frc.robot.RobotContainer;
import frc.robot.commands.MoveAndDo;
import static frc.robot.commands.autonomous.FieldCoordinates.*;

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
        BLUE_FIVE_W1_FOR_RED,
        BLUE_FIVE_W2,
        BLUE_THREE_W5,
        BLUE_NO_WHITE,
        RED_FIVE_W1,
        RED_FIVE_W2,
        RED_THREE_W5,
        SHOOT_AND_CROSS_LINE_BLUE,  
        SHOOT_AND_CROSS_LINE_RED,
        BLUE_TEST_CURVING,
        BLUE_JUST_MOVE,
        PLAYOFFS_AUTO_MOVE_THREE_METRES,
        PLAYOFFS_AUTO_SHOOT,
        BLUE_MIIDLE_3_NOTES,
        BLUE_W1_REDONE,
        BLUE_RIGHT_REDONE,
        RED_W1_REDONE,
        RED_LEFT_REDONE,
        test,
        FOR_AYSEL,;
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

            // case TEST_INTAKING:
            // return Commands.sequence(
            //     new PrintCommand("Cycles test"),
            //     new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
            //         new Pose2d(0,0, new Rotation2d(0))), 
            //         robotContainer.swerveDrive),
            //     // front 3  
            //     robotContainer.swerveDrive.moveAndTurn(1, 0, Rotation2d.fromDegrees(0)),
            //     SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 1, () -> 0.4, () -> -40),
            //     new ParallelCommandGroup(
            //         robotContainer.intake.spinIntake(() -> 35, robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> -45)),
            //     robotContainer.swerveDrive.moveAndTurn(1.5, 0, Rotation2d.fromDegrees(0)),
            //     robotContainer.swerveDrive.moveAndTurn(1, 0, Rotation2d.fromDegrees(0)),
                // SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 1, () -> 0.4, () -> -40));

            case TEST_TURNING:
            return Commands.sequence(
                new PrintCommand("Test turning"),
                new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
                    new Pose2d(0, 0, new Rotation2d(0))), 
                    robotContainer.swerveDrive),
               robotContainer.swerveDrive.moveTo(1, 0),
                robotContainer.swerveDrive.turnTo(Rotation2d.fromDegrees(-90)));
               

            // case BLUE_FIVE_W1:
            // return Commands.sequence(
            //     robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(1),
            //     new PrintCommand("Blue test"),
            //     new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
            //         new Pose2d(-7.1, 1.5, new Rotation2d(0))), 
            //         robotContainer.swerveDrive),
            //     // front 3  
            //     new InstantCommand(() -> robotContainer.shooterPivot.setIdle(NeutralModeValue.Brake), robotContainer.shooterPivot),
            //     //shoot preload
            //     new ParallelCommandGroup(   
            //         robotContainer.swerveDrive.moveAndTurn(-6.3, 0.2, Rotation2d.fromDegrees(-42)),//
            //         new SequentialCommandGroup(
            //             new WaitCommand(0.2),
            //             SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40)),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 15)).withTimeout(2), //13.5
                
            //     //go to 1st note, intake 1st note, shoot 1st note
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(-5.7, 0.2, Rotation2d.fromDegrees(20)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED),
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 23)).withTimeout(2), //16

            //     //shoot 1st note, go to 2nd note
            //     robotContainer.swerveDrive.moveAndTurn(-6.3, 1.5, Rotation2d.fromDegrees(0)).withTimeout(1.5),
                
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(-5.7, 1.5, Rotation2d.fromDegrees(0)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED),
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 20)).withTimeout(2), //18.5
            
            //     //shoot 2nd note, go to 3rd note 
            //     robotContainer.swerveDrive.moveAndTurn(-6.1, 2.7, Rotation2d.fromDegrees(25)).withTimeout(1.5),
                
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(-5.4, 2.8, Rotation2d.fromDegrees(25)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED)),
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(-5.4, 2.8, Rotation2d.fromDegrees(32)),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 17),
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40)).withTimeout(2),

            //     //shoot 3rd note, go to 4th note 
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(0, 3.35, Rotation2d.fromDegrees(0)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED)).withTimeout(2),
                
            //     new ParallelCommandGroup(
            //         robotContainer.intake.spinIntake(() -> 30, robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> -30),
            //         robotContainer.swerveDrive.moveAndTurn(-3.7, 2.5, Rotation2d.fromDegrees(17)),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 16),
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 1, () -> 0.5, () -> -50)).withTimeout(2.5),
            //     robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 16).withTimeout(1.5));

            // case RED_FIVE_W1:
            // return Commands.sequence(
            //     robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(1),
            //     new PrintCommand("Red test"),
            //     new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
            //         new Pose2d(7.1, 1.5, Rotation2d.fromDegrees(-180))), 
            //         robotContainer.swerveDrive),
            //     // front 3  
            //     new InstantCommand(() -> robotContainer.shooterPivot.setIdle(NeutralModeValue.Brake), robotContainer.shooterPivot),
            //     //shoot preload

            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(6.3, 0.2, Rotation2d.fromDegrees(42)),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 12),
            //         new SequentialCommandGroup(
            //             new WaitCommand(0.2),
            //             SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40))).withTimeout(2.5),

            //     //go to 1st note, intake 1st note, shoot 1st note
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(5.7, 0.2, Rotation2d.fromDegrees(20)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 14.3),
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.8, () -> 0.4, () -> -40)).withTimeout(2),

            //     //shoot 1st note, go to 2nd note
            //     robotContainer.swerveDrive.moveAndTurn(6.3, 1.5, Rotation2d.fromDegrees(-180)).withTimeout(1.5),
                
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(5.7, 1.5, Rotation2d.fromDegrees(-180)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 13.5),
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.8, () -> 0.4, () -> -40)).withTimeout(2),
            
            //     //shoot 2nd note, go to 3rd note 
            //     robotContainer.swerveDrive.moveAndTurn(6.1, 2.8, Rotation2d.fromDegrees(-25)).withTimeout(1.5),
                
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(5.4, 2.8, Rotation2d.fromDegrees(-25)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 14.3),
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.8, () -> 0.4, () -> -40)).withTimeout(2),
                
            //     //shoot 3rd note, go to 4th note 
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(0, 3.35, Rotation2d.fromDegrees(-180)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() ->Feeder.FEEDER_SPEED)).withTimeout(2),
                
            //     new ParallelCommandGroup(
            //         robotContainer.intake.spinIntake(() -> 30, robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> -30),
            //         robotContainer.swerveDrive.moveAndTurn(4, 2.5, Rotation2d.fromDegrees(-17)),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 19.9),
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 1, () -> 0.4, () -> -40)).withTimeout(2.5),
            //     robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(1.5));

            // case BLUE_FIVE_W2:
            // return Commands.sequence(
            //     robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0),
            //     new PrintCommand("Blue test"),
            //     new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
            //         new Pose2d(-7.1, 1.5, new Rotation2d(0))), 
            //         robotContainer.swerveDrive),
            //     // front 3  
            //     new InstantCommand(() -> robotContainer.shooterPivot.setIdle(NeutralModeValue.Brake), robotContainer.shooterPivot),
            //     //shoot preload
            //     new ParallelCommandGroup(
            //         SpinFeedShooter.autoSpinFeedShootWithCoords(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.8, () -> 0.4, () -> -40, -6.3, 0.2),             
            //         robotContainer.swerveDrive.moveAndTurn(-6.3, 0.2, Rotation2d.fromDegrees(-35)),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 12)).withTimeout(2),
                
            //     //go to 1st note, intake 1st note, shoot 1st note
            //     new ParallelCommandGroup(
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.8, () -> 0.4, () -> -40),
            //         robotContainer.swerveDrive.moveAndTurn(-5.75, 0.2, Rotation2d.fromDegrees(-25)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 15)).withTimeout(2),

            //     //shoot 1st note, go to 2nd note
            //     robotContainer.swerveDrive.moveAndTurn(-6.3, 1.5, Rotation2d.fromDegrees(0)).withTimeout(1.5),
                
            //     new ParallelCommandGroup(
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.8, () -> 0.4, () -> -40),
            //         robotContainer.swerveDrive.moveAndTurn(-5.7, 1.5, Rotation2d.fromDegrees(0)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 13.5)).withTimeout(2),
            
            //     //shoot 2nd note, go to 3rd note 
            //     robotContainer.swerveDrive.moveAndTurn(-6.1, 2.8, Rotation2d.fromDegrees(25)).withTimeout(1.5),
                
            //     new ParallelCommandGroup(
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.8, () -> 0.4, () -> -40),
            //         robotContainer.swerveDrive.moveAndTurn(-5.7, 2.8, Rotation2d.fromDegrees(25)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 15)).withTimeout(2),
                
            //     //shoot 3rd note, go to 4th note 
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(0, 1.7, Rotation2d.fromDegrees(-20)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED)).withTimeout(2.5),
                
            //     robotContainer.swerveDrive.moveAndTurn(-4, 2.5, Rotation2d.fromDegrees(-10)).withTimeout(2),
                
            //     new ParallelCommandGroup(
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 1, () -> 0.4, () -> -40),
            //         robotContainer.swerveDrive.moveAndTurn(-4, 2.2, Rotation2d.fromDegrees(12.5)),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 20.8)).withTimeout(2.5),
            //     robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(2));
            
            // case RED_FIVE_W2:
            // return Commands.sequence(
            //     robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(1),
            //     new PrintCommand("Blue test"),
            //     new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
            //         new Pose2d(7.1, 1.5, Rotation2d.fromDegrees(0))), 
            //         robotContainer.swerveDrive),
            //     // front 3  
            //     new InstantCommand(() -> robotContainer.shooterPivot.setIdle(NeutralModeValue.Brake), robotContainer.shooterPivot),
            //     //shoot preload
            //     new ParallelCommandGroup(
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.8, () -> 0.4, () -> -40),             
            //         robotContainer.swerveDrive.moveAndTurn(6.3, 0.2, Rotation2d.fromDegrees(35)),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 12)).withTimeout(2),
                
            //     //go to 1st note, intake 1st note, shoot 1st note
            //     new ParallelCommandGroup(
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.8, () -> 0.4, () -> -40),
            //         robotContainer.swerveDrive.moveAndTurn(5.75, 0.2, Rotation2d.fromDegrees(25)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 15)).withTimeout(2),

            //     //shoot 1st note, go to 2nd note
            //     robotContainer.swerveDrive.moveAndTurn(6.3, 1.5, Rotation2d.fromDegrees(0)).withTimeout(1.5),
                
            //     new ParallelCommandGroup(
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.8, () -> 0.4, () -> -40),
            //         robotContainer.swerveDrive.moveAndTurn(5.7, 1.5, Rotation2d.fromDegrees(0)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 13.5)).withTimeout(2),
            
            //     //shoot 2nd note, go to 3rd note 
            //     robotContainer.swerveDrive.moveAndTurn(6.1, 2.8, Rotation2d.fromDegrees(-25)).withTimeout(1.5),
                
            //     new ParallelCommandGroup(
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.8, () -> 0.4, () -> -40),
            //         robotContainer.swerveDrive.moveAndTurn(5.7, 2.8, Rotation2d.fromDegrees(-25)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 15)).withTimeout(2),
                
            //     //shoot 3rd note, go to 4th note 
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(0, 1.7, Rotation2d.fromDegrees(20)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED)).withTimeout(2.5),
                
            //     robotContainer.swerveDrive.moveAndTurn(4, 2.5, Rotation2d.fromDegrees(10)).withTimeout(2),
                
            //     new ParallelCommandGroup(
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 1, () -> 0.4, () -> -40),
            //         robotContainer.swerveDrive.moveAndTurn(4, 2.2, Rotation2d.fromDegrees(-12.5)),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 21.5)).withTimeout(2.5),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(2));

            // case BLUE_THREE_W5:
            //     return Commands.sequence(
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0),
            //         new PrintCommand("Blue test"),
            //         new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
            //             new Pose2d(-7.6, 0.5, Rotation2d.fromDegrees(-61.2))), 
            //             robotContainer.swerveDrive),
            //         // front 3  
            //         new WaitCommand(2),
            //         new InstantCommand(() -> robotContainer.shooterPivot.setIdle(NeutralModeValue.Brake), robotContainer.shooterPivot),
            //         //shoot preload             
            //         new ParallelCommandGroup(
            //             robotContainer.swerveDrive.moveAndTurn(-6.3, 0.2, Rotation2d.fromDegrees(-35)),
            //             robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 12.5)).withTimeout(2),
                    
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.8, () -> 0.4, () -> -40));           

                //     robotContainer.swerveDrive.moveAndTurnCurve(-5.5, -2.5, 0.2,  Roatation2d.fromDegrees(-61.2)),
                //     robotContainer.swerveDrive.moveAndTurnCurve(-5.5, -2.5, 0,  Rotation2d.fromDegrees(0)),

                //     //go to 1st note, intake 1st note, shoot 1st note
                //     new ParallelCommandGroup(
                //         robotContainer.swerveDrive.moveAndTurn(0, -3.65,  Rotation2d.fromDegrees(0)),
                //         robotContainer.intake.spinIntake(() -> 40, robotContainer.feeder, robotContainer.lift), 
                //         robotContainer.feeder.spinFeeder(() -> -40)).withTimeout(2),
                        
                //     new ParallelCommandGroup(
                //         robotContainer.swerveDrive.moveAndTurnCurve(-1.5, 0, 0.4,  Rotation2d.fromDegrees(0)),
                //         robotContainer.intake.spinIntake(() -> 40, robotContainer.feeder, robotContainer.lift), 
                //         robotContainer.feeder.spinFeeder(() -> -40)).withTimeout(2),

                //     robotContainer.swerveDrive.moveAndTurnCurve(-4.5, 0.5, 0.4,  Rotation2d.fromDegrees(-10)),
                    
                //     new ParallelCommandGroup(
                //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 0.9, () -> 0.4, () -> -40),             
                //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 15)).withTimeout(1.5),
                        
                //     robotContainer.swerveDrive.moveAndTurnCurve(-1.5, 0, 0.4,  Rotation2d.fromDegrees(0)),
                    
                //     new ParallelCommandGroup(
                //         robotContainer.swerveDrive.moveAndTurn(0, -1.8,  Rotation2d.fromDegrees(0)),
                //         robotContainer.intake.spinIntake(() -> 40, robotContainer.feeder, robotContainer.lift), 
                //         robotContainer.feeder.spinFeeder(() -> -40)).withTimeout(2),
                        
                //         new ParallelCommandGroup(
                //             robotContainer.swerveDrive.moveAndTurnCurve(-1.5, 0, 0.4,  Rotation2d.fromDegrees(0)),
                //             robotContainer.intake.spinIntake(() -> 40, robotContainer.feeder, robotContainer.lift), 
                //             robotContainer.feeder.spinFeeder(() -> -40)).withTimeout(2),

                //     robotContainer.swerveDrive.moveAndTurnCurve(-4.5, 0.5, 0.4,  Rotation2d.fromDegrees(-10)),
                    
                //     new ParallelCommandGroup(
                //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 0.9, () -> 0.4, () -> -40),             
                //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 15)).withTimeout(1.5));//,
      

            // case BLUE_NO_WHITE:
            // return Commands.sequence(
            //     robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(1),
            //     new PrintCommand("Blue test"),
            //     new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
            //         new Pose2d(-7.1, 1.5, new Rotation2d(0))), 
            //         robotContainer.swerveDrive),
            //     // front 3  
            //     new InstantCommand(() -> robotContainer.shooterPivot.setIdle(NeutralModeValue.Brake), robotContainer.shooterPivot),
            //     //shoot preload
            //     new ParallelCommandGroup(   
            //         robotContainer.swerveDrive.moveAndTurn(-6.3, 0.2, Rotation2d.fromDegrees(-42)),
            //         new SequentialCommandGroup(
            //             new WaitCommand(0.2),
            //             SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40)),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 12)).withTimeout(2),
                
            //     //go to 1st note, intake 1st note, shoot 1st note
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(-5.7, 0.2, Rotation2d.fromDegrees(-20)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED),
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 14.5)).withTimeout(2),

            //     //shoot 1st note, go to 2nd note
            //     robotContainer.swerveDrive.moveAndTurn(-6.3, 1.5, Rotation2d.fromDegrees(0)).withTimeout(1.5),
                
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(-5.7, 1.5, Rotation2d.fromDegrees(0)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED),
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 13.5)).withTimeout(2),
            
            //     //shoot 2nd note, go to 3rd note 
            //     robotContainer.swerveDrive.moveAndTurn(-6.1, 2.7, Rotation2d.fromDegrees(25)).withTimeout(1.5),
                
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(-5.4, 2.8, Rotation2d.fromDegrees(25)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 14.5),
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40)).withTimeout(2),
                
            //     robotContainer.swerveDrive.moveAndTurn(-5.4, 2.8, Rotation2d.fromDegrees(0)).withTimeout(2));

            // case BLUE_FIVE_W1_FOR_RED:
            // return Commands.sequence(
            //     robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(1),
            //     new PrintCommand("Blue test"),
            //     new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
            //         new Pose2d(-7.1, 1.5, new Rotation2d(0))), 
            //         robotContainer.swerveDrive),
            //     // front 3  
            //     new InstantCommand(() -> robotContainer.shooterPivot.setIdle(NeutralModeValue.Brake), robotContainer.shooterPivot),
            //     //shoot preload
            //     new ParallelCommandGroup(   
            //         robotContainer.swerveDrive.moveAndTurn(-6.3, 0.2, Rotation2d.fromDegrees(-42)),
            //         new SequentialCommandGroup(
            //             new WaitCommand(0.2),
            //             SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40)),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 12)).withTimeout(2),
                
            //     //go to 1st note, intake 1st note, shoot 1st note
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(-5.7, 0.2, Rotation2d.fromDegrees(-20)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED),
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 14.5)).withTimeout(2),

            //     //shoot 1st note, go to 2nd note
            //     robotContainer.swerveDrive.moveAndTurn(-6.3, 1.5, Rotation2d.fromDegrees(0)).withTimeout(1.5),
                
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(-5.7, 1.5, Rotation2d.fromDegrees(0)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED),
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 13.5)).withTimeout(2),
            
            //     //shoot 2nd note, go to 3rd note 
            //     robotContainer.swerveDrive.moveAndTurn(-6.1, 2.7, Rotation2d.fromDegrees(25)).withTimeout(1.5),
                
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(-5.75, 2.8, Rotation2d.fromDegrees(25)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 14.5),
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40)).withTimeout(2));

            // case SHOOT_AND_CROSS_LINE_BLUE:
            //  return Commands.sequence(
            //     robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(1),
            //     new PrintCommand("Blue test"),
            //     new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
            //         new Pose2d(-7.1, 1.5, new Rotation2d(0))), 
            //         robotContainer.swerveDrive),
            //     // front 3  
            //     new InstantCommand(() -> robotContainer.shooterPivot.setIdle(NeutralModeValue.Brake), robotContainer.shooterPivot),
            //     //shoot preload
            //     new WaitCommand(3),
            //     new ParallelCommandGroup(   
            //         robotContainer.swerveDrive.moveAndTurn(-6.3, 0.2, Rotation2d.fromDegrees(-42)),
            //         new SequentialCommandGroup(
            //             new WaitCommand(0.2),
            //             SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40)),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 12)).withTimeout(2),
                    
            //     robotContainer.swerveDrive.moveAndTurn(-5.7, 0.2, Rotation2d.fromDegrees(-20)),
            //     robotContainer.swerveDrive.moveAndTurn(-5.7, 0.2, Rotation2d.fromDegrees(0)));
            
            // case SHOOT_AND_CROSS_LINE_RED:
            //  return Commands.sequence(
            //     robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(1),
            //     new PrintCommand("Blue test"),
            //     new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
            //         new Pose2d(-7.1, 1.5, new Rotation2d(0))), 
            //         robotContainer.swerveDrive),
            //     // front 3  
            //     new InstantCommand(() -> robotContainer.shooterPivot.setIdle(NeutralModeValue.Brake), robotContainer.shooterPivot),
            //     //shoot preload
            //     new WaitCommand(3),
            //     new ParallelCommandGroup(   
            //         robotContainer.swerveDrive.moveAndTurn(-6.3, 0.2, Rotation2d.fromDegrees(42)),
            //         new SequentialCommandGroup(
            //             new WaitCommand(0.2),
            //             SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40)),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 12)).withTimeout(2),
                    
            //     robotContainer.swerveDrive.moveAndTurn(-5.7, 0.2, Rotation2d.fromDegrees(20)),
            //     robotContainer.swerveDrive.moveAndTurn(-5.7, 0.2, Rotation2d.fromDegrees(0)));
            
            // case PLAYOFFS_AUTO_MOVE_THREE_METRES:
            //  return Commands.sequence(
            //     // robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(1),
            //     new PrintCommand("Playoffs"),
            //     new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
            //         new Pose2d(0, 0, new Rotation2d(0))), 
            //         robotContainer.swerveDrive),
            //     // front 3  
            //     new InstantCommand(() -> robotContainer.shooterPivot.setIdle(NeutralModeValue.Brake), robotContainer.shooterPivot),
            //     //shoot preload
            //     new WaitCommand(13),
            //     new ParallelCommandGroup(   
            //         robotContainer.swerveDrive.moveAndTurn(3, 0, Rotation2d.fromDegrees(0))));
            //     //     new SequentialCommandGroup(
            //     //         new WaitCommand(1),
            //     //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, () -> 0.9, () -> 0.4, () -> -40)),
            //     //     robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 15)).withTimeout(2),
            //     // robotContainer.swerveDrive.moveAndTurn(-5.4, 2.8, Rotation2d.fromDegrees(0)));
            
            // case PLAYOFFS_AUTO_SHOOT:
            //  return Commands.sequence(
            //     // robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(1),
            //     new PrintCommand("Playoffs"),
            //     new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
            //         new Pose2d(0, 0, new Rotation2d(0))), 
            //         robotContainer.swerveDrive),
            //     // front 3  
            //     new InstantCommand(() -> robotContainer.shooterPivot.setIdle(NeutralModeValue.Brake), robotContainer.shooterPivot),
            //     //shoot preload
            //     new WaitCommand(11.5),
            //     new ParallelCommandGroup(   
            //         robotContainer.swerveDrive.moveAndTurn(2.8, 0, Rotation2d.fromDegrees(-20)),
            //         new SequentialCommandGroup(
            //             new WaitCommand(0.2),
            //             SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40)),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 15)).withTimeout(2),

            //     new ParallelCommandGroup( 
            //         robotContainer.swerveDrive.moveAndTurn(2.8, 0, Rotation2d.fromDegrees(0)))
            //         );

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
            
            // case BLUE_MIIDLE_3_NOTES:
            // return Commands.sequence(
            //     robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 4).withTimeout(1),
            //     new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
            //         new Pose2d(-7.84, -0.033, new Rotation2d(0))), 
            //         robotContainer.swerveDrive),

                
            //     // front 3  
            //     new InstantCommand(() -> robotContainer.shooterPivot.setIdle(NeutralModeValue.Brake), robotContainer.shooterPivot),
            
            
            //     robotContainer.swerveDrive.moveAndTurn(-7.28, 0.082, Rotation2d.fromDegrees(-60)).withTimeout(3),
                
            //     SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.55, () -> 0.4, () -> -40).withTimeout(2),

            //     //shoot 1st note, go to 2nd note
            //     robotContainer.swerveDrive.moveAndTurn(-5.64, -1.427, Rotation2d.fromDegrees(0)).withTimeout(3),

                                 
            //     robotContainer.swerveDrive.moveAndTurn(-1.365, 0, Rotation2d.fromDegrees(0)),
                  
                       
            //     //intake 2nd note
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(0.4, -0.2, Rotation2d.fromDegrees(0)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED)).withTimeout(3),
                    
            //     //move to positions to shoot 2nd note
            //     new ParallelCommandGroup(
            //     robotContainer.swerveDrive.moveAndTurn(-5, 1, Rotation2d.fromDegrees(-15)),
            //     robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 22)),
            //     //Shoot 2nd note
            //     new ParallelCommandGroup(
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 22),
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.7, () -> 0.4, () -> -40).withTimeout(2),
            //         robotContainer.swerveDrive.moveAndTurn(-5, 1, Rotation2d.fromDegrees(-15))).withTimeout(1.5),


            //     robotContainer.swerveDrive.moveAndTurn(-1.2, -0.6, Rotation2d.fromDegrees(-30)).withTimeout(3),

            //      robotContainer.swerveDrive.moveAndTurn(-1.2, -1.6, Rotation2d.fromDegrees(-30)).withTimeout(3),

            //      new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(0.4, -1.6, Rotation2d.fromDegrees(0)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED)).withTimeout(2),

            //     robotContainer.swerveDrive.moveAndTurn(-1.2, -1.6, Rotation2d.fromDegrees(-30)).withTimeout(3),
            //     robotContainer.swerveDrive.moveAndTurn(-4.15, 0.42, Rotation2d.fromDegrees(-12.4)).withTimeout(3)


            //    );

            // case BLUE_W1_REDONE:
            //     return Commands.sequence(
            //     //Set to contain preload
            //     new InstantCommand(() -> robotContainer.feeder.setContainsNote(true)),
            //     // robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(1),
            //     new PrintCommand("Blue test"),
            //     new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
            //         new Pose2d(-7.1, 1.4478, new Rotation2d(0))), 
            //         robotContainer.swerveDrive),
            //     //Shoot first note
            //     MoveAndDo.StationaryShot.ShootStationary(0.6, 0, 0, robotContainer).withTimeout(0.5),
            //     //Drive to blue right note and intake
            //     robotContainer.swerveDrive.moveAndTurn(BLUE_NOTE_RIGHT_X-NOTE_AVOIDANCE, BLUE_NOTE_RIGHT_Y, new Rotation2d(0)),
            //     new ParallelDeadlineGroup(new MoveAndDo.MoveAndIntake(new Pose2d(BLUE_NOTE_RIGHT_X-STAGE_NOTE_OFFSET, BLUE_NOTE_RIGHT_Y, Rotation2d.fromDegrees(0)), robotContainer.swerveDrive, robotContainer.intake, robotContainer.feeder, robotContainer.lift).withTimeout(2.5),
            //         this.robotContainer.shooterPivot.prepareShootAngle(21)
            //         ),
              
            //     //Drive to blue centre note then shoot blue right
            //     new ParallelDeadlineGroup(robotContainer.swerveDrive.moveTo(BLUE_NOTE_CENTRE_X-NOTE_AVOIDANCE, BLUE_NOTE_CENTRE_Y),
            //         this.robotContainer.shooterPivot.prepareShootAngle(21)
            //         //pivotAngle : 19
            //         ),
            //     MoveAndDo.StationaryShot.ShootStationary(Shooter.SHOOTER_AUTO_POWER, 21, 0, robotContainer).withTimeout(2),
            //     robotContainer.swerveDrive.turnTo(Rotation2d.fromDegrees(0)),
            //     //Intake blue centre and shoot it.
            //     new MoveAndDo.MoveAndIntake(new Pose2d(BLUE_NOTE_CENTRE_X, BLUE_NOTE_CENTRE_Y, Rotation2d.fromDegrees(0)), robotContainer.swerveDrive, robotContainer.intake, robotContainer.feeder, robotContainer.lift).withTimeout(2),
            //     MoveAndDo.StationaryShot.ShootStationary(robotContainer.shooter.SHOOTER_AUTO_POWER, CENTRE_NOTE_ANGLE, 0, robotContainer).withTimeout(2),
            //     //Grab blue left note and shoot
            //     robotContainer.swerveDrive.turnTo(Rotation2d.fromDegrees(90)),
            //     new MoveAndDo.MoveAndIntake(new Pose2d(BLUE_NOTE_LEFT_X, BLUE_NOTE_LEFT_Y, Rotation2d.fromDegrees(90)), robotContainer.swerveDrive, robotContainer.intake, robotContainer.feeder, robotContainer.lift).withTimeout(2),
            //     robotContainer.swerveDrive.turnTo(Rotation2d.fromDegrees(20)),
            //     MoveAndDo.StationaryShot.ShootStationary(robotContainer.shooter.SHOOTER_AUTO_POWER, 24, 20, robotContainer).withTimeout(4),
            //     //Grab leftmost note from white line and then shoot it
            //     new MoveAndDo.MoveAndIntake(new Pose2d(CENTRE_LINE_X, FIELD_CENTRE_LL_Y-0.3, Rotation2d.fromDegrees(0)), robotContainer.swerveDrive, robotContainer.intake, robotContainer.feeder, robotContainer.lift).withTimeout(2.5),
            //     robotContainer.swerveDrive.moveAndTurn(BLUE_NOTE_CENTRE_X+1.5, BLUE_NOTE_CENTRE_Y+0.5, Rotation2d.fromDegrees(4)),
            //     MoveAndDo.StationaryShot.ShootStationary(robotContainer.shooter.SHOOTER_AUTO_POWER, 23, 5, robotContainer)
            //     );

                
            // case BLUE_RIGHT_REDONE:
            //    return Commands.sequence(
            //     //Set to contain preload
            //     new InstantCommand(() -> robotContainer.feeder.setContainsNote(true)),
            //     // robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(1),
            //     new PrintCommand("Blue test"),
            //     new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(new Pose2d(-DISTANCE_CENTRE_TO_ALLIANCE+0.5, BLUE_NOTE_RIGHT_Y, Rotation2d.fromDegrees(0))),
            //         robotContainer.swerveDrive),
            //     //Shoot first note
            //     new ParallelDeadlineGroup(
            //         robotContainer.swerveDrive.moveAndTurn(BLUE_NOTE_CENTRE_X, -BLUE_NOTE_CENTRE_Y, Rotation2d.fromDegrees(-40)), 
            //         this.robotContainer.shooterPivot.prepareShootAngle(25) //20
            //         ).withTimeout(3),
            //     MoveAndDo.StationaryShot.ShootStationary(robotContainer.shooter.SHOOTER_AUTO_POWER, 23, -47, robotContainer).withTimeout(1.8),
            //     //grab second note
            //     robotContainer.swerveDrive.moveAndTurn(STAGE_TAPE_X-1, FIELD_CENTRE_RR_Y, Rotation2d.fromDegrees(0)),
            //     new MoveAndDo.MoveAndIntake(new Pose2d(CENTRE_LINE_X, FIELD_CENTRE_RR_Y+0.1, Rotation2d.fromDegrees(0)), robotContainer.swerveDrive, robotContainer.intake, robotContainer.feeder, robotContainer.lift).withTimeout(2),
            //     robotContainer.swerveDrive.moveAndTurn(BLUE_NOTE_CENTRE_X, -BLUE_NOTE_CENTRE_Y, Rotation2d.fromDegrees(-47)),
            //     MoveAndDo.StationaryShot.ShootStationary(robotContainer.shooter.SHOOTER_AUTO_POWER, 48, -47, robotContainer).withTimeout(2.5), //23
            //     //grab 3rd note
            //     robotContainer.swerveDrive.moveAndTurn(STAGE_TAPE_X, BLUE_NOTE_RIGHT_Y, Rotation2d.fromDegrees(0)),
            //     new MoveAndDo.MoveAndIntake(new Pose2d(CENTRE_LINE_X, FIELD_CENTRE_C_Y+0.1, Rotation2d.fromDegrees(0)), robotContainer.swerveDrive, robotContainer.intake, robotContainer.feeder, robotContainer.lift).withTimeout(2.5),
            //     robotContainer.swerveDrive.moveAndTurn(BLUE_NOTE_CENTRE_X+3, BLUE_NOTE_RIGHT_Y, Rotation2d.fromDegrees(-10)),
            //     robotContainer.swerveDrive.moveAndTurn(BLUE_NOTE_CENTRE_X+1.4, BLUE_NOTE_CENTRE_Y-0.7, Rotation2d.fromDegrees(-10)),
            //     MoveAndDo.StationaryShot.ShootStationary(0.9, 19,  -10, robotContainer)
            //    );
            //     //grab centre note



            //     case RED_W1_REDONE:
            //     return Commands.sequence(
            //     //Set to contain preload
            //     new InstantCommand(() -> robotContainer.feeder.setContainsNote(true)),
            //     // robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(1),
            //     new PrintCommand("Red 5 note"),
            //     new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
            //         new Pose2d(-7.1, RED_NOTE_CENTRE_Y, new Rotation2d(0))), 
            //         robotContainer.swerveDrive),
            //     //Shoot first note
            //     MoveAndDo.StationaryShot.ShootStationary(0.6, 0, 0, robotContainer).withTimeout(0.5),
            //     //Drive to red left note and intake
            //     robotContainer.swerveDrive.moveAndTurn(RED_NOTE_LEFT_X-NOTE_AVOIDANCE, RED_NOTE_LEFT_Y, new Rotation2d(0)),
            //     new ParallelDeadlineGroup(new MoveAndDo.MoveAndIntake(new Pose2d(RED_NOTE_LEFT_X-STAGE_NOTE_OFFSET, RED_NOTE_LEFT_Y, Rotation2d.fromDegrees(0)), robotContainer.swerveDrive, robotContainer.intake, robotContainer.feeder, robotContainer.lift).withTimeout(2.5),
            //         this.robotContainer.shooterPivot.prepareShootAngle(19)
            //         ),
              
            //     //Drive to red centre note then shoot red left
            //     new ParallelDeadlineGroup(robotContainer.swerveDrive.moveTo(RED_NOTE_CENTRE_X-NOTE_AVOIDANCE, RED_NOTE_CENTRE_Y),
            //         this.robotContainer.shooterPivot.prepareShootAngle(19)
            //         ),
            //     MoveAndDo.StationaryShot.ShootStationary(Shooter.SHOOTER_AUTO_POWER, 19, 0, robotContainer).withTimeout(2),
            //     robotContainer.swerveDrive.turnTo(Rotation2d.fromDegrees(0)),
            //     //Intake red centre and shoot it.
            //     new MoveAndDo.MoveAndIntake(new Pose2d(RED_NOTE_CENTRE_X, RED_NOTE_CENTRE_Y, Rotation2d.fromDegrees(0)), robotContainer.swerveDrive, robotContainer.intake, robotContainer.feeder, robotContainer.lift).withTimeout(2),
            //     MoveAndDo.StationaryShot.ShootStationary(robotContainer.shooter.SHOOTER_AUTO_POWER, CENTRE_NOTE_ANGLE, 0, robotContainer).withTimeout(2),
            //     //Grab red right note and shoot
            //     robotContainer.swerveDrive.turnTo(Rotation2d.fromDegrees(-90)),
            //     new MoveAndDo.MoveAndIntake(new Pose2d(RED_NOTE_RIGHT_X, RED_NOTE_RIGHT_Y, Rotation2d.fromDegrees(-90)), robotContainer.swerveDrive, robotContainer.intake, robotContainer.feeder, robotContainer.lift).withTimeout(2),
            //     robotContainer.swerveDrive.turnTo(Rotation2d.fromDegrees(-20)),
            //     MoveAndDo.StationaryShot.ShootStationary(robotContainer.shooter.SHOOTER_AUTO_POWER,CENTRE_NOTE_ANGLE, -20, robotContainer).withTimeout(4),
            //     //Grab rightmost note from white line and then shoot it
            //     new MoveAndDo.MoveAndIntake(new Pose2d(CENTRE_LINE_X, FIELD_CENTRE_RR_Y-0.3, Rotation2d.fromDegrees(0)), robotContainer.swerveDrive, robotContainer.intake, robotContainer.feeder, robotContainer.lift).withTimeout(2.5),
            //     robotContainer.swerveDrive.moveAndTurn(RED_NOTE_CENTRE_X+1.5, RED_NOTE_CENTRE_Y-0.5, Rotation2d.fromDegrees(-1)),
            //     MoveAndDo.StationaryShot.ShootStationary(robotContainer.shooter.SHOOTER_AUTO_POWER, 23, -1, robotContainer)
            //     );


            // case RED_LEFT_REDONE:
            //    return Commands.sequence(
            //     //Set to contain preload
            //     new InstantCommand(() -> robotContainer.feeder.setContainsNote(true)),
            //     // robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(1),
            //     new PrintCommand("Blue test"),
            //     new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(new Pose2d(-DISTANCE_CENTRE_TO_ALLIANCE+0.5, RED_NOTE_LEFT_Y, Rotation2d.fromDegrees(0))),
            //         robotContainer.swerveDrive),
            //     //Shoot first note
            //     new ParallelDeadlineGroup(
            //         robotContainer.swerveDrive.moveAndTurn(RED_NOTE_CENTRE_X, -RED_NOTE_CENTRE_Y, Rotation2d.fromDegrees(50)), 
            //         this.robotContainer.shooterPivot.prepareShootAngle(22)
            //         ).withTimeout(3),
            //     MoveAndDo.StationaryShot.ShootStationary(robotContainer.shooter.SHOOTER_AUTO_POWER, 22, 47, robotContainer).withTimeout(1.8),
            //     //grab second note
            //     robotContainer.swerveDrive.moveAndTurn(STAGE_TAPE_X-1, FIELD_CENTRE_LL_Y-1.2, Rotation2d.fromDegrees(0)),
            //     new MoveAndDo.MoveAndIntake(new Pose2d(CENTRE_LINE_X+0.2, FIELD_CENTRE_LL_Y-0.25, Rotation2d.fromDegrees(0)), robotContainer.swerveDrive, robotContainer.intake, robotContainer.feeder, robotContainer.lift).withTimeout(2),
            //     robotContainer.swerveDrive.moveAndTurn(RED_NOTE_CENTRE_X, -RED_NOTE_CENTRE_Y, Rotation2d.fromDegrees(30)), //42
            //     MoveAndDo.StationaryShot.ShootStationary(robotContainer.shooter.SHOOTER_AUTO_POWER, 23, 47, robotContainer).withTimeout(2.5),
            //     //grab 3rd note
            //     robotContainer.swerveDrive.moveAndTurn(STAGE_TAPE_X, RED_NOTE_LEFT_Y, Rotation2d.fromDegrees(0)),
            //     new MoveAndDo.MoveAndIntake(new Pose2d(CENTRE_LINE_X+0.2, FIELD_CENTRE_C_Y-0.4, Rotation2d.fromDegrees(0)), robotContainer.swerveDrive, robotContainer.intake, robotContainer.feeder, robotContainer.lift).withTimeout(2.5),
            //     robotContainer.swerveDrive.moveAndTurn(RED_NOTE_CENTRE_X+3, RED_NOTE_LEFT_Y, Rotation2d.fromDegrees(10)),
            //     robotContainer.swerveDrive.moveAndTurn(RED_NOTE_CENTRE_X+1.5, RED_NOTE_CENTRE_Y+0.5, Rotation2d.fromDegrees(10)),
            //     MoveAndDo.StationaryShot.ShootStationary(0.9, 23,  10, robotContainer)
            //     //increase pivot angle = increase angle
            //    );
            
            
            //        case test:
            // return Commands.sequence(
            //     robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 0).withTimeout(1),
            //     new PrintCommand("Blue test"),
            //     new InstantCommand(() -> robotContainer.swerveDrive.setRobotPosition(
            //         new Pose2d(-7.1, 1.5, new Rotation2d(0))), 
            //         robotContainer.swerveDrive),
            //     // front 3  
            //     new InstantCommand(() -> robotContainer.shooterPivot.setIdle(NeutralModeValue.Brake), robotContainer.shooterPivot),
            //     //shoot preload
            //     new ParallelCommandGroup(   
            //         robotContainer.swerveDrive.moveAndTurn(-6.1, 2.7, Rotation2d.fromDegrees(42)),
            //         new SequentialCommandGroup(
            //             new WaitCommand(0.2),
            //             SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40)),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 15)).withTimeout(2), //13.5
            //     ////////////////////////////up to here works


            //     //go to 1st note, intake 1st note, shoot 1st note
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(-5.8, 2.8, Rotation2d.fromDegrees(25)),///////
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED)).withTimeout(2),
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(-5.8, 2.8, Rotation2d.fromDegrees(32)),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 24),
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40)).withTimeout(2),

            //     //shoot 1st note, go to 2nd note
            //     robotContainer.swerveDrive.moveAndTurn(-6.3, 1.5, Rotation2d.fromDegrees(0)).withTimeout(1.5),
                
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(-5.4, 1.5, Rotation2d.fromDegrees(0)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED),
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 20)).withTimeout(2), //18.5
            
            //          robotContainer.swerveDrive.moveAndTurn(-6.3, 0.2, Rotation2d.fromDegrees(-15)),//

            //     //shoot 2nd note, go to 3rd note 
    
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(-5.3, 0.1, Rotation2d.fromDegrees(-15)),//-42
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED),
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 0.9, () -> 0.4, () -> -40),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 23)).withTimeout(2), //16

            //     //shoot 3rd note, go to 4th note 
            //     new ParallelCommandGroup(
            //         robotContainer.swerveDrive.moveAndTurn(0, -0.3, Rotation2d.fromDegrees(0)),
            //         robotContainer.intake.spinIntake(() -> Intake.INTAKING_SPEED_AUTO,robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> Feeder.FEEDER_SPEED)).withTimeout(2),
                
            //     new ParallelCommandGroup(
            //         robotContainer.intake.spinIntake(() -> 30, robotContainer.feeder, robotContainer.lift), 
            //         robotContainer.feeder.spinFeeder(() -> -30),
            //         robotContainer.swerveDrive.moveAndTurn(-3.7, 1, Rotation2d.fromDegrees(-17)),
            //         robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 16),
            //         SpinFeedShooter.autoSpinFeedShoot(robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.swerveDrive, () -> 1, () -> 0.5, () -> -50)).withTimeout(2.5),
            //     robotContainer.shooterPivot.shooterPivotPID(robotContainer.shooterPivot, () -> 16).withTimeout(1.5));
                default:
                return Commands.none();

        }
    }
}