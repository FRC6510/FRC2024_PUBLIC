// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.lift;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.MotionMagicDutyCycle;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Feeder.Feeder;
import frc.robot.subsystems.ShooterPivot.ShooterPivot;
import frc.robot.subsystems.ShooterPivot.ShooterPivotPID;

public class Lift extends SubsystemBase {
  private static final int LIFT_CTRE_BUS = 15;
  public final TalonFX liftMotor;
  public static boolean doingTrap;

  private final Supplier<Double> positionSupplier;
  private final PositionDutyCycle liftPositionDutyCycle;
  private final MotionMagicDutyCycle liftMotionMagicDutyCycle;

  /** Creates a new Lift. */
  public Lift() {
    CurrentLimitsConfigs currentlimits = new CurrentLimitsConfigs();
    currentlimits.StatorCurrentLimitEnable = false;
    currentlimits.StatorCurrentLimit = 10;//TODO test for values 
    
    this.liftMotor = new TalonFX(LIFT_CTRE_BUS, "rio");
    positionSupplier = liftMotor.getPosition().asSupplier();

    liftMotor.setInverted(true);
    liftMotor.setNeutralMode(NeutralModeValue.Brake);

    Slot0Configs slot0ConfigsLift = new Slot0Configs();
    slot0ConfigsLift.kP = 0.25;
    slot0ConfigsLift.kI = 0;
    slot0ConfigsLift.kD = 0.001;

    MotionMagicConfigs motionMagicConfigsLift = new MotionMagicConfigs();
    motionMagicConfigsLift.MotionMagicCruiseVelocity = 110.0;
    motionMagicConfigsLift.MotionMagicAcceleration = 190.0;

    liftMotor.getConfigurator().apply(slot0ConfigsLift);
    liftMotor.getConfigurator().apply(motionMagicConfigsLift);
    liftMotor.getConfigurator().apply(currentlimits);

    liftPositionDutyCycle = new PositionDutyCycle(0.0);
    liftMotionMagicDutyCycle = new MotionMagicDutyCycle(0.0);

    doingTrap = false;

  }

  public double getDegrees(){
    return positionSupplier.get();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("lift postion", positionSupplier.get());
    SmartDashboard.putNumber("lift velocity", liftMotor.getVelocity().getValueAsDouble());
    // This method will be called once per scheduler run

    if (liftMotor.getPosition().getValueAsDouble() > 75){
      doingTrap = true;
    } else {
      doingTrap = false;
    }
  }

  // public Command moveLift(DoubleSupplier powerSupplier){
  //   return new MoveLift(this, powerSupplier);
  // }

  public void setLiftPosition(double rotations){
    liftMotionMagicDutyCycle.Position = rotations;
    liftMotor.setControl(liftMotionMagicDutyCycle); 
  }

  public Command liftPID(Lift lift, DoubleSupplier setpointSupplier){
    return new LiftPID(lift, setpointSupplier);
  }

  public Command TrapAutomation(Lift lift, DoubleSupplier liftSupplier, Feeder feeder, DoubleSupplier feederSupplier){
    return new TrapAutomation(lift, liftSupplier, feeder, feederSupplier);
  }

  public TalonFX getMotor(){
    return liftMotor;
  }
}
