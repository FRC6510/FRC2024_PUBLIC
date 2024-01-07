// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.Command;

public class SpinThrice extends Command {
  private final Intake intake;
  private final double desiredRotations;
  /** Creates a new SpinIntake. */
  public SpinThrice(Intake intake, double desiredRotations) {
    this.intake = intake;
    this.desiredRotations = desiredRotations;
    addRequirements(intake);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    intake.intakeMotor.setPosition(0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intake.spinThriceThing(desiredRotations);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.intakeMotor.set(-0.1);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return intake.getSensorState();
  }
}