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
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import frc.robot.subsystems.Drivetrain.SwerveDrive;
import frc.robot.subsystems.lift.Lift;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterPivot extends SubsystemBase {
  private static final int SHOOTER_PIVOT_CTRE_BUS = 14;
  private final MotionMagicDutyCycle pivotMotionMagicDutyCycle;

  public TalonFX shooterPivot;
  //25.46 rotations from top to bottom.
  //encoder resolution = 2048
  //5:1 ratio, 5:1 ratio, 10:123 ratio
  //1 degree
  private double rotationsToDegrees = (5 * 5 * 12.3) / 360.0;
  private double maxSetpoint = 25;
  private double minSetpoint = 1;
  private final Supplier<Double> positionSupplier;
  private Lift lift;
  /** Creates a new ShooterPivot. */
  public ShooterPivot(Lift lift) {
    this.shooterPivot = new TalonFX(SHOOTER_PIVOT_CTRE_BUS, "rio");
    shooterPivot.setNeutralMode(NeutralModeValue.Brake);
    positionSupplier = shooterPivot.getPosition().asSupplier();
    shooterPivot.setInverted(true);

    CurrentLimitsConfigs currentlimits = new CurrentLimitsConfigs();
    currentlimits.StatorCurrentLimitEnable = true;
    currentlimits.StatorCurrentLimit = 10;

    Slot0Configs slot0ConfigsPivot = new Slot0Configs();
    slot0ConfigsPivot.kP = 0.2;
    slot0ConfigsPivot.kI = 0;
    slot0ConfigsPivot.kD = 0.0005;

    MotionMagicConfigs motionMagicConfigsPivot = new MotionMagicConfigs();
    motionMagicConfigsPivot.MotionMagicCruiseVelocity = 60.0;
    motionMagicConfigsPivot.MotionMagicAcceleration = 90.0;

    shooterPivot.getConfigurator().apply(slot0ConfigsPivot);
    shooterPivot.getConfigurator().apply(motionMagicConfigsPivot);

    shooterPivot.getConfigurator().apply(currentlimits, 0.50);
    pivotMotionMagicDutyCycle = new MotionMagicDutyCycle(0.0);
    shooterPivot.setPosition(0);
    this.lift = lift;
    //21.49
  }

  public double getPosition(){
    return positionSupplier.get();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("shooter pivot position", positionSupplier.get());
    SmartDashboard.putNumber("shoot pivot angle", positionSupplier.get()*rotationsToDegrees);

    SmartDashboard.putNumber("supply current", shooterPivot.getSupplyCurrent().getValueAsDouble());
    SmartDashboard.putNumber("stator current", shooterPivot.getStatorCurrent().getValueAsDouble());

    if (lift.getMotor().getPosition().getValueAsDouble() >= 70){
      pivotMotionMagicDutyCycle.Position = 0;
      // pivotPositionDutyCycle.Position = rotations;
      shooterPivot.setControl(pivotMotionMagicDutyCycle); 
    }

  }

  // public Command moveShooterPivot(DoubleSupplier powerSupplier){
  //   return new MoveShooterPivot(this, powerSupplier);
  // }
  public void setPivotPositionDegrees(double angle){
    if (angle > maxSetpoint) angle = maxSetpoint;
    if (angle < minSetpoint) angle = minSetpoint;

    //int bottomAngle = 45; //TODO update with actual angle value
    pivotMotionMagicDutyCycle.Position = angle * rotationsToDegrees;
    // pivotPositionDutyCycle.Position = rotations;

    shooterPivot.setControl(pivotMotionMagicDutyCycle); 
    SmartDashboard.putNumber("Setpoint pivot", angle);
  }

  // Oscar: removing this to only deal with angles
  // public void setPivotPosition(double rotations){
  //   pivotMotionMagicDutyCycle.Position = rotations;
  //   // pivotPositionDutyCycle.Position = rotations;
  //   shooterPivot.setControl(pivotMotionMagicDutyCycle); 
  // }

  public void setIdle(NeutralModeValue mode){
    shooterPivot.setNeutralMode(mode);
  }

  public Command shooterPivotPID(ShooterPivot shooterPivot, DoubleSupplier setpointSupplier){
    return new ShooterPivotPID(shooterPivot, setpointSupplier);
  }

  public Command shooterPivotAutomation(SwerveDrive swerveDrive, ShooterPivot shooterPivot, double manualPos){
    return new ShooterPivotAutomation(swerveDrive, shooterPivot, manualPos);
  }

  public Command prepareShootAngle(double pivotAngle){
    return new ShooterPivotPIDEndless(this, ()->pivotAngle);
  }

  public TalonFX getMotor() {
    return shooterPivot;
  }
}