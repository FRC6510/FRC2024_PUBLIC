// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight extends SubsystemBase {

  private final NetworkTableEntry validTargets;
  private final NetworkTableEntry xOffset;
  private final NetworkTableEntry yOffset;
  private final NetworkTableEntry targetArea;
  private final NetworkTableEntry latencyContribution;
  private final NetworkTableEntry captureLatency;
  private final NetworkTableEntry shortSideLength;
  private final NetworkTableEntry longSideLength;
  private final NetworkTableEntry lengthHor;
  private final NetworkTableEntry lengthVert;
  private final NetworkTableEntry pipeIndex;  
  private final NetworkTableEntry classID;
  
  private final Trigger triggerTargetDetected;
  

  public Limelight() {
    // These are the steps for getting values from networktable
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-back");

    triggerTargetDetected = new Trigger(this::getTv);
    

    validTargets = table.getEntry("tv");
    xOffset = table.getEntry("tx");
    yOffset = table.getEntry("ty");
    targetArea = table.getEntry("ta");
    latencyContribution = table.getEntry("tl");
    captureLatency = table.getEntry("cl");
    shortSideLength = table.getEntry("tshort");
    longSideLength = table.getEntry("tlong");
    lengthHor = table.getEntry("thor");
    lengthVert = table.getEntry("tvert");
    pipeIndex = table.getEntry("getpipe");
    // pipeline = table.getEntry("pipeline");
    classID = table.getEntry("tclass");

    NetworkTableEntry mode = table.getEntry("camMode");

    mode.setNumber(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    NetworkTableInstance.getDefault().getTable("limelight-back").getEntry("pipeline").setNumber(1);

    // These values are reset everytime the scheduler runs
    double tx = xOffset.getDouble(0.0);
    double ty = yOffset.getDouble(0.0);
    double ta = targetArea.getDouble(0.0);
    double tv = validTargets.getDouble(0.0);
    double tl = latencyContribution.getDouble(0.0);
    double cl = captureLatency.getDouble(0.0);
    double tshort = shortSideLength.getDouble(0.0);
    double tlong = longSideLength.getDouble(0.0);
    double thor = lengthHor.getDouble(0.0);
    double tvert = lengthVert.getDouble(0.0);
    double getpipe = pipeIndex.getDouble(0.0);
    double tclass = classID.getDouble(0.0);
    
      
    SmartDashboard.putNumber("LimelightX", tx);
    SmartDashboard.putNumber("LimelightY", ty);
    SmartDashboard.putNumber("LimelightA", ta);
    SmartDashboard.putNumber("LimelightX", tv);
    SmartDashboard.putNumber("LimelightY", tl);
    SmartDashboard.putNumber("LimelightA", cl);
    SmartDashboard.putNumber("LimelightX", tshort);
    SmartDashboard.putNumber("LimelightY", tlong);
    SmartDashboard.putNumber("LimelightA", thor);
    SmartDashboard.putNumber("LimelightY", tvert);
    SmartDashboard.putNumber("LimelightA", getpipe);
    SmartDashboard.putNumber("LimelightX", tclass);
    
  }

  public boolean getTv(){
    return validTargets.getBoolean(false);
  }
  
  public Trigger getTriggerTargetDetected(){
    return triggerTargetDetected;
  }
}


