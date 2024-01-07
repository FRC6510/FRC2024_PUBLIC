// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class Shooter extends SubsystemBase {
  private static final int TOP_SHOOTER_CAN_BUS = 5;
  private static final int BOTTOM_SHOOTER_CAN_BUS = 6;

  public CANSparkFlex top;
  public CANSparkFlex bottom;

  private SparkPIDController pidControllerTop;
  private SparkPIDController pidControllerBottom;

  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;
  public static double maxRPM;

  /** Creates a new Shooter. */
  public Shooter() {
    top = new CANSparkFlex(TOP_SHOOTER_CAN_BUS, MotorType.kBrushless);
    bottom = new CANSparkFlex(BOTTOM_SHOOTER_CAN_BUS, MotorType.kBrushless);
    
    top.setIdleMode(IdleMode.kCoast);
    bottom.setIdleMode(IdleMode.kCoast);

    this.pidControllerTop = top.getPIDController();
    this.pidControllerBottom = bottom.getPIDController();

    //try tuning the pid
    // start with all values 0, but increase p by 0.001
    // once oscillating, decrease p and sometimes d
    // print position first to find max
    //next goal, set motor to 80% of max velocity

    // print velocity to smartdashboard
    // use a percentage without pid use 100% DO NOT USE PID
    // USE REV CLIENT USB CABLE
    // look at dashboard to see velocity with a fully charged battery
    // potentially may use a variable

    kP = 0.008;
    kI = 0;
    kD = 0;
    kIz = 0;
    kFF = 0;
    //from the max and min output, test using smartdashboard to find the kp, and this, kd
    kMaxOutput = 1;
    kMinOutput = -0.9;
    maxRPM = 6220;

    pidControllerTop.setP(kP);
    pidControllerTop.setI(kI);
    pidControllerTop.setD(kD);
    pidControllerTop.setIZone(kIz);
    pidControllerTop.setFF(kFF);
    pidControllerTop.setOutputRange(kMinOutput, kMaxOutput);

    pidControllerBottom.setP(kP);
    pidControllerBottom.setI(kI);
    pidControllerBottom.setD(kD);
    pidControllerBottom.setIZone(kIz);
    pidControllerBottom.setFF(kFF);
    pidControllerBottom.setOutputRange(kMinOutput, kMaxOutput);

    //should print the curve of p to see whether in/decrease

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
    SmartDashboard.putNumber("Shooter Top Velocity", top.getEncoder().getVelocity());
    SmartDashboard.putNumber("Shottor Bottom Velocity", bottom.getEncoder().getVelocity());
    // This method will be called once per scheduler run
  }

  public Command spinShooter(Shooter shooter, double powerSupplier){
    return new SpinShooter(this, powerSupplier);
  }

  public Command shooterPid(Shooter shooter, DoubleSupplier PIDSetPointSource){
    return new ShooterPID(this, PIDSetPointSource);
  }
}
