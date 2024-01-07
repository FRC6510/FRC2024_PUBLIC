// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooterpivot;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterPivot extends SubsystemBase {
  private static final int SHOOTER_PIVOT_CTRE_BUS = 14;
  public final TalonFX shooterPivotMotor;
  /** Creates a new ShooterPivot. */
  public ShooterPivot() {
    this.shooterPivotMotor = new TalonFX(SHOOTER_PIVOT_CTRE_BUS, "rio");
    shooterPivotMotor.setNeutralMode(NeutralModeValue.Brake);

    var talonFXConfiguration = new TalonFXConfiguration();
    var slot0Configs = talonFXConfiguration.Slot0;
    slot0Configs.kS = 0;
    slot0Configs.kV = 0;
    slot0Configs.kA = 0;
    slot0Configs.kP = 0;
    slot0Configs.kI = 0;
    slot0Configs.kD = 0;

    var motionMagicConfigs = talonFXConfiguration.MotionMagic;
    motionMagicConfigs.MotionMagicCruiseVelocity = 10;
    motionMagicConfigs.MotionMagicAcceleration = 20;
    
    shooterPivotMotor.getConfigurator().apply(talonFXConfiguration);
    shooterPivotMotor.setPosition(0);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Shooter Pivot Position", shooterPivotMotor.getPosition().getValueAsDouble());
    SmartDashboard.putNumber("Shooter Pivot Velocity", shooterPivotMotor.getVelocity().getValueAsDouble());
    // This method will be called once per scheduler run
  }

  public void setShooterPivotPosition(double rotations){
    final MotionMagicVoltage setShooterPivotPosition = new MotionMagicVoltage(0);
    shooterPivotMotor.setControl(setShooterPivotPosition.withPosition(rotations));
  }

  public Command shooterPivotPID(ShooterPivot shooterPivot, double rotations){
    return new ShooterPivotPID(this, rotations);
  }

  public TalonFX getMotor(){
    return shooterPivotMotor;
  }
}