// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Intake;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
  private static final int INTAKE_CTRE_BUS = 17;
  public TalonFX intakeMotor;
  /** Creates a new Intake. */
  public Intake() {
    this.intakeMotor = new TalonFX(INTAKE_CTRE_BUS, "CANivore");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  
  public Command spinIntake(double power){
    return new SpinIntake(this, power);
  }
}
