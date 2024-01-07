// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intakefeeder;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;

public class Intake extends SubsystemBase {
  /** Creates a new Intake. */
    private static final int INTAKE_CTRE_BUS = 17;
    private final TalonFX intake;

  public Intake(){
    this.intake = new TalonFX(INTAKE_CTRE_BUS);
    intake.setInverted(true);
  }

  @Override
  public void periodic() {
  }

  public Command spinIntake(DoubleSupplier powerSupplier) {
    return new SpinIntake(this, powerSupplier);
  }

  public TalonFX getIntake(){
    return intake;
  }

   public void stopSpinning() {
   if (Feeder.noteDetector.get()==false){
    intake.set(0);
    }
  } 

  public boolean noteDetected() {
    return Feeder.noteDetector.get();
  }
}