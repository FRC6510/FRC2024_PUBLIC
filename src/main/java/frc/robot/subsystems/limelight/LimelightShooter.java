// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.limelight;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.limelight.SwitchShooterPipeline;
import frc.robot.subsystems.limelight.SwitchShooterPipeline.ShooterPipelineID;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.function.DoubleSupplier;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.subsystems.Drivetrain.SwerveDrive;

public class LimelightShooter extends SubsystemBase {

  // private final NetworkTableEntry validTargets;
  // private final NetworkTableEntry xOffset;
  // private final NetworkTableEntry yOffset;
  // private final NetworkTableEntry targetArea;
  // private final NetworkTableEntry latencyContribution;
  // private final NetworkTableEntry captureLatency;
  // private final NetworkTableEntry shortSideLength;
  // private final NetworkTableEntry longSideLength;
  // private final NetworkTableEntry lengthHor;
  // private final NetworkTableEntry lengthVert;
  // private final NetworkTableEntry pipeIndex;  
  // private final NetworkTableEntry pipeline;

  // private final boolean validTargets;
  private double xOffset;
  private double yOffset;
  private double targetArea;
  // private final double[] position;
  // private final double id;
  // private final double latencyContribution;
  // private final double captureLatency;

  public LimelightShooter() {
    // These are the steps for getting values from networktable
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-shooter");
    
    // validTargets = table.getEntry("tv");
    xOffset = table.getEntry("tx").getDouble(0);
    yOffset = table.getEntry("ty").getDouble(0);
    targetArea = table.getEntry("ta").getDouble(0);
    // latencyContribution = table.getEntry("tl");
    // captureLatency = table.getEntry("cl");
    // shortSideLength = table.getEntry("tshort");
    // longSideLength = table.getEntry("tlong");
    // lengthHor = table.getEntry("thor");
    // lengthVert = table.getEntry("tvert");
    // pipeIndex = table.getEntry("getpipe");
    // pipeline = table.getEntry("pipeline");

    // NetworkTableEntry mode = table.getEntry("camMode");
    // mode.setNumber(0);

    // NetworkTableEntry ty = table.getEntry("ty");
    // double targetOffsetAngle_Vertical = ty.getDouble(0.0);

    // how many degrees back is your limelight rotated from perfectly vertical?
    double limelightMountAngleDegrees = 25.0; 

    // distance from the center of the Limelight lens to the floor
    double limelightLensHeightInches = 20.0; 

    // distance from the target to the floor
    double goalHeightInches = 60.0; 

    double angleToGoalDegrees = limelightMountAngleDegrees + yOffset;
    double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0);

    //calculate distance
    double targetDistance = (goalHeightInches - limelightLensHeightInches) / Math.tan(angleToGoalRadians);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    NetworkTableInstance.getDefault().getTable("limelight-shooter").getEntry("pipeline").setNumber(1);

    // These values are reset everytime the scheduler runs
    // double x = xOffset.getDouble(0.0);
    // double y = yOffset.getDouble(0.0);
    // double area = targetArea.getDouble(0.0);
    // double v = validTargets.getDouble(0.0);
    // double tl = latencyContribution.getDouble(0.0);
    // double cl = captureLatency.getDouble(0.0);
    // double tshort = shortSideLength.getDouble(0.0);
    // double tlong = longSideLength.getDouble(0.0);
    // double thor = lengthHor.getDouble(0.0);
    // double tvert = lengthVert.getDouble(0.0);
    // double getpipe = pipeIndex.getDouble(0.0);
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-shooter");

    xOffset = table.getEntry("tx").getDouble(0);
    yOffset = table.getEntry("ty").getDouble(0);
    targetArea = table.getEntry("ta").getDouble(0);

    SmartDashboard.putNumber("LimelightX", xOffset);
    SmartDashboard.putNumber("LimelightY", yOffset);
    SmartDashboard.putNumber("LimelightA", targetArea);
    // SmartDashboard.putNumber("LimelightX", v);
    // SmartDashboard.putNumber("LimelightY", tl);
    // SmartDashboard.putNumber("LimelightA", cl);
    // SmartDashboard.putNumber("LimelightX", tshort);
    // SmartDashboard.putNumber("LimelightY", tlong);
    // SmartDashboard.putNumber("LimelightA", thor);
    // SmartDashboard.putNumber("LimelightY", tvert);
    // SmartDashboard.putNumber("LimelightA", getpipe);
 
    // NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-shooter");
    // double tx = table.getEntry("tx").getDouble(0.0);
    // double ty = table.getEntry("ty").getDouble(0.0);  

    // double[] botpose = table.getEntry("botpose").getDoubleArray(new double[6]);


    // NetworkTableInstance.getDefault().getTable("limelight").getEntry("botpose").getDoubleArray(new double[6]);
    // SmartDashboard.putNumber("TX", position[0]);
    // SmartDashboard.putNumber("TY", position[1]);
    // SmartDashboard.putNumber("TZ", position[2]);
    // SmartDashboard.putNumber("RX", position[3]);
    // SmartDashboard.putNumber("RY", position[4]);
    // SmartDashboard.putNumber("RZ", position[5]);
    
    SmartDashboard.putNumber("ty", yOffset);
    SmartDashboard.putNumber("tx", xOffset);
  }

  void setPipeline(int id) {
    // pipeline.setNumber(id);
  }

  public double getTx(){
    return xOffset;
  }

  public double getTy(){
    return yOffset;
  }

  public Command switchShooterPipeline(ShooterPipelineID pipelineID) {
    return new SwitchShooterPipeline(this, pipelineID);
  }

  public Command estimatingDistance() {
    return new EstimatingDistance(this);
  }

  public Command lockToTarget(SwerveDrive swerveDrive, LimelightShooter limelight){
    return (Command) new LockToTarget(swerveDrive, this);
  }

  public Command faceTargetWhileDriving(SwerveDrive swerveDrive, DoubleSupplier xVelocity, DoubleSupplier yVelocity, DoubleSupplier thetaVelocity) {
    return (Command) new FaceTargetWhileDriving(swerveDrive, this, xVelocity, yVelocity, thetaVelocity); 
  }
}
