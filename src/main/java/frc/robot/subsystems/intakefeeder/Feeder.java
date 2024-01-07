// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intakefeeder;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class Feeder extends SubsystemBase {
  /** Creates a new Feeder. */
    private static final int FEEDER_CTRE_BUS = 19;
    private final TalonFX feeder;
    private static final int NOTE_DETECTOR_DIO_CHANNEL = 9;
    public static DigitalInput noteDetector;

  public Feeder(){
    this.feeder = new TalonFX(FEEDER_CTRE_BUS);
    noteDetector = new DigitalInput(NOTE_DETECTOR_DIO_CHANNEL);
  }


  public void getSensorState(){
    SmartDashboard.putBoolean("feeder note possessed", noteDetector.get()==false);
  }

  @Override
  public void periodic() {
    getSensorState();
  }

  public Command spinFeeder(Intake intake, DoubleSupplier powerSupplier) {
    return new SpinFeeder(this, intake ,powerSupplier);
  }

  //method to get feeder from other files when motor is private
  public TalonFX getFeeder(){
    return feeder;
  }

  public void stopSpinning() {
   if (noteDetector.get()==false){
    feeder.set(0);
    }
  } 

  public boolean noteDetected() {
    return noteDetector.get();
  }
}