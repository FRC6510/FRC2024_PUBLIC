// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Intake;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.FeedbackConfigs;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Feeder.Feeder;
import frc.robot.subsystems.ShooterFeeder.ShooterFeeder;

public class SpinIntake extends Command {
  private final Intake intake;
  private final DoubleSupplier rps;

  /** Creates a new SpinIntake. */
  public SpinIntake(Intake intake, DoubleSupplier rps) {
    this.intake = intake;
    this.rps = rps;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intake.setIntakeVelocity(rps);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.intakeMotor.set(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Feeder.feederDetected && !Feeder.wantToShoot;
  }
}
