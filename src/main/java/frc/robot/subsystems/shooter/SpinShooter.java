// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooter;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

public class SpinShooter extends Command {
  private final Shooter shooter;
  private final DoubleSupplier powerSupplier;

  SpinShooter(Shooter shooter, DoubleSupplier powerSupplier) {
    this.shooter = shooter;
    this.powerSupplier = powerSupplier;
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooter.top.set(powerSupplier.getAsDouble());
    shooter.bottom.set(powerSupplier.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if(!interrupted) {
      shooter.top.set(0);
      shooter.bottom.set(0);
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
