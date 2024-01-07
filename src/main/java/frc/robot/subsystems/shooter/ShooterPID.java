// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooter;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class ShooterPID extends Command {

  private final Shooter shooter;
  private DoubleSupplier PIDSetPointSource;
  
  /** Creates a new ShooterPID. */
  public ShooterPID(Shooter shooter, DoubleSupplier PIDSetPointSource) {
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

    double setPoint = PIDSetPointSource.getAsDouble()*Shooter.maxRPM;
    shooter.getPidControllerTop().setReference(setPoint, CANSparkMax.ControlType.kVelocity);
    shooter.getPidControllerBottom().setReference(setPoint, CANSparkMax.ControlType.kVelocity);
    SmartDashboard.putNumber("SetPoint", setPoint);
    
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
