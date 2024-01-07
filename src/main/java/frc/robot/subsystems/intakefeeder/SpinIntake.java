// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intakefeeder;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

class SpinIntake extends Command {
  private final Intake intake;
  private final DoubleSupplier powerSupplier;
  /** Creates a new SpinFeeder. */
  public SpinIntake(Intake intake, DoubleSupplier powerSupplier) {
    this.intake = intake;
    this.powerSupplier = powerSupplier;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
   intake.getIntake().set(powerSupplier.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.stopSpinning();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return intake.noteDetected()==false;
  }
}