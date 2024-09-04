// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.drivebase;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;

public class LockToHeading extends Command {

  private final PIDController headingController;
  private final DoubleSupplier xSupplier;
  private final DoubleSupplier ySupplier;

  private Rotation2d targetHeading;

  /** Creates a new LockToHeading. */
  public LockToHeading(DoubleSupplier xSupplier, DoubleSupplier ySupplier) {
    this.xSupplier = xSupplier;
    this.ySupplier = ySupplier;

    headingController = new PIDController(0.000025, 0.0, 0.000000000001);
    headingController.enableContinuousInput(-180, 180);

    addRequirements(Swerve.INSTANCE);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    targetHeading = Swerve.getPose().getRotation();
    headingController.setSetpoint(targetHeading.getDegrees());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double currentHeading = Swerve.getPose().getRotation().getDegrees();
    double correction = headingController.calculate(currentHeading);
    Swerve.drive(-Swerve.deadzone(xSupplier.getAsDouble(), 0.1),
                 -Swerve.deadzone(ySupplier.getAsDouble(), 0.1), correction);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
