// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Sensors.LimitSwitch;
import frc.robot.subsystems.Sensors.TouchSensor;
import frc.robot.subsystems.blinkin.Blinkin;
import frc.robot.subsystems.blinkin.Blinkin.Colours;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
// subsystems
  public final Blinkin blinkin = new Blinkin();
  public final LimitSwitch limitSwitch = new LimitSwitch();
  public final Limelight limelight = new Limelight();

// controllers
  public final CommandXboxController driverController = new CommandXboxController(0);
  

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
    driverController.a().onTrue(blinkin.setColour(Blinkin.Colours.ORANGE));
    driverController.b().onTrue(blinkin.setColour(Blinkin.Colours.GREEN));
    driverController.x().onTrue(blinkin.setBlinking(false));
    driverController.y().onTrue(blinkin.setBlinking(true));
    driverController.leftBumper().onTrue(blinkin.setColour(Blinkin.Colours.OFF));

    limitSwitch.getTrigger().onTrue(blinkin.set(Blinkin.Colours.ORANGE, false));
    limitSwitch.getTrigger().onFalse(blinkin.set(Blinkin.Colours.GREEN, false));

    limelight.getTriggerTargetDetected().onTrue(blinkin.set(Colours.ORANGE, false));

  }
}