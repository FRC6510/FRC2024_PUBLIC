// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.ShooterFeeder;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.MotionMagicDutyCycle;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class ShooterFeeder extends SubsystemBase {
  private static final int SHOOTER_DETECTOR_DIO_CHANNEL = 6;
  private final DigitalInput shooterDetector;
  private static final int SHOOTER_FEEDER_CAN_BUS = 7;
  public CANSparkFlex shooterFeeder;
  private final CommandXboxController operatorController;
  public static boolean shooterDetected;
  public static boolean readyToShoot;
  private SparkPIDController pidControllerSFeeder;
  public double kP, kI, kD, kIz, kF, kMaxOutput, kMinOutput;
  public static double maxRPM;

  public boolean previousDetected;
  public boolean noted;
  /** Creates a new ShooterFeeder. */
  public ShooterFeeder() {
    this.operatorController = new CommandXboxController(1);
    this.shooterDetector = new DigitalInput(SHOOTER_DETECTOR_DIO_CHANNEL);
    shooterFeeder = new CANSparkFlex(SHOOTER_FEEDER_CAN_BUS, MotorType.kBrushless);
    shooterFeeder.setIdleMode(IdleMode.kBrake);
    pidControllerSFeeder = shooterFeeder.getPIDController();
    noted = false;

    kP = 0;//.0009; 
    kI = 0;
    kD = 0;//.000001; 
    kIz = 0; 
    kF = 0.00025;
    kMaxOutput = 1; 
    kMinOutput = -0.9;
    maxRPM = 1000;//measure max 

    pidControllerSFeeder.setP(kP);
    pidControllerSFeeder.setI(kI);
    pidControllerSFeeder.setD(kD);
    pidControllerSFeeder.setIZone(kIz);
    pidControllerSFeeder.setFF(kF);
    pidControllerSFeeder.setOutputRange(kMinOutput, kMaxOutput);



    // public void setDriveModuleVelocity(double rps){
    //   driveVelocityRequest.Velocity = rps;
    //   driveMotor.setControl(driveVelocityRequest); 
    // }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("shooter detector", getSensorState());
    if (operatorController.getLeftTriggerAxis() > 0.1){
      readyToShoot = true;
    } else {
      readyToShoot = false;
    }
    /**
     * the sensor returns false when it detects something
     */
    if (!shooterDetector.get()){
      shooterDetected = true;
    } else {
      shooterDetected = false;
    }

    if (previousDetected && !shooterDetected){
      noted = true;// note in
    } else {
      noted = false;
    }

    previousDetected = shooterDetected;
    //System.out.println(noted);
  }

  public boolean getSensorState() {
    return shooterDetector.get();
  }

  public void stop(){
    shooterFeeder.stopMotor();
  }

  public SparkPIDController getSFeederPIDController(){
    return pidControllerSFeeder;
  }

  public Command spinShooterFeeder(DoubleSupplier powerSupplier) {
    return new SpinShooterFeeder(this, powerSupplier);
  }
}