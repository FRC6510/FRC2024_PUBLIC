// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Sensors;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;


public class TouchSensor extends SubsystemBase {
  private static final int DIGITAL_INPUT_PORT = 8;
  private final DigitalInput touchSensor;
  private final Trigger trigger;

  public TouchSensor() {
    touchSensor = new DigitalInput(DIGITAL_INPUT_PORT);
    trigger = new Trigger(this::getTouchSensor);
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("touch sensor", getTouchSensor());
    // This method will be called once per scheduler run
  }

  public Trigger getTrigger(){
    return trigger;
  }

  public boolean getTouchSensor() {
    return touchSensor.get();
  }
}
