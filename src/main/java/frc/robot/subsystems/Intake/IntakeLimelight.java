// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Intake;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeLimelight extends SubsystemBase {
   private double xOffset;
    private double yOffset;
    private double targetArea;
    NetworkTable table;
    private static final String limelightName = "limelight-intake";
    // 

     public double getxOffset(){
        return xOffset;
    }

    public double getyOffset() {
        return yOffset;
    }

    public double getTargetArea() {
        return targetArea;
    }
  /** Creates a new FeederLimelight. */
  public IntakeLimelight() {
    NetworkTableInstance.getDefault().getTable(limelightName).getEntry("botpose").getDoubleArray(new double[6]);

    table = NetworkTableInstance.getDefault().getTable(limelightName);

    xOffset = table.getEntry("tx").getDouble(0);
    yOffset = table.getEntry("ty").getDouble(0);
    targetArea = table.getEntry("ta").getDouble(0);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
