// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooter;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

import com.revrobotics.CANSparkMax;
/**
 * Spins the bottom motor when the top motor reaches full velocity(ish)
 */
public class SpinIf extends Command {
  private final Shooter shooter;
  private final DoubleSupplier PIDSetPointSource;
  /** Creates a new SpinIf. */
  public SpinIf(Shooter shooter, DoubleSupplier PIDSetPointSource) {
    this.shooter = shooter;
    this.PIDSetPointSource = PIDSetPointSource;
    addRequirements(shooter);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double setPoint = PIDSetPointSource.getAsDouble() * Shooter.maxRPM;
    shooter.getPidControllerTop().setReference(setPoint, CANSparkMax.ControlType.kVelocity);
    if (Math.abs(shooter.top.getEncoder().getVelocity()) > Math.abs(setPoint) * 0.9) {
      shooter.getPidControllerBottom().setReference(setPoint,CANSparkMax.ControlType.kVelocity);
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
