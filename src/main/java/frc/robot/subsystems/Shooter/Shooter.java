// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Shooter;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  private static final int LEFT_SHOOTER_CAN_BUS = 1;
  private static final int RIGHT_SHOOTER_CAN_BUS = 2;
  private static final int LOWER_SHOOTER_CAN_BUS = 3;

  public CANSparkFlex left;
  public CANSparkFlex right;
  public CANSparkFlex lower; 

  private SparkPIDController pidControllerLeft;
  private SparkPIDController pidControllerRight;
  private SparkPIDController pidControllerLower;

  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;
  public static double maxRPM;
  
  /** Creates a new Shooter. */
  public Shooter() {
    left = new CANSparkFlex(LEFT_SHOOTER_CAN_BUS, MotorType.kBrushless);
    right = new CANSparkFlex(RIGHT_SHOOTER_CAN_BUS, MotorType.kBrushless);
    lower = new CANSparkFlex(LOWER_SHOOTER_CAN_BUS, MotorType.kBrushless);

    this.pidControllerLeft = left.getPIDController();
    this.pidControllerRight = right.getPIDController();
    this.pidControllerLower = lower.getPIDController();

    kP = 0.00015; 
    kI = 0;
    kD = 0.000001; 
    kIz = 0; 
    kFF = 0.00015; 
    kMaxOutput = 1; 
    kMinOutput = -1;
    maxRPM = 2500;
    
    pidControllerLeft.setP(kP);
    pidControllerLeft.setI(kI);
    pidControllerLeft.setD(kD);
    pidControllerLeft.setIZone(kIz);
    pidControllerLeft.setFF(kFF);
    pidControllerLeft.setOutputRange(kMinOutput, kMaxOutput);
    pidControllerLeft.setOutputRange(-0.6, 0.6);

    pidControllerRight.setP(kP);
    pidControllerRight.setI(kI);
    pidControllerRight.setD(kD);
    pidControllerRight.setIZone(kIz);
    pidControllerRight.setFF(kFF);
    pidControllerRight.setOutputRange(kMinOutput, kMaxOutput);
    pidControllerRight.setOutputRange(-0.6, 0.6);

    pidControllerLower.setP(kP);
    pidControllerLower.setI(kI);
    pidControllerLower.setD(kD);
    pidControllerLower.setIZone(kIz);
    pidControllerLower.setFF(kFF);
    pidControllerLower.setOutputRange(kMinOutput, kMaxOutput);
    pidControllerLower.setOutputRange(-0.6, 0.6);

    SmartDashboard.putNumber("P Gain", kP);
    SmartDashboard.putNumber("I Gain", kI);
    SmartDashboard.putNumber("D Gain", kD);
    SmartDashboard.putNumber("I Zone", kIz);
    SmartDashboard.putNumber("Feed Forward", kFF);
    SmartDashboard.putNumber("Max Output", kMaxOutput);
    SmartDashboard.putNumber("Min Output", kMinOutput);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    double p = SmartDashboard.getNumber("P Gain", 0);
    double i = SmartDashboard.getNumber("I Gain", 0);
    double d = SmartDashboard.getNumber("D Gain", 0);
    double iz = SmartDashboard.getNumber("I Zone", 0);
    double ff = SmartDashboard.getNumber("Feed Forward", 0);
    double max = SmartDashboard.getNumber("Max Output", 0);
    double min = SmartDashboard.getNumber("Min Output", 0);
    SmartDashboard.putNumber("Left Velocity", left.getEncoder().getVelocity());
    SmartDashboard.putNumber("Right Velocity", right.getEncoder().getVelocity());
    SmartDashboard.putNumber("Lower Velocity", lower.getEncoder().getVelocity());

    if((p != kP)) { pidControllerLeft.setP(p); kP = p; }
    if((i != kI)) { pidControllerLeft.setI(i); kI = i; }
    if((d != kD)) { pidControllerLeft.setD(d); kD = d; }
    if((iz != kIz)) { pidControllerLeft.setIZone(iz); kIz = iz; }
    if((ff != kFF)) { pidControllerLeft.setFF(ff); kFF = ff; }
    if((max != kMaxOutput) || (min != kMinOutput)) { 
      pidControllerLeft.setOutputRange(min, max); 
      kMinOutput = min; kMaxOutput = max; 
    }

    if((p != kP)) { pidControllerRight.setP(p); kP = p; }
    if((i != kI)) { pidControllerRight.setI(i); kI = i; }
    if((d != kD)) { pidControllerRight.setD(d); kD = d; }
    if((iz != kIz)) { pidControllerRight.setIZone(iz); kIz = iz; }
    if((ff != kFF)) { pidControllerRight.setFF(ff); kFF = ff; }
    if((max != kMaxOutput) || (min != kMinOutput)) { 
      pidControllerRight.setOutputRange(min, max); 
      kMinOutput = min; kMaxOutput = max; 
    }

    if((p != kP)) { pidControllerLower.setP(p); kP = p; }
    if((i != kI)) { pidControllerLower.setI(i); kI = i; }
    if((d != kD)) { pidControllerLower.setD(d); kD = d; }
    if((iz != kIz)) { pidControllerLower.setIZone(iz); kIz = iz; }
    if((ff != kFF)) { pidControllerLower.setFF(ff); kFF = ff; }
    if((max != kMaxOutput) || (min != kMinOutput)) { 
      pidControllerRight.setOutputRange(min, max); 
      kMinOutput = min; kMaxOutput = max; 
    }
  }

  public SparkPIDController getPidControllerLeft() {
    return pidControllerLeft;
  }

  public SparkPIDController getPidControllerRight() {
    return pidControllerRight;
  }

  public SparkPIDController getPidControllerLower() {
    return pidControllerLower;
  }

  public Command moveShooter (DoubleSupplier powerSupplier) {
    return new MoveShooter (this, powerSupplier);
  }

  public Command shooterPID (DoubleSupplier PIDSetPointSource) {
    return new ShooterPID (this, PIDSetPointSource);
  }

}
