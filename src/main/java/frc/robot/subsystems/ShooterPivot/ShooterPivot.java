// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.ShooterPivot;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import com.ctre.phoenix6.controls.StaticBrake;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterPivot extends SubsystemBase {
  private static final int SHOOTER_PIVOT_CTRE_BUS = 14;
  private final TalonFX shooterPivot;

  //encoder resolution = 2048
  //5:1 ratio, 5:1 ratio, 10:123 ration
  //1 degree
  private double pivotAngle = (5*5*2048*12.3)/360;

  private final Supplier<Double> positionSupplier;
  /** Creates a new ShooterPivot. */
  public ShooterPivot() {
    this.shooterPivot = new TalonFX(SHOOTER_PIVOT_CTRE_BUS);
    positionSupplier = shooterPivot.getPosition().asSupplier();
    shooterPivot.setNeutralMode(NeutralModeValue.Brake);
    shooterPivot.setPosition(24*pivotAngle);
  }

  public double getDegrees(){
    return positionSupplier.get();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("shooter pivot position", positionSupplier.get());
    SmartDashboard.putNumber("shoot pivot angle", positionSupplier.get()/pivotAngle);
  }

  public Command moveShooterPivot(DoubleSupplier powerSupplier){
    return new MoveShooterPivot(this, powerSupplier);
  }

  public Command shooterPivotPID(ShooterPivot shooterPivot, DoubleSupplier setpointSupplier){
    return new ShooterPivotPID(shooterPivot, setpointSupplier);
  }

  public TalonFX getMotor() {
    return shooterPivot;
  }
}