// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Feeder;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase {
  private static final int NOTE_DETECTOR_DIO_CHANNEL = 9;
  private final DigitalInput noteDetector;
  private static final int FEEDER_CTRE_BUS = 16;
  private final TalonFX feeder;


  /** Creates a new Feeder. */
  public Feeder() {
    this.noteDetector = new DigitalInput(NOTE_DETECTOR_DIO_CHANNEL);
    this.feeder = new TalonFX(FEEDER_CTRE_BUS);
  }

  @Override
  public void periodic() {
    
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("noteDetector", getSensorState());
  }

  public Command spinFeeder(DoubleSupplier powerSupplier) {
    return new SpinFeeder(this, powerSupplier);
  }

  public boolean getSensorState() {
    return noteDetector.get();
  }

  //method to get feeder from other files when motor is private
  public TalonFX getFeeder(){
    return feeder;
  }
}
