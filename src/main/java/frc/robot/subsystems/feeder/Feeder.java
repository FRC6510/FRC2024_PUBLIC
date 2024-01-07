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
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.Shooter.Shooter;
import frc.robot.subsystems.Shooter.ShooterLED;

public class Feeder extends SubsystemBase {
  private static final int NOTE_DETECTOR_DIO_CHANNEL = 9;
  private final DigitalInput noteDetector;
  private static final int FEEDER_CTRE_BUS = 16;
  public static final double FEEDER_SPEED = -60;// It was 50
  public static final double FEEDER_AUTO_SHOOT_SPEED = -40;       
  private final TalonFX feeder;
  private final XboxController operatorController;
  private final VelocityDutyCycle feederVelocityRequest;
  private final PositionDutyCycle feederPositionDutyCycle;
  private final Trigger noteDetectionTrigger;
  private final Trigger containsNoteTrigger;
  public static boolean wantToShoot;
  private boolean containsNote = true;
  private boolean previousDetected;
  // public static boolean intaken;

  /** Creates a new Feeder. */
  public Feeder(CommandXboxController controller) {
    Slot0Configs slot0ConfigsFeeder = new Slot0Configs();
    slot0ConfigsFeeder.kV = 0.0100;//TODO test for velocity
    slot0ConfigsFeeder.kP = 0.03565;
    slot0ConfigsFeeder.kI = 0;
    slot0ConfigsFeeder.kD = 0;
    this.feederVelocityRequest = new VelocityDutyCycle(0.0);
    this.feederPositionDutyCycle = new PositionDutyCycle(0.0);
    this.operatorController = controller.getHID();
    this.noteDetector = new DigitalInput(NOTE_DETECTOR_DIO_CHANNEL);
    this.feeder = new TalonFX(FEEDER_CTRE_BUS, "rio");
    feeder.getConfigurator().apply(slot0ConfigsFeeder);
    this.noteDetectionTrigger = new Trigger(() -> !noteDetector.get());
    this.containsNoteTrigger = new Trigger(() -> containsNote);
    wantToShoot = false;
  }

  @Override
  public void periodic() {
    
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("want to shoot", wantToShoot);

    SmartDashboard.putNumber("Feeder velocity", feeder.getVelocity().getValueAsDouble());

    if (operatorController.getRightTriggerAxis() > 0.1 || operatorController.getLeftTriggerAxis() > 0.1 || operatorController.getLeftBumper() || operatorController.getPOV() == 0 || operatorController.getYButton()){
      wantToShoot = true;
    } else {
      wantToShoot = false;
    }

    // if (previousDetected && !feederDetected){
    //   intaken = true;// note in
    // } else {
    //   intaken = false;
    // }


    boolean detected = noteDetectionTrigger.getAsBoolean();
    if (detected && !previousDetected) {
      containsNote = !containsNote;
    }
    previousDetected = detected;
    SmartDashboard.putBoolean("Has note", containsNote);

  }

  public Trigger getContainsNoteTrigger() {
    return containsNoteTrigger;
  }

  public boolean getContainsNote() {
    return containsNote;
  }

  public Command resetContainsFlag() {
    return new InstantCommand(() -> containsNote = false);
  }

  public Command spinFeeder(DoubleSupplier powerSupplier) {
    return new SpinFeeder(this, powerSupplier);
  }

  public Command holdFeeder(){
    return new FeederFixedPosition(this).withTimeout(0.5);
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
    feeder.setControl(feederPositionDutyCycle);
  }

  public double getFeederPosition(){
      return feeder.getPosition().getValueAsDouble();
  }

  //method to get feeder from other files when motor is private
  public TalonFX getFeeder(){
    return feeder;
  }

  public void setContainsNote(boolean hasNote){
    this.containsNote = hasNote;
  }

  public SpinFeederNoNoteLocks spinFeederWithoutNoteDetectionStops(double rps){
    return new SpinFeederNoNoteLocks(this, () -> rps);
  }
  
}
