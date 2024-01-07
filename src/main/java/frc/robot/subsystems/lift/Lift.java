// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.lift;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Lift extends SubsystemBase {
  private static final int LIFT_CTRE_BUS = 15;
  public final TalonFX liftMotor;
  
  public Lift() {
    this.liftMotor = new TalonFX(LIFT_CTRE_BUS, "rio");
    liftMotor.getInverted();
    liftMotor.setNeutralMode(NeutralModeValue.Brake);

    var talonFXConfiguration = new TalonFXConfiguration();
    var slot0Configs = talonFXConfiguration.Slot0;
    slot0Configs.kS = 0;
    slot0Configs.kV = 0;
    slot0Configs.kA = 0;
    slot0Configs.kP = 6.4;
    slot0Configs.kI = 0;
    slot0Configs.kD = 0.32;
    
    var motionMagicConfigs = talonFXConfiguration.MotionMagic;
    motionMagicConfigs.MotionMagicCruiseVelocity = 110.0;
    motionMagicConfigs.MotionMagicAcceleration = 190.0;
    //motionMagicConfigs.MotionMagicJerk = 1600;
    
    liftMotor.getConfigurator().apply(talonFXConfiguration);
    liftMotor.setPosition(0);
   }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Lift Position", liftMotor.getPosition().getValueAsDouble());
    SmartDashboard.putNumber("Lift Velocity", liftMotor.getVelocity().getValueAsDouble());
  }

  public void setLiftPosition(double rotations){
    final MotionMagicVoltage setLiftPosition = new MotionMagicVoltage(0);
    liftMotor.setControl(setLiftPosition.withPosition(rotations));
  }

  public Command liftPID(Lift lift, double rotations){
    return new LiftPID(this, rotations);
  }

  public TalonFX getMotor(){
    return liftMotor;
  }
}
