// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooterfeeder;

import edu.wpi.first.wpilibj2.command.Command;

public class SpinShooterFeeder extends Command {
  private double power;
  /** Creates a new SpinShooter. */
  public SpinShooterFeeder(double power) {
    this.power = power;
    addRequirements(ShooterFeeder.INSTANCE);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    ShooterFeeder.INSTANCE.shooterFeeder.set(power);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
        ShooterFeeder.INSTANCE.shooterFeeder.set(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
