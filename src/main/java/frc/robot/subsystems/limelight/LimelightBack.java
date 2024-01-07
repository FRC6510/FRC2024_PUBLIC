// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.limelight;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Drivetrain.SwerveDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class LimelightBack extends SubsystemBase {
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
  private NetworkTableEntry pipeline;
  private final NetworkTableEntry classID;
  private final NetworkTableEntry camPose;
  
  private SwerveDrive swerveDrive;

  public LimelightBack() {
    // These are the steps for getting values from networktable
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-back");
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("<variablename>").getDoubleArray(new double[6]);





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
    pipeline = table.getEntry("pipeline");
    classID = table.getEntry("tclass");
    camPose = table.getEntry("camerapose_targetspace");

    NetworkTableEntry mode = table.getEntry("camMode");

    swerveDrive = new SwerveDrive();

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
    double camerapose_targetspace = camPose.getDouble(0.0);
    
      
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
    SmartDashboard.putNumber("LimelightX", camerapose_targetspace);

    
  }

  /**
   * @return 
   * the x-axis difference between the current detection and the crosshair
   * @see #getTy
   */
  public double getTx(){
    return xOffset.getDouble(0);
  }

    /**
   * @return 
   * the y-axis difference between the current detection and the crosshair
   * @see #getTx
   */
  public double getTy(){
    return yOffset.getDouble(0);
  }

  public void Locking() {
    //.getDouble(0), gets the value of xOffset, but if no value is found (e.g. limelight breaks), defaults value 0
    //pi/180 is radians; theta velocity is speed

    //while xOffset value is within certain range (0), stop turning, otherwise, keep turning slowly
    if(xOffset.getDouble(0) == 0){
      swerveDrive.fieldCentricDrive(0, 0, 0);
    }
    else{
      swerveDrive.fieldCentricDrive(0, 0, Math.PI/180);
    }

    //while yOffset value is within range (negative value near robot), stop moving towards, otherwise, keep moving forwards slowly
    // if(yOffset.getDouble(0) <= -15){
    //   swerveDrive.fieldCentricDrive(0, 0, 0);
    // }
    // else{
    //   swerveDrive.fieldCentricDrive(0, Math.PI/180, 0);
    // }
    // only for robot container swerveDrive.driveCommand()
  
  }
 
  // public void Locking() {
  //   while(validTargets != null){
  //     xOffset = 0;
  //     yOffset = 0;
  //   }
  // }

  public LockToTarget lockToTarget(SwerveDrive swerveDrive, LimelightBack limelight){
    return new LockToTarget(this, swerveDrive);
  }

  //public Command switchBackPipeline(PipelineID pipelineID, )

  /**
   * 
   * 
   * @param id a pipeline number from 1-10
   */
  void setPipeline(int id) {
    pipeline.setNumber(id);
  }
}
