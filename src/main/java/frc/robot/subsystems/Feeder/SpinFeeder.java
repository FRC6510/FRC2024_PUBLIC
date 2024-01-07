// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Feeder;

import edu.wpi.first.wpilibj2.command.Command;

public class SpinFeeder extends Command {
  private final Feeder feeder;
  private final double desiredRotationsFeeder;
  /** Creates a new SpinFeeder. */
  public SpinFeeder(Feeder feeder, double desiredRotationsFeeder) {
    this.feeder = feeder;
    this.desiredRotationsFeeder = desiredRotationsFeeder;
    addRequirements(feeder);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    feeder.feederMotor.setPosition(0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    feeder.spinFeederThing(desiredRotationsFeeder);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    feeder.feederMotor.set(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return feeder.getSensorState();
  }
}