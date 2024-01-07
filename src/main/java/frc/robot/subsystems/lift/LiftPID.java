// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.lift;
import edu.wpi.first.wpilibj2.command.Command;

public class LiftPID extends Command {
  private final Lift lift;
  private double rotations;
  // private double maxRotations = -90;
  // private double minRotations = 0;

  /** Creates a new LiftPID. */
  public LiftPID(Lift lift, double rotations) {
    this.lift = lift;
    this.rotations = rotations;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(lift);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // if (rotations > maxRotations) rotations = maxRotations;
    // if (rotations < minRotations) rotations = minRotations;
    lift.setLiftPosition(rotations);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    lift.liftMotor.set(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}