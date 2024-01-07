// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.ShooterFeeder;

import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Feeder.Feeder;

public class SpinShooterFeeder extends Command {
  private final ShooterFeeder shooterFeeder;
  private final DoubleSupplier powerSupplier;
  private Feeder feeder;
  private final CommandXboxController operatorController;

  /** Creates a new SpinShooterFeeder. */
  public SpinShooterFeeder(ShooterFeeder shooterFeeder, DoubleSupplier powerSupplier) {
    this.shooterFeeder = shooterFeeder;
    this.powerSupplier = powerSupplier;
    this.operatorController = new CommandXboxController(1);
    // Use addRequirements() here to declare subsystem dependencies.

    addRequirements(shooterFeeder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooterFeeder.shooterFeeder.set(powerSupplier.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooterFeeder.shooterFeeder.set(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return ShooterFeeder.shooterDetected && !ShooterFeeder.readyToShoot;// && !ShooterFeeder.readyToShoot;
  }
}
