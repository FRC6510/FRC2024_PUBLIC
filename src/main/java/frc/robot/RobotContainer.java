// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.Shooter.Shooter;
import frc.robot.subsystems.ShooterFeeder.ShooterFeeder;
import frc.robot.subsystems.ShooterPivot.ShooterPivot;
import frc.robot.commands.Autos;
import frc.robot.subsystems.Feeder.Feeder;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  //can split line initialising but only in methods, one line works anywhere
  //private final Shooter trial = new SHooter();
  private final Shooter shooter = new Shooter();
  private final ShooterFeeder shooterFeeder = new ShooterFeeder();
  private final Feeder feeder = new Feeder();
  private final ShooterPivot shooterPivot = new ShooterPivot();

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController driverController = new CommandXboxController(0);
  private final CommandXboxController operatorController = new CommandXboxController(1);

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

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
    //dot calling method to call on object
    //methods belong to objects
    //have object acces things in object, numbers cannot be, acces things inside class folder
    //if all declared shooter use same spinshooter method access which shooter by dotting shooter before using method
     //driverController.x().onTrue(trial.spinShooter(()-> 0.2));
    operatorController.rightBumper().onTrue(shooter.spinShooter(()-> 1)).onFalse(shooter.spinShooter(()->0));
    operatorController.leftBumper().onTrue(shooterFeeder.spinShooterFeeder(()->1)).onFalse(shooterFeeder.spinShooterFeeder(()->0));
    //operatorController.x().whileTrue(feeder.spinFeeder(()-> 0.1));
    //operatorController.y().whileTrue(feeder.spinFeeder(()-> -0.1));
    driverController.x().whileTrue(feeder.spinFeeder(()-> 0.2)).onFalse(feeder.spinFeeder(()->0));
    driverController.y().whileTrue(feeder.spinFeeder(()-> -0.2)).onFalse(feeder.spinFeeder(()->0));

    operatorController.a().onTrue(shooterPivot.moveShooterPivot(()-> 0.1)).onFalse(shooterPivot.moveShooterPivot(()->0));
    operatorController.b().onTrue(shooterPivot.moveShooterPivot(()-> -0.1)).onFalse(shooterPivot.moveShooterPivot(()->0));
    //operatorController.a().whileTrue(shooterPivot.shooterPivotPID(shooterPivot, () -> 0.01));
    //operatorController.b().whileTrue(shooterPivot.shooterPivotPID(shooterPivot, () -> -0.01));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return Commands.waitSeconds(1);
    // An example command will be run in autonomous
    //return Autos.exampleAuto(m_exampleSubsystem);
  }
}