// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Shooter;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    private static final int TOP_SHOOTER_CAN_BUS = 5;
    private static final int BOTTOM_SHOOTER_CAN_BUS = 6;

    public CANSparkFlex top;
    public CANSparkFlex bottom;

    private SparkPIDController pidControllerTop;
    private SparkPIDController pidControllerBottom;
    public double kP, kI, kD, kIz, kF, kMaxOutput, kMinOutput;
    public static double maxRPM;


  /** Creates a new Shooter. */
  public Shooter() {
    top = new CANSparkFlex(TOP_SHOOTER_CAN_BUS, MotorType.kBrushless);
    bottom = new CANSparkFlex(BOTTOM_SHOOTER_CAN_BUS, MotorType.kBrushless);

    this.pidControllerTop = top.getPIDController();
    this.pidControllerBottom = bottom.getPIDController();

    //define values

    kP = 0.00015; 
    kI = 0;
    kD = 0.000001; 
    kIz = 0; 
    kF = 0.00015; 
    kMaxOutput = 1; 
    kMinOutput = -0.9;
    maxRPM = 6784;

    pidControllerTop.setP(kP);
    pidControllerTop.setI(kI);
    pidControllerTop.setD(kD);
    pidControllerTop.setIZone(kIz);
    pidControllerTop.setFF(kF);
    pidControllerTop.setOutputRange(kMinOutput, kMaxOutput);

    pidControllerBottom.setP(kP);
    pidControllerBottom.setI(kI);
    pidControllerBottom.setD(kD);
    pidControllerBottom.setIZone(kIz);
    pidControllerBottom.setFF(kF);
    pidControllerBottom.setOutputRange(kMinOutput, kMaxOutput);

    SmartDashboard.putNumber("P Gain", kP);
    SmartDashboard.putNumber("I Gain", kI);
    SmartDashboard.putNumber("D Gain", kD);
    SmartDashboard.putNumber("I Zone", kIz);
    SmartDashboard.putNumber("Feed Forward", kF);
    SmartDashboard.putNumber("Max Output", kMaxOutput);
    SmartDashboard.putNumber("Min Output", kMinOutput);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public Command shooterPID (DoubleSupplier PIDSetPointSource) {
    return new ShooterPID (this, PIDSetPointSource);
  }

  public Command spinShooter (DoubleSupplier powerSupplier) {
    return new SpinShooter(this, powerSupplier);
  }

  public SparkPIDController getPidControllerTop() {
    return pidControllerTop;
  }

  public SparkPIDController getPidControllerBottom() {
    return pidControllerBottom;
  }
}
