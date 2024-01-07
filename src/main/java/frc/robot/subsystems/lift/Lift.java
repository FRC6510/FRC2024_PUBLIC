// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.lift;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Lift extends SubsystemBase {
  private static final int LIFT_CTRE_BUS = 15;
  public final TalonFX liftMotor;

  private final Supplier<Double> positionSupplier;
  /** Creates a new Lift. */
  public Lift() {
    this.liftMotor = new TalonFX(LIFT_CTRE_BUS);
    positionSupplier = liftMotor.getPosition().asSupplier();
    liftMotor.setNeutralMode(NeutralModeValue.Brake);
  }

  public double getDegrees(){
    return positionSupplier.get();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("lift postion", positionSupplier.get());
    // This method will be called once per scheduler run
  }

  public Command moveLift(DoubleSupplier powerSupplier){
    return new MoveLift(this, powerSupplier);
  }

  public TalonFX getMotor(){
    return liftMotor;
  }
}
