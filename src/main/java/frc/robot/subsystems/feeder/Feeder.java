// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Shooter.ShooterLimelight;
import frc.robot.subsystems.Shooter.ShooterLimelight.LEDMode;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Shooter.Shooter;
import frc.robot.subsystems.Shooter.ShooterLED;

public class Feeder extends SubsystemBase {
  private static final int NOTE_DETECTOR_DIO_CHANNEL = 9;
  private final DigitalInput noteDetector;
  private static final int FEEDER_CTRE_BUS = 16;
  private final TalonFX feeder;
  private final CommandXboxController operatorController;
  private final VelocityDutyCycle feederVelocityRequest;
  private final PositionDutyCycle feederPositionDutyCycle;
  public static boolean feederDetected;
  public static boolean wantToShoot;
  public boolean previousDetected;
  // public static boolean intaken;

  /** Creates a new Feeder. */
  public Feeder() {
    Slot0Configs slot0ConfigsFeeder = new Slot0Configs();
    slot0ConfigsFeeder.kV = 0.0100;//TODO test for velocity
    slot0ConfigsFeeder.kP = 0.02465;
    slot0ConfigsFeeder.kI = 0;
    slot0ConfigsFeeder.kD = 0;
    this.feederVelocityRequest = new VelocityDutyCycle(0.0);
    this.feederPositionDutyCycle = new PositionDutyCycle(0.0);
    this.operatorController = new CommandXboxController(1);
    this.noteDetector = new DigitalInput(NOTE_DETECTOR_DIO_CHANNEL);
    this.feeder = new TalonFX(FEEDER_CTRE_BUS, "rio");
    feeder.getConfigurator().apply(slot0ConfigsFeeder);
    feederDetected = false;
    wantToShoot = false;
  }

  @Override
  public void periodic() {
    
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("feeder detected", feederDetected);
    SmartDashboard.putBoolean("want to shoot", wantToShoot);

    SmartDashboard.putNumber("Feeder velocity", feeder.getVelocity().getValueAsDouble());
    
    if (operatorController.getRightTriggerAxis() > 0.1 || operatorController.getLeftTriggerAxis() > 0.1 || operatorController.leftBumper().getAsBoolean() || operatorController.pov(0).getAsBoolean() || operatorController.y().getAsBoolean()){
      wantToShoot = true;
    } else {
      wantToShoot = false;
    }

    if (!noteDetector.get()){// note, do not want to shoot
      feederDetected = true;
    } else {
      feederDetected = false;
  
    }

    // if (previousDetected && !feederDetected){
    //   intaken = true;// note in
    // } else {
    //   intaken = false;
    // }

    // previousDetected = feederDetected;

  }

  public Command spinFeeder(DoubleSupplier powerSupplier) {
    return new SpinFeeder(this, powerSupplier);
  }

  public void stop(){
    feeder.stopMotor();
  }

  public boolean getSensorState() {
    return noteDetector.get();
  }

  public void setFeederVelocity(double rps){//TODO test for velocity
    feederVelocityRequest.Velocity = rps;
    feeder.setControl(feederVelocityRequest); 
  }

  public void setFeederPosition(double position){
    feederPositionDutyCycle.Position = position;
    feeder.setControl(feederVelocityRequest);
  }

  //method to get feeder from other files when motor is private
  public TalonFX getFeeder(){
    return feeder;
  }
}
