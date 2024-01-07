// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class LimitSwitch extends SubsystemBase {
private int DIGITAL_INPUT_PORT;
private DigitalInput limitSwitch = new DigitalInput(DIGITAL_INPUT_PORT);

public boolean getLimitSwitch() {
    return limitSwitch.get();

}


  public LimitSwitch() {

  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("limit switch", getLimitSwitch());
    // This method will be called once per scheduler run
  }
}
