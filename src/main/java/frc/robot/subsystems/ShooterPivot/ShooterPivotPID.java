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
  private double maxSetpoint = Double.POSITIVE_INFINITY;
  private double minSetpoint = Double.NEGATIVE_INFINITY;
  private double speed;

  private static final double 
      KP = 0.00001,
      KI = 0,
      KD = 0;
  private final PIDController pid = new PIDController(KP, KI, KD);

  /** Creates a new ShooterPivotPID. */
  public ShooterPivotPID(ShooterPivot shooterPivot, DoubleSupplier setpointSupplier) {
    this.shooterPivot = shooterPivot;
    this.setpointSupplier = setpointSupplier;
    pid.setSetpoint(setpointSupplier.getAsDouble());    
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooterPivot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double setpoint = setpointSupplier.getAsDouble();
    if (setpoint > maxSetpoint) setpoint = maxSetpoint;
    if (setpoint < minSetpoint) setpoint = minSetpoint;
    
    speed = pid.calculate(shooterPivot.getDegrees(), setpoint);
    shooterPivot.getMotor().set(speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooterPivot.getMotor().set(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
