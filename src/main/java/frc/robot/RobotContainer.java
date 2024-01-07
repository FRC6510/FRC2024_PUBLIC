// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

// import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import frc.robot.commands.Autonomous.Auto;
import frc.robot.commands.Autonomous.Auto.AutoList;
import frc.robot.subsystems.Drivetrain.SwerveDrive;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController driverController =
    new CommandXboxController(0);

  private final CommandXboxController operatorController =
    new CommandXboxController(1);

  public SwerveDrive swerveDrive = new SwerveDrive();

  public Auto auto = new Auto(this);
  SendableChooser<Command> chooser = new SendableChooser<>();

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
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    new Trigger(m_exampleSubsystem::exampleCondition)
        .onTrue(new ExampleCommand(m_exampleSubsystem));

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
    driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());

    chooser.addOption("Figure 8s ", auto.getAuto(AutoList.FIGURE_EIGHTS));
    chooser.addOption("Test turning", auto.getAuto(AutoList.TEST_TURNING));
    chooser.addOption("Blue full", auto.getAuto(AutoList.BLUE_FULL));// Max 32 points
    chooser.addOption("Blue first 3", auto.getAuto(AutoList.BLUE_FIRST_THREE));// Max 32 points
    SmartDashboard.putData(chooser);

    // driverController.a().whileTrue(Commands.startEnd(
    //     () -> swerveDrive.setPower(1), 
    //     () -> swerveDrive.setPower(1), 
    //     swerveDrive)
    //   ).whileFalse(Commands.startEnd(
    //     () -> swerveDrive.setPower(0), 
    //     () -> swerveDrive.setPower(0), 
    //     swerveDrive));

    swerveDrive.setDefaultCommand(swerveDrive.driveCommand(
      () -> -driverController.getLeftY(), 
      () -> -driverController.getLeftX(),  
      () -> -driverController.getRightX()));

    driverController.y().onTrue(new InstantCommand(() -> swerveDrive.resetHeading(), swerveDrive));

    driverController.a().onTrue(new InstantCommand(
        () -> swerveDrive.swerveDriveOdometry.resetPosition(
          swerveDrive.getIMU().getRotation2d(), 
          swerveDrive.getCurrentModulePositions(), 
          new Pose2d(0, 0, new Rotation2d())), swerveDrive));

    driverController.b()
    .whileTrue(new InstantCommand(() -> swerveDrive.setSpeedLimit(0.1), swerveDrive))
    .whileFalse(new InstantCommand(() -> swerveDrive.setSpeedLimit(1), swerveDrive));

    driverController.rightBumper().onTrue(swerveDrive.turnTo(Rotation2d.fromDegrees(90)));
    driverController.leftBumper().onTrue(swerveDrive.turnTo(Rotation2d.fromDegrees(-90)));

    // driverController.pov(0).onTrue(swerveDrive.moveTo(
    //   1, 
    //   0));

    // driverController.pov(90).onTrue(swerveDrive.moveTo(
    //   0, 
    //   -1));

    // driverController.pov(180).onTrue(swerveDrive.moveTo(
    //   -1, 
    //   0));

    // driverController.pov(270).onTrue(swerveDrive.moveTo(
    //   0, 
    //   1));

    // driverController.rightBumper().onTrue(Commands.startEnd(
    //   () -> swerveDrive.setModVelocity(4), 
    //   () -> swerveDrive.setModVelocity(4), 
    //   swerveDrive)
    // ).onFalse(Commands.startEnd(
    //   () -> swerveDrive.setModVelocity(0), 
    //   () -> swerveDrive.setModVelocity(0),
    //    swerveDrive));

    // driverController.leftBumper().onTrue(Commands.startEnd(
    //   () -> swerveDrive.setModAngle(90), 
    //   () -> swerveDrive.setModAngle(90), 
    //   swerveDrive)
    // ).onFalse(Commands.startEnd(
    //   () -> swerveDrive.setModAngle(0), 
    //   () -> swerveDrive.setModAngle(0), 
    //     swerveDrive));
    
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
     return chooser.getSelected();
  }
}
