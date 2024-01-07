// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Feeder;

import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterFeeder.ShooterFeeder;

class SpinFeederNoNoteLocks extends Command {
  private final Feeder feeder;
  private final DoubleSupplier rps;
  /** Creates a new SpinFeeder. */
  public SpinFeederNoNoteLocks(Feeder feeder, DoubleSupplier rps) {
    this.feeder = feeder;
    this.rps = rps;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(feeder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
      feeder.setFeederVelocity(rps.getAsDouble());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("feeder finished");
      feeder.getFeeder().set(0);
      // feeder.setFeederPosition(feeder.getFeederPosition());
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
    //gets run every loop
    //return feeder.getSensorState();
  }
}
