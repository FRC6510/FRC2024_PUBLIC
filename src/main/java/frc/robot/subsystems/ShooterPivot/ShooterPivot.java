// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.ShooterPivot;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.MotionMagicDutyCycle;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.StaticBrake;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.CANSparkFlex;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterPivot extends SubsystemBase {
  private static final int SHOOTER_PIVOT_CTRE_BUS = 14;
  private final MotionMagicDutyCycle pivotMotionMagicDutyCycle;
  private final PositionDutyCycle pivotPositionDutyCycle;

  public TalonFX shooterPivot;

  //encoder resolution = 2048
  //5:1 ratio, 5:1 ratio, 10:123 ration
  //1 degree
  private double pivotAngle = (5*5*2048*12.3)/360;

  private final Supplier<Double> positionSupplier;
  /** Creates a new ShooterPivot. */
  public ShooterPivot() {
    this.shooterPivot = new TalonFX(SHOOTER_PIVOT_CTRE_BUS, "rio");
    shooterPivot.setNeutralMode(NeutralModeValue.Coast);
    positionSupplier = shooterPivot.getPosition().asSupplier();
    shooterPivot.setInverted(true);

    CurrentLimitsConfigs currentlimits = new CurrentLimitsConfigs();
    currentlimits.StatorCurrentLimitEnable = true;
    currentlimits.StatorCurrentLimit = 10;

    Slot0Configs slot0ConfigsPivot = new Slot0Configs();
    slot0ConfigsPivot.kP = 0.15;
    slot0ConfigsPivot.kI = 0;
    slot0ConfigsPivot.kD = 0.0005;

    MotionMagicConfigs motionMagicConfigsPivot = new MotionMagicConfigs();
    motionMagicConfigsPivot.MotionMagicCruiseVelocity = 34.0;
    motionMagicConfigsPivot.MotionMagicAcceleration = 60.0;

    shooterPivot.getConfigurator().apply(slot0ConfigsPivot);
    shooterPivot.getConfigurator().apply(motionMagicConfigsPivot);

    shooterPivot.getConfigurator().apply(currentlimits, 0.50);
    pivotMotionMagicDutyCycle = new MotionMagicDutyCycle(0.0);
    pivotPositionDutyCycle = new PositionDutyCycle(0.0);
    shooterPivot.setPosition(0);
    //21.49
  }

  public double getPosition(){
    return positionSupplier.get();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("shooter pivot position", positionSupplier.get());
    SmartDashboard.putNumber("shoot pivot angle", positionSupplier.get()/pivotAngle);

    SmartDashboard.putNumber("supply current", shooterPivot.getSupplyCurrent().getValueAsDouble());
    SmartDashboard.putNumber("stator current", shooterPivot.getStatorCurrent().getValueAsDouble());

  }

  // public Command moveShooterPivot(DoubleSupplier powerSupplier){
  //   return new MoveShooterPivot(this, powerSupplier);
  // }

  public void setPivotPosition(double rotations){
    pivotMotionMagicDutyCycle.Position = rotations;
    // pivotPositionDutyCycle.Position = rotations;
    shooterPivot.setControl(pivotMotionMagicDutyCycle); 
  }

  public void setIdle(NeutralModeValue mode){
    shooterPivot.setNeutralMode(mode);
  }

  public Command shooterPivotPID(ShooterPivot shooterPivot, DoubleSupplier setpointSupplier){
    return new ShooterPivotPID(shooterPivot, setpointSupplier);
  }

  public TalonFX getMotor() {
    return shooterPivot;
  }
}