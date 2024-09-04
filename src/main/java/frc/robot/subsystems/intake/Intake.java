// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
  private static final int INTAKE_CTRE_BUS = 17;
  private static final int NOTE_DETECTOR_DIO_CHANNEL = 9;

  private final TalonFX intakeMotor;
  public final DigitalInput noteDetector;

  private double desiredVelocity;

  /** Creates a new Intake. */
  private Intake() {
    intakeMotor = new TalonFX(INTAKE_CTRE_BUS);
    noteDetector = new DigitalInput(NOTE_DETECTOR_DIO_CHANNEL);
    configureMotor();
    intakeMotor.setPosition(0);
  }

  private void configureMotor(){
    var talonFXConfigsIntake = new TalonFXConfiguration();
    
    var slot0Configs = talonFXConfigsIntake.Slot0;
    slot0Configs.kS = 0.25;
    slot0Configs.kV = 0.12;
    slot0Configs.kA = 0.01;
    slot0Configs.kP = 0.11;
    slot0Configs.kI = 0;
    slot0Configs.kD = 0;

    var motionMagicConfigs = talonFXConfigsIntake.MotionMagic;
    motionMagicConfigs.MotionMagicAcceleration = 100;
    motionMagicConfigs.MotionMagicJerk = 1600;

    intakeMotor.getConfigurator().apply(talonFXConfigsIntake);
  }

  public static final Intake INSTANCE = new Intake();

  @Override
  public void periodic() {
    SmartDashboard.putNumber("intake position", intakeMotor.getPosition().getValueAsDouble());
    SmartDashboard.putNumber("intake velocity", intakeMotor.getVelocity().getValueAsDouble());
    SmartDashboard.putBoolean("intake sensor status", getSensorState());
    // This method will be called once per scheduler run
  }

  //Command that spins the intake until sensor stops it
  public static Command spinIntake(double power){
    return new SpinIntake(power);
  }

  //Method to set the motor power
  public void setMotorPower(double power) {
    intakeMotor.set(power);
  }

  //Method to stop the motor
  public void stopMotor() {
    intakeMotor.stopMotor();
  }

  //Command that spins the intake consistently at a certain velocity
  public static Command spinIntakeVelocity(double desiredVelocity){
    INSTANCE.desiredVelocity = desiredVelocity;
    return new SpinIntakeVelocity(desiredVelocity);
  }

  //Method used to control motor speed, aiming to achieve a consistent rotational velocity
  public static void spinIntakeVelocityControl(double desiredVelocity){
    final MotionMagicVelocityVoltage spinIntakeVelocityControl = new MotionMagicVelocityVoltage(0);
    INSTANCE.intakeMotor.setControl(spinIntakeVelocityControl.withVelocity(desiredVelocity));
  }

  public boolean getSensorState() {
    return !INSTANCE.noteDetector.get();
  }
}
