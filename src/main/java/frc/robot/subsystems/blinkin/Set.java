// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.blinkin;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.blinkin.Blinkin.Colours;

public class Set extends Command {
  private final Blinkin blinkin;
  private boolean blinking;
  private Colours colour;

  /** Creates a new Set. */
  Set(Blinkin blinkin, Colours colour, boolean blinking) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(blinkin);
    this.colour = colour;
    this.blinking = blinking;
    this.blinkin = blinkin;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    blinkin.setColourInternal(colour);
    blinkin.setBlinkingInternal(blinking);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}