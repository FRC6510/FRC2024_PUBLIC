// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.feeder;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.intake.Intake;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class Feeder extends SubsystemBase {
  private double interruptEncoder;
  private static final int FEEDER_CTRE_BUS = 19;
  public TalonFX feederMotor;
  private double desiredRotationsFeeder;
  private double desiredVelocity;
  /** Creates a new Feeder. */
  public Feeder() {
    this.feederMotor = new TalonFX(FEEDER_CTRE_BUS);
    feederMotor.setNeutralMode(NeutralModeValue.Brake);

    var talonFXConfigs1 = new TalonFXConfiguration();
    var slot0Configs = talonFXConfigs1.Slot0;
    slot0Configs.kS = 0.25; // Add 0.25 V output to overcome static friction
    slot0Configs.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
    slot0Configs.kA = 0.01; // An acceleration of 1 rps/s requires 0.01 V output
    slot0Configs.kP = 0.11; // A position error of 2.5 rotations results in 12 V output
    slot0Configs.kI = 0; // no output for integrated error
    slot0Configs.kD = 0; // A velocity error of 1 rps results in 0.1 V output
    var motionMagicConfigs = talonFXConfigs1.MotionMagic;
    motionMagicConfigs.MotionMagicCruiseVelocity = 5; // Target cruise velocity of 80 rps
    motionMagicConfigs.MotionMagicAcceleration = 10; // Target acceleration of 160 rps/s (0.5 seconds)
    motionMagicConfigs.MotionMagicJerk = 1600; // Target jerk of 1600 rps/s/s (0.1 seconds)
    feederMotor.getConfigurator().apply(talonFXConfigs1);
    feederMotor.setPosition(0);

    }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("feeder position", feederMotor.getPosition().getValueAsDouble());
    SmartDashboard.putNumber("feeder velocity", feederMotor.getVelocity().getValueAsDouble());
  }

  public Command spinFeeder(double desiredRotationsFeeder){
    return new SpinFeeder(this, desiredRotationsFeeder);
  }

  public Command spinFeederAutomatic(double desiredRotationsFeeder){
    return new SpinFeederAutomatic(this, desiredRotationsFeeder);
  }


  public void spinFeederThing(double desiredRotationsFeeder){
    final MotionMagicVelocityVoltage spinFeederThing = new MotionMagicVelocityVoltage(0);
    feederMotor.setControl(spinFeederThing.withVelocity(desiredRotationsFeeder));
  }

   public void spinFeederPosition(double desiredRotationsFeeder){
    final MotionMagicVoltage spinFeederThing = new MotionMagicVoltage(0);
    feederMotor.setControl(spinFeederThing.withPosition(desiredRotationsFeeder));
  }

  public boolean getFeederStatus() {
    if(feederMotor.getPosition().getValueAsDouble() > (desiredRotationsFeeder - 5) && feederMotor.getPosition().getValueAsDouble() < (desiredRotationsFeeder + 5)) {
      return true;
    }
    else {
      return false;
    }
  }

  public Command simpleSpinFeeder(double power){
    return new SimpleSpinFeeder(this, power);
  }

  public boolean getSensorState() {
    return !Intake.noteDetector.get();
  }
}