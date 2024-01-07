// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.Drivetrain.SwerveDrive;
import frc.robot.subsystems.limelight.LimelightShooter;
import frc.robot.subsystems.limelight.LockToTarget;
import frc.robot.subsystems.limelight.SwitchShooterPipeline.ShooterPipelineID;
import frc.robot.subsystems.limelight.FaceTargetWhileDriving;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.SpinShooter;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // CONTROLLERS:
  private final CommandXboxController driverController =
      new CommandXboxController(0);
    
  private final CommandXboxController operatorController =
      new CommandXboxController(1);

  // SUBSYEMS:
  // The robot's subsystems and commands are defined here...
  SwerveDrive swerveDrive = new SwerveDrive();
  Shooter shooter = new Shooter();
  private final LimelightShooter limelight = new LimelightShooter();
  

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    configureBindings();

  }  

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    
    // switch limelight
    driverController.b().onTrue(limelight.estimatingDistance());
    // driverController.x().onTrue(limelight.lockToTarget(swerveDrive, limelight));
    driverController.a().onFalse(limelight.switchShooterPipeline(ShooterPipelineID.AMPS));

    swerveDrive.setDefaultCommand(swerveDrive.driveCommand( 
      () -> -driverController.getLeftY(), 
      () -> -driverController.getLeftX(),
      () -> -driverController.getRightX()));

    shooter.setDefaultCommand(shooter.shooterPID(driverController::getLeftTriggerAxis));

    driverController.rightBumper().onTrue(limelight.faceTargetWhileDriving(swerveDrive,
       () -> -driverController.getLeftY(),
       () -> -driverController.getLeftX(),
       () -> -driverController.getRightX()));

    //driverController.rightBumper().onTrue(limelight.lockToTarget(swerveDrive, limelight));

    

    // driverController.y().onTrue(new InstantCommand(() -> swerveDrive.resetHeading(), swerveDrive));

    // driverController.a().onTrue(new InstantCommand(
    //   () -> swerveDrive.swerveDriveOdometry.resetPosition(
    //     swerveDrive.getIMU().getRotation2d(), 
    //     swerveDrive.getCurrentModulePositions(), 
    //     new Pose2d(0, 0, new Rotation2d())), swerveDrive));

    // driverController.b()
    // .whileTrue(new InstantCommand(() -> swerveDrive.setSpeedLimit(0.1), swerveDrive))
    // .whileFalse(new InstantCommand(() -> swerveDrive.setSpeedLimit(1), swerveDrive));

    // operatorController.a()
    // .onTrue(Commands.startEnd(() -> swerveDrive.setModVelocity(0.25), () -> swerveDrive.setModVelocity(0), swerveDrive));

    
    // operatorController.x()
    // .onTrue(Commands.startEnd(() -> swerveDrive.setModVelocity(-0.25), () -> swerveDrive.setModVelocity(0), swerveDrive));

    // operatorController.b()
    // .onTrue(Commands.startEnd(() -> swerveDrive.setModVelocity(0), () -> swerveDrive.setModVelocity(0), swerveDrive));

  }
}
