// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.drivebase;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

public class Drive extends Command {
  /** Creates a new Drive. */
  private final DoubleSupplier xSupplier;
  private final DoubleSupplier ySupplier;
  private final DoubleSupplier thetaSupplier;
  
  public Drive(DoubleSupplier xSupplier, DoubleSupplier ySupplier, DoubleSupplier thetaSupplier) {
    this.xSupplier = xSupplier;
    this.ySupplier = ySupplier;
    this.thetaSupplier = thetaSupplier;
    addRequirements(Swerve.INSTANCE);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Swerve.drive(-Swerve.deadzone(xSupplier.getAsDouble(), 0.1) * Swerve.getSpeedLimit(), 
                -Swerve.deadzone(ySupplier.getAsDouble(), 0.1) * Swerve.getSpeedLimit(), 
                -Swerve.deadzone(thetaSupplier.getAsDouble(), 0.1) * Swerve.getSpeedLimit());
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
