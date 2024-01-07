// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.ShooterFeeder;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.revrobotics.CANSparkFlex;
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

  public boolean previousDetected;
  public boolean noted;
  /** Creates a new ShooterFeeder. */
  public ShooterFeeder() {
    this.operatorController = new CommandXboxController(1);
    this.shooterDetector = new DigitalInput(SHOOTER_DETECTOR_DIO_CHANNEL);
    shooterFeeder = new CANSparkFlex(SHOOTER_FEEDER_CAN_BUS, MotorType.kBrushless);
    noted = false;

    // Slot0Configs slot0Configs = new Slot0Configs();
    // slot0Configs.kV = 0.0100;
    // slot0Configs.kP = 0.02465;
    // slot0Configs.kI = 0;
    // slot0Configs.kD = 0;
    // driveMotor.getConfigurator().apply(slot0Configs, 0.050);

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

  public Command spinShooterFeeder(DoubleSupplier powerSupplier) {
    return new SpinShooterFeeder(this, powerSupplier);
  }
}