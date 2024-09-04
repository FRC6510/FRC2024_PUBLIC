// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.feeder;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase {
    private static final int FEEDER_CTRE_BUS = 19;
    private final TalonFX feederMotor;
    private double desiredVelocity;

  /** Creates a new Feeder. */
  private Feeder() {
    feederMotor = new TalonFX(FEEDER_CTRE_BUS);
    feederMotor.setNeutralMode(NeutralModeValue.Brake);
    configureMotor();
    feederMotor.setPosition(0);
  }

  private void configureMotor(){
    var talonFXConfigsFeeder = new TalonFXConfiguration();

    var slot0Configs = talonFXConfigsFeeder.Slot0;
    slot0Configs.kS = 0.25; 
    slot0Configs.kV = 0.12; 
    slot0Configs.kA = 0.01; 
    slot0Configs.kP = 0.11; 
    slot0Configs.kI = 0; 
    slot0Configs.kD = 0; 

    var motionMagicConfigs = talonFXConfigsFeeder.MotionMagic;
    motionMagicConfigs.MotionMagicCruiseVelocity = 5; 
    motionMagicConfigs.MotionMagicAcceleration = 10;
    motionMagicConfigs.MotionMagicJerk = 1600; 

    feederMotor.getConfigurator().apply(talonFXConfigsFeeder);
  }

  public static final Feeder INSTANCE = new Feeder();

  @Override
  public void periodic() {
    SmartDashboard.putNumber("feeder position", feederMotor.getPosition().getValueAsDouble());
    SmartDashboard.putNumber("feeder velocity", feederMotor.getVelocity().getValueAsDouble());
    // This method will be called once per scheduler run
  }

  public static Command spinFeeder(double power){
    return new SpinFeeder(power);
  }

  //Method to set the motor power
  public void setMotorPower(double power) {
    feederMotor.set(power);
  }

  //Method to stop the motor
  public void stopMotor() {
    feederMotor.stopMotor();
  }

  //Command that spins the intake consistently at a certain velocity
  public static Command spinFeederVelocity(double desiredVelocity){
    INSTANCE.desiredVelocity = desiredVelocity;
    return new SpinFeederVelocity(desiredVelocity);
  }

  //Method used to control motor speed, aiming to achieve a consistent rotational velocity
  public static void spinFeederVelocityControl(double desiredVelocity){
    final MotionMagicVelocityVoltage spinFeederVelocityControl = new MotionMagicVelocityVoltage(0);
    INSTANCE.feederMotor.setControl(spinFeederVelocityControl.withVelocity(desiredVelocity));
  }
}
