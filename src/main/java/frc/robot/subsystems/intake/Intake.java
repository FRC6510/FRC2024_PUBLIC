// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Intake;

import java.util.function.DoubleSupplier;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Intake extends SubsystemBase {
  private static final int INTAKE_MOTOR_CAN_BUS = 5;
  public CANSparkMax intakeMotor;
  /** Creates a new IntakePrototype. */
  public Intake() {
     intakeMotor = new CANSparkMax(INTAKE_MOTOR_CAN_BUS, MotorType.kBrushless);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public Command moveIntake (double power) {
    return new MoveIntake (this, power);
  }
  
}