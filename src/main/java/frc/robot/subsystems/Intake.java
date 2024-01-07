// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.SpinIntake;

public class Intake extends SubsystemBase {
  //_ctre_bus shows what can bus connected to
  // can be called anything, but gives more information
  private static final int INTAKE_CTRE_BUS = 17;
  private static final int INTAKE_CENSOR_DIO_CHANNEL= 16;

  public TalonFX intakeMotor;
  private final DigitalInput intakeSensor;


  /** Creates a new Intake. */
  public Intake() {
    this.intakeMotor = new TalonFX(INTAKE_CTRE_BUS, "rio");
    this.intakeSensor = new DigitalInput(INTAKE_CENSOR_DIO_CHANNEL);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  //not receivng data, no return, but method still runs
  // public void spin(double power){
  //   //return new SpinIntake(intake, power);
  //   intake.set(power);
  // }

  

  public Command SpinIntake(double power){
    return new SpinIntake(this, power);
  }
  
  public boolean getSensorState(){
    return intakeSensor.get();
  }

}
