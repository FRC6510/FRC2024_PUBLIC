// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Intake;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
  private static final int INTAKE_CTRE_BUS = 17;
  private final VelocityDutyCycle intakeVelocityRequest;

  public TalonFX intakeMotor;
  /** Creates a new Intake. */
  public Intake() {
    Slot0Configs slot0ConfigsIntake = new Slot0Configs();
    slot0ConfigsIntake.kV = 0.0100;//TODO test for velocity
    slot0ConfigsIntake.kP = 0.02465;
    slot0ConfigsIntake.kI = 0;
    slot0ConfigsIntake.kD = 0;
    this.intakeMotor = new TalonFX(INTAKE_CTRE_BUS, "rio");
    intakeMotor.getConfigurator().apply(slot0ConfigsIntake);
    intakeVelocityRequest = new VelocityDutyCycle(0.0);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Intake velocity", intakeMotor.getVelocity().getValueAsDouble());

  }
  
  public Command spinIntake(DoubleSupplier rps){
    return new SpinIntake(this, rps);
  }

  public void setIntakeVelocity(DoubleSupplier rps){//TODO test for velocity
    intakeVelocityRequest.Velocity = rps.getAsDouble();
    intakeMotor.setControl(intakeVelocityRequest); 
  }
}
