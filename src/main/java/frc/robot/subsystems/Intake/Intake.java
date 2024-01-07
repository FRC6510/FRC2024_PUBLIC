// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.feeder.SpinFeeder;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

public class Intake extends SubsystemBase {
  private double interruptEncoder;
  private static final int INTAKE_CTRE_BUS = 17;
  public TalonFX intakeMotor;
  private static final int NOTE_DETECTOR_DIO_CHANNEL = 9;
  public static DigitalInput noteDetector;
  private double desiredRotations;
  /** Creates a new Intake. */
  public Intake() {
    this.intakeMotor = new TalonFX(INTAKE_CTRE_BUS);
    this.noteDetector = new DigitalInput(NOTE_DETECTOR_DIO_CHANNEL);

    var talonFXConfigs = new TalonFXConfiguration();
    var slot0Configs = talonFXConfigs.Slot0;
    slot0Configs.kS = 0.25; // Add 0.25 V output to overcome static friction
    slot0Configs.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
    slot0Configs.kA = 0.01; // An acceleration of 1 rps/s requires 0.01 V output
    slot0Configs.kP = 0.11; // A position error of 2.5 rotations results in 12 V output
    slot0Configs.kI = 0; // no output for integrated error
    slot0Configs.kD = 0; // A velocity error of 1 rps results in 0.1 V output
    var motionMagicConfigs = talonFXConfigs.MotionMagic;
    // motionMagicConfigs.MotionMagicCruiseVelocity = 5; // Target cruise velocity of 80 rps
    motionMagicConfigs.MotionMagicAcceleration = 10; // Target acceleration of 160 rps/s (0.5 seconds)
    motionMagicConfigs.MotionMagicJerk = 1600; // Target jerk of 1600 rps/s/s (0.1 seconds)
    intakeMotor.getConfigurator().apply(talonFXConfigs);
    intakeMotor.setPosition(0);

    }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("intake position", intakeMotor.getPosition().getValueAsDouble());
    SmartDashboard.putNumber("intake velocity", intakeMotor.getVelocity().getValueAsDouble());
    SmartDashboard.putBoolean("sensor status", getSensorState());
  }

  public Command spinIntake(double power){
    return new SpinIntake(this, power);
  }

  public Command spinThrice(double desiredRotations){
    return new SpinThrice(this, desiredRotations);
  }

  public void spinThriceThing(double desiredRotations){
    final MotionMagicVelocityVoltage spinThriceThing = new MotionMagicVelocityVoltage(0);
    intakeMotor.setControl(spinThriceThing.withVelocity(desiredRotations));
  }

  public boolean getSensorState() {
    return !noteDetector.get();
  }

  public boolean getIntakeStatus() {
    if(intakeMotor.getPosition().getValueAsDouble() > (desiredRotations - 5) && intakeMotor.getPosition().getValueAsDouble() < (desiredRotations + 5)) {
      return true;
    }
    else {
      return false;
    }
  }

}