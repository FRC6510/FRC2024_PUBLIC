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

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  // Replace with CommandPS4Controller or CommandJoystick if needed
  /**
   * in port 0
   */

   SwerveDrive swerveDrive = new SwerveDrive();

  private final CommandXboxController driverController =
      new CommandXboxController(0);
    
  private final CommandXboxController operatorController =
      new CommandXboxController(1);


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
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
    
    // configure the bindings

    driverController.leftBumper().onTrue(swerveDrive.turnTo(
      new Rotation2d(0)));

   driverController.rightBumper().onTrue(swerveDrive.turnTo(
      new Rotation2d(Math.PI)));

    // driverController.leftBumper().onTrue(swerveDrive.moveTo(
    //   0, 
    //   0));

   driverController.pov(0).onTrue(swerveDrive.moveTo(
      1,
      0));

   driverController.pov(90).onTrue(swerveDrive.moveTo(
      0,
      -1));

   driverController.pov(180).onTrue(swerveDrive.moveTo(
      -1,
      0));

    driverController.pov(270).onTrue(swerveDrive.moveTo(
      0,
      1));

    swerveDrive.setDefaultCommand(swerveDrive.driveCommand( 
      () -> -driverController.getLeftY(),
      () -> -driverController.getLeftX(),
      () -> -driverController.getRightX()));

    driverController.a().onTrue(new InstantCommand(
    () -> swerveDrive.swerveDriveOdometry.resetPosition(
      swerveDrive.getIMU().getRotation2d(), 
      swerveDrive.getCurrentModulePositions(), 
      new Pose2d(0, 0, new Rotation2d())), swerveDrive));

    driverController.y().onTrue(new InstantCommand(() -> swerveDrive.resetHeading(), swerveDrive));

    driverController.b()
    .whileTrue(new InstantCommand(() -> swerveDrive.setSpeedLimit(0.1), swerveDrive))
    .whileFalse(new InstantCommand(() -> swerveDrive.setSpeedLimit(1), swerveDrive));


  }
}
