// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain.SwerveDrive;

public class ShooterVelocity extends Command {
  private final Shooter shooter;
  private final SwerveDrive swerveDrive;
  private double maxVelocity = 1;
  private double minVelocity = 0.5;
  /** Creates a new ShooterVelocity. */
  public ShooterVelocity(Shooter shooter, SwerveDrive swerveDrive) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.shooter = shooter;
    this.swerveDrive = swerveDrive;
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double distanceFromSpeaker = swerveDrive.getPose2d().getX() + 8;
    double shooterSetpoint = Math.abs((-1/(Math.pow(Math.E, distanceFromSpeaker))+1));

    if (shooterSetpoint > maxVelocity) shooterSetpoint = maxVelocity;
    if (shooterSetpoint < minVelocity) shooterSetpoint = minVelocity;
    shooter.top.set(shooterSetpoint);
    shooter.bottom.set(shooterSetpoint);
    
    SmartDashboard.putNumber("distance from speaker", distanceFromSpeaker);
    SmartDashboard.putNumber("velocity", shooterSetpoint);
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
