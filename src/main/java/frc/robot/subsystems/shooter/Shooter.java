// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooter;

import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.SparkPIDController;


public class Shooter extends SubsystemBase {
  private static final int TOP_SHOOTER_CAN_BUS = 1;
  private static final int BOTTOM_SHOOTER_CAN_BUS = 2;
    
  public CANSparkFlex top;
  public CANSparkFlex bottom; 

  private SparkPIDController pidControllerTop;
  private SparkPIDController pidControllerBottom;
  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;
  public static double maxRPM;
  //TODO make constants final and format correctly

  public Shooter() {
    top = new CANSparkFlex(TOP_SHOOTER_CAN_BUS, MotorType.kBrushless);
    bottom = new CANSparkFlex(BOTTOM_SHOOTER_CAN_BUS, MotorType.kBrushless);

    this.pidControllerTop = top.getPIDController();
    this.pidControllerBottom = bottom.getPIDController();

    kP = 0.00015; 
    kI = 0;
    kD = 0.000001; 
    kIz = 0; 
    kFF = 0.00015; 
    kMaxOutput = 1; 
    kMinOutput = -1;
    maxRPM = 6784;

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

    double p = SmartDashboard.getNumber("P Gain", 0);
    double i = SmartDashboard.getNumber("I Gain", 0);
    double d = SmartDashboard.getNumber("D Gain", 0);
    double iz = SmartDashboard.getNumber("I Zone", 0);
    double ff = SmartDashboard.getNumber("Feed Forward", 0);
    double max = SmartDashboard.getNumber("Max Output", 0);
    double min = SmartDashboard.getNumber("Min Output", 0);
    SmartDashboard.putNumber("velocityTop", top.getEncoder().getVelocity());
    SmartDashboard.putNumber("velocityBottom", bottom.getEncoder().getVelocity());
    
    if((p != kP)) { pidControllerTop.setP(p); kP = p; }
    if((i != kI)) { pidControllerTop.setI(i); kI = i; }
    if((d != kD)) { pidControllerTop.setD(d); kD = d; }
    if((iz != kIz)) { pidControllerTop.setIZone(iz); kIz = iz; }
    if((ff != kFF)) { pidControllerTop.setFF(ff); kFF = ff; }
    if((max != kMaxOutput) || (min != kMinOutput)) { 
      pidControllerTop.setOutputRange(min, max); 
      kMinOutput = min; kMaxOutput = max; 
    }

    if((p != kP)) { pidControllerBottom.setP(p); kP = p; }
    if((i != kI)) { pidControllerBottom.setI(i); kI = i; }
    if((d != kD)) { pidControllerBottom.setD(d); kD = d; }
    if((iz != kIz)) { pidControllerBottom.setIZone(iz); kIz = iz; }
    if((ff != kFF)) { pidControllerBottom.setFF(ff); kFF = ff; }
    if((max != kMaxOutput) || (min != kMinOutput)) { 
      pidControllerBottom.setOutputRange(min, max); 
      kMinOutput = min; kMaxOutput = max; 
    }
    
  }

  public Command spinIf (DoubleSupplier PIDSetPointSource) {
    return new SpinIf (this, PIDSetPointSource);
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