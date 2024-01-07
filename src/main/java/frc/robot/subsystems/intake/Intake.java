// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.


package frc.robot.subsystems.intake;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.hardware.TalonFX;


public class Intake extends SubsystemBase {
  /** Creates a new Intake. */
  private static final int NOTE_DETECTOR_DIO_CHANNEL = 9;
  private final DigitalInput noteDetector;
  private static final int INTAKE_CTRE_BUS = 17;
  public TalonFX intakeMotor;
  private double interruptTime;


  public Intake() {
    this.noteDetector = new DigitalInput(NOTE_DETECTOR_DIO_CHANNEL);
    this.intakeMotor = new TalonFX(INTAKE_CTRE_BUS, "rio");
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }


  public Command spinIntake(double power){
    return new SpinIntake(this, power);
  }


  public boolean getSensorState() {
    return noteDetector.get();
  }


  public double currentTime(){
    return System.currentTimeMillis();
  }
 
  public void keepSpinningTime(){
    interruptTime = currentTime();
    while((currentTime()- interruptTime) < 3000){
      intakeMotor.set(0.3);
    }
    intakeMotor.set(0);
  }
}
