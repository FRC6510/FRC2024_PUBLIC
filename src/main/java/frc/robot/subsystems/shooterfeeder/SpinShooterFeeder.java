// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooterfeeder;

import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;

public class SpinShooterFeeder extends Command {
  private final ShooterFeeder shooterFeeder;
  private final double power;
  /** Creates a new SpinShooterFeeder. */
  public SpinShooterFeeder(ShooterFeeder shooterFeeder, double power) {
    this.shooterFeeder = shooterFeeder;
    this.power = power;
    addRequirements(shooterFeeder);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
   shooterFeeder.shooterFeeder.set(power);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooterFeeder.shooterFeeder.set(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
