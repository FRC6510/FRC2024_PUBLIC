// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Intake;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Feeder.Feeder;
import frc.robot.subsystems.lift.Lift;

public class SpinIntake extends Command {
  private final Intake intake;
  private final Feeder feeder;
  private final Lift lift;
  private final DoubleSupplier rps;

  /** Creates a new SpinIntake. */
  public SpinIntake(Intake intake, Feeder feeder, Lift lift, DoubleSupplier rps) {
    this.intake = intake;
    this.feeder = feeder;
    this.lift = lift;
    this.rps = rps;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    //Intake.intaken = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //TODO WHAT IS THIS!!!!! I am leaving it here so nothing breaks but why!!!!!!
    //THIS IS SO MISLEADING
    intake.setIntakeVelocity(() -> 40);

    /*if (feeder.getContainsNote() && !lift.doingTrap){
      intake.setIntakeVelocity(() -> -40);
    } else {
      intake.setIntakeVelocity(rps);
    }*/

   // Intake.intaken = false;

    // if (Feeder.intaken){
    //   intakeCounter++;
    // }

    // System.out.println("the intake counter is " + intakeCounter);

    // System.out.println(intakeCounter);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("intake finished");
    intake.intakeMotor.set(0);
    //Intake.intaken = true;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (feeder.getContainsNote() && !Feeder.wantToShoot);
  }
}
