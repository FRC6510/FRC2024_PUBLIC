// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Shooter;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.subsystems.Drivetrain.SwerveDrive;
import frc.robot.subsystems.Feeder.Feeder;
import frc.robot.subsystems.Shooter.SpinShooter;
import frc.robot.subsystems.ShooterFeeder.ShooterFeeder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    private static final int TOP_SHOOTER_CAN_BUS = 5;
    private static final int BOTTOM_SHOOTER_CAN_BUS = 6;
    public static final double SHOOTER_AUTO_POWER = 0.8;

    public CANSparkFlex top;
    public CANSparkFlex bottom;

    public RelativeEncoder encoderTop;
    public RelativeEncoder encoderBottom;

    private SparkPIDController pidControllerTop;
    private SparkPIDController pidControllerBottom;
    public double kP, kI, kD, kIz, kF, kMaxOutput, kMinOutput;
    public static double maxRPM;
    public SpinFeedShooter spinFeedShooter;

  /** Creates a new Shooter. */
  public Shooter() {
    top = new CANSparkFlex(TOP_SHOOTER_CAN_BUS, MotorType.kBrushless);
    bottom = new CANSparkFlex(BOTTOM_SHOOTER_CAN_BUS, MotorType.kBrushless);

    top.setIdleMode(IdleMode.kCoast);
    bottom.setIdleMode(IdleMode.kCoast);

    encoderTop = top.getEncoder();
    encoderBottom = bottom.getEncoder();
    
    this.pidControllerTop = top.getPIDController();
    this.pidControllerBottom = bottom.getPIDController();

    //define values

    kP = 0;//.0009; 
    kI = 0;
    kD = 0;//.000001; 
    kIz = 0; 
    kF = 0.00025;
    kMaxOutput = 1; 
    kMinOutput = -0.9;
    maxRPM = 6220;

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
    SmartDashboard.putNumber("Shooter top velocity", encoderTop.getVelocity());
    SmartDashboard.putNumber("Shooter bottom velocity", encoderBottom.getVelocity());
 
  }

  public Command shooterPID (DoubleSupplier PIDSetPointSource) {
    return new ShooterPID (this, PIDSetPointSource);
  }

  public Command spinShooter (DoubleSupplier powerSupplier) {
    return new SpinShooter(this, powerSupplier);
  }

  public Command shooterVelocity(Shooter shooter, SwerveDrive swerveDrive){
    return new ShooterVelocity(this, swerveDrive);
  }

  public SparkPIDController getPidControllerTop() {
    return pidControllerTop;
  }

  public SparkPIDController getPidControllerBottom() {
    return pidControllerBottom;
  }

  public double getTopVelocity(){
    return encoderTop.getVelocity();
    
  }
  public double getBottomVelocity(){
    return encoderBottom.getVelocity();
    
  }

  public void stop(){
    top.stopMotor();
    bottom.stopMotor();
  }

  // public boolean shooterAtSpeed(double rpm){
  //   return this.getTopVelocity() >= 5850 && this.getBottomVelocity() >= 5850;
  // }

  public boolean shooterAtSpeed(double rpm){
    return this.getTopVelocity() >= (rpm-50) && this.getBottomVelocity() >= (rpm-50);
    // return this.getTopVelocity() >= 5850 && this.getBottomVelocity() >= 5850;
  }

  public SpinFeedShooterFixedVelocity spinFeedShootFixed(Shooter shooter, ShooterFeeder shooterFeeder, Feeder feeder, SwerveDrive swerveDrive, DoubleSupplier shooterSupplier, DoubleSupplier sFeederSupplier, DoubleSupplier feederSupplier){
    BooleanSupplier inTeleop = () -> ShooterFeeder.readyToShoot;
    return new SpinFeedShooterFixedVelocity(shooter, shooterFeeder, feeder, swerveDrive, shooterSupplier, sFeederSupplier, feederSupplier, inTeleop);
  }

}
