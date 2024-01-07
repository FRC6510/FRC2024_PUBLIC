// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Shooter;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

public class MoveShooter extends Command {
  private final Shooter shooter;
  private final DoubleSupplier powerSupplier;

  /** Creates a new MoveShooter. */
  public MoveShooter(Shooter shooter, DoubleSupplier powerSupplier) {
    this.shooter = shooter;
    this.powerSupplier = powerSupplier;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooter.left.set(powerSupplier.getAsDouble());
    shooter.right.set(powerSupplier.getAsDouble());
    shooter.lower.set(powerSupplier.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.left.set(0);
    shooter.right.set(0);
    shooter.lower.set(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
