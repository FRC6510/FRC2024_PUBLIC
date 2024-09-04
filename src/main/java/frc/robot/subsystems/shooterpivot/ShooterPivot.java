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
  private ShooterPivot() {
    this.shooterPivotMotor = new TalonFX(SHOOTER_PIVOT_CTRE_BUS, "rio");
    shooterPivotMotor.setNeutralMode(NeutralModeValue.Brake);
    configureMotor();
    shooterPivotMotor.setPosition(0);
  }

  private void configureMotor(){
    var talonFXConfigsShooterPivot = new TalonFXConfiguration();

    var slot0Configs = talonFXConfigsShooterPivot.Slot0;
    slot0Configs.kS = 0; 
    slot0Configs.kV = 0; 
    slot0Configs.kA = 0; 
    slot0Configs.kP = 0.15; 
    slot0Configs.kI = 0; 
    slot0Configs.kD = 0.0005; 

    var motionMagicConfigs = talonFXConfigsShooterPivot.MotionMagic;
    motionMagicConfigs.MotionMagicCruiseVelocity = 34.0; 
    motionMagicConfigs.MotionMagicAcceleration = 60.0;

    shooterPivotMotor.getConfigurator().apply(talonFXConfigsShooterPivot);
  }

  public static final ShooterPivot INSTANCE = new ShooterPivot();

  @Override
  public void periodic() {
    SmartDashboard.putNumber("shooter pivot position", shooterPivotMotor.getPosition().getValueAsDouble());
    SmartDashboard.putNumber("shooter pivot velocity", shooterPivotMotor.getVelocity().getValueAsDouble());
    // This method will be called once per scheduler run
  }

  public static void setShooterPivotPosition(double rotations){
    final MotionMagicVoltage setShooterPivotPosition = new MotionMagicVoltage(0);
    INSTANCE.shooterPivotMotor.setControl(setShooterPivotPosition.withPosition(rotations));
  }

  public static Command spinShooterPivot(double rotations){
    return new SpinShooterPivot(rotations);
  }
}
