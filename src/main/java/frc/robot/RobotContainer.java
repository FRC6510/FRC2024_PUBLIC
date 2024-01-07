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
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.Intake.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.SpinShooter;
import frc.robot.subsystems.shooter.SpinIf;
import frc.robot.subsystems.Drivetrain.SwerveDrive;
import frc.robot.subsystems.blinkin.Blinkin;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // CONTROLLERS:

  // SUBSYSTEMS:
  // The robot's subsystems and commands are defined here...
  Arm arm = new Arm();
  Shooter shooter = new Shooter();
  Intake intake = new Intake();

  private final CommandXboxController driverController =
  new CommandXboxController(0);

private final CommandXboxController operatorController =
  new CommandXboxController(1);

  // COMMANDS:
// instances for controller and blinkin

  // Replace with CommandPS4Controller or CommandJoystick if needed
  /**
   * in port 0
   */

   SwerveDrive swerveDrive = new SwerveDrive();

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
    // shooter.spinShooter(driverController::getLeftTriggerAxis).schedule();

    // shooter.setDefaultCommand(shooter.shooterPID(driverController::getLeftTriggerAxis));

    // configure the bindings

    operatorController.rightBumper().onTrue(shooter.spinIf(() -> 1)).onFalse(shooter.spinIf(() -> 0));
    operatorController.leftBumper().onTrue(shooter.spinIf(() -> -0.05)).onFalse(shooter.spinIf(() -> 0));

    //operatorController.rightTrigger().onTrue(arm..onFalse(arm.moveArm(() -> 0));
    // operatorController.x().onTrue(arm.spinArmPid(14.4));
    // operatorController.y().onTrue(arm.spinArmPid(50.29));

    operatorController.a().onTrue(intake.moveIntake(0.2)).onFalse(intake.moveIntake(0));
    operatorController.b().onTrue(intake.moveIntake(-0.2)).onFalse(intake.moveIntake(0));

    // new Trigger(() -> {
    //   // TODO determine if we are in a shooting zone

    //   return false;
    // }).whileTrue(shooter.spinShooter(() -> 1));
    
    // configure the bindingss

    swerveDrive.setDefaultCommand(swerveDrive.driveCommand( 
      () -> -driverController.getLeftY(),
      () -> -driverController.getLeftX(),
      () -> -driverController.getRightX()
    ));

    driverController.rightBumper().onTrue(swerveDrive.driveAngled(
      driverController,
      () -> -driverController.getLeftY(),
      () -> -driverController.getLeftX(),
      new Rotation2d(0)
    ));
  
    driverController.leftBumper().onTrue(swerveDrive.driveAngled(
      driverController,
      () -> -driverController.getLeftY(),
      () -> -driverController.getLeftX(),
      Rotation2d.fromDegrees(180)
    ));

    driverController.a().onTrue(new InstantCommand(
    () -> swerveDrive.swerveDriveOdometry.resetPosition(
      swerveDrive.getIMU().getRotation2d(), 
      swerveDrive.getCurrentModulePositions(), 
      new Pose2d(0, 0, new Rotation2d())), swerveDrive));

    driverController.y().onTrue(new InstantCommand(() -> swerveDrive.resetHeading(), swerveDrive));

    driverController.b()
    .whileTrue(new InstantCommand(() -> swerveDrive.setSpeedLimit(0.4), swerveDrive))
    .whileFalse(new InstantCommand(() -> swerveDrive.setSpeedLimit(1), swerveDrive));
  }
}