// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.lift;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Feeder.Feeder;

import java.util.function.DoubleSupplier;

public class TrapAutomation extends Command {
  private final Lift liftMotor;
  private final DoubleSupplier liftSupplier;
  private final Feeder feeder;
  private final DoubleSupplier feederSupplier;
  private double maxSetpoint = 90;
  private double minSetpoint = 0;
  /** Creates a new TrapAutomation. */
  public TrapAutomation(Lift liftMotor, DoubleSupplier liftSupplier, Feeder feeder, DoubleSupplier feederSupplier) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.liftMotor = liftMotor;
    this.liftSupplier = liftSupplier;
    this.feeder = feeder;
    this.feederSupplier = feederSupplier;
    addRequirements(liftMotor);
    addRequirements(feeder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double setpoint = liftSupplier.getAsDouble();
    if (setpoint > maxSetpoint) setpoint = maxSetpoint;
    if (setpoint < minSetpoint) setpoint = minSetpoint;

    liftMotor.setLiftPosition(setpoint);
    if (Lift.doingTrap){
      if (Feeder.feederDetected){
        feeder.setFeederVelocity(-0.15);
      } else {
        feeder.setFeederVelocity(-0.2);
      } 
    }
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
