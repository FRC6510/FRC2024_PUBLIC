// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.ShooterPivot;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;

public class ShooterPivotPID extends Command {
  private final ShooterPivot shooterPivot;
  private DoubleSupplier setpointSupplier;
  private double maxSetpoint = 19.50;
  private double minSetpoint = 0;

  /** Creates a new ShooterPivotPID. */
  public ShooterPivotPID(ShooterPivot shooterPivot, DoubleSupplier setpointSupplier) {
    this.shooterPivot = shooterPivot;
    this.setpointSupplier = setpointSupplier;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooterPivot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double setpoint = setpointSupplier.getAsDouble();
    if (setpoint > maxSetpoint) setpoint = maxSetpoint;
    if (setpoint < minSetpoint) setpoint = minSetpoint;
    shooterPivot.setPivotPosition(setpoint);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooterPivot.setPivotPosition(setpointSupplier.getAsDouble());
    // shooterPivot.getMotor().set(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return shooterPivot.getMotor().getPosition().getValueAsDouble() >= (setpointSupplier.getAsDouble() - 0.15);
  }
}
