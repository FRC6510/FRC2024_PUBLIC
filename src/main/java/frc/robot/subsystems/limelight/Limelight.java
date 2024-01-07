// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package frc.robot.subsystems.limelight;

// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.networktables.NetworkTable;
// import edu.wpi.first.networktables.NetworkTableEntry;
// import edu.wpi.first.networktables.NetworkTableInstance;

// public class Limelight extends SubsystemBase {

//   private final NetworkTableEntry validTargets;
//   private final NetworkTableEntry xOffset;
//   private final NetworkTableEntry yOffset;
//   private final NetworkTableEntry targetArea;
//   private final NetworkTableEntry latencyContribution;
//   private final NetworkTableEntry captureLatency;
//   private final NetworkTableEntry shortSideLength;
//   private final NetworkTableEntry longSideLength;
//   private final NetworkTableEntry lengthHor;
//   private final NetworkTableEntry lengthVert;
//   private final NetworkTableEntry pipeIndex;  
//   private final NetworkTableEntry pipeline;
//   private final NetworkTableEntry mode;

//   private final NetworkTableEntry position;
//   private final NetworkTableEntry id;

//   private final boolean validTargets;
//   private final double xOffset;
//   private final double yOffset;
//   private final double targetArea;
//   private final double latencyContribution;
//   private final double captureLatency;
//   private final double shortSideLength;
//   private final double longSideLength;
//   private final double lengthHor;
//   private final double lengthVert;
//   private final double pipeIndex;  
//   private final double pipeline;
//   private final double[] position;
//   private final double id;
//   private final double mode;
//   private final NetworkTableEntry mode;


//   public Limelight() {
//     // These are the steps for getting values from networktable
//     NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-shooter");
    
//     xOffset = table.getEntry("tx").getDouble(0);
//     yOffset = table.getEntry("ty").getDouble(0);
//     targetArea = table.getEntry("ta").getDouble(0);
//     validTargets = table.getEntry("tv");
//     xOffset = table.getEntry("tx");
//     yOffset = table.getEntry("ty");
//     targetArea = table.getEntry("ta");
//     latencyContribution = table.getEntry("tl");
//     captureLatency = table.getEntry("cl");
//     shortSideLength = table.getEntry("tshort");
//     longSideLength = table.getEntry("tlong");
//     lengthHor = table.getEntry("thor");
//     lengthVert = table.getEntry("tvert");
//     pipeIndex = table.getEntry("getpipe");
//     pipeline = table.getEntry("pipeline");

//     shortSideLength = table.getEntry("tshort");
//     longSideLength = table.getEntry("tlong");
//     lengthHor = table.getEntry("thor");
//     lengthVert = table.getEntry("tvert");/TODO no lengths were on the limelight helper
//     pipeIndex = LimelightHelpers.getCurrentPipelineIndex("limelight");
//     pipeline = table.getEntry("pipeline");

//     mode = table.getEntry("camMode");
//     NetworkTableEntry mode = table.getEntry("camMode");
//     mode.setNumber(0);

//     NetworkTableInstance.getDefault().getTable("limelight-shooter").getEntry("limelight-shooter").getDoubleArray(new double[???]);
//     position = LimelightHelpers.getBotPose("limelight");
//     id = LimelightHelpers.getFiducialID("limelight");
    



//     NetworkTableEntry ty = table.getEntry("ty");
//     double targetOffsetAngle_Vertical = ty.getDouble(0.0);

//     // how many degrees back is your limelight rotated from perfectly vertical?
//     double limelightMountAngleDegrees = 25.0; 

//     // distance from the center of the Limelight lens to the floor
//     double limelightLensHeightInches = 20.0; 

//     // distance from the target to the floor
//     double goalHeightInches = 60.0; 

//     double angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
//     double angleToGoalRadians = angleToGoalDegrees * (Math.PI / 180.0);

//     //calculate distance
//     double targetDistance = (goalHeightInches - limelightLensHeightInches) / Math.tan(angleToGoalRadians);
//   }

//   @Override
//   public void periodic() {
//     // This method will be called once per scheduler run
//     NetworkTableInstance.getDefault().getTable("limelight-shooter").getEntry("pipeline").setNumber(1);

    
//     // These values are reset everytime the scheduler runs
//     double x = xOffset.getDouble(0.0);
//     double y = yOffset.getDouble(0.0);
//     double area = targetArea.getDouble(0.0);
//     double v = validTargets.getDouble(0.0);
//     double tl = latencyContribution.getDouble(0.0);
//     double cl = captureLatency.getDouble(0.0);
//     double tshort = shortSideLength.getDouble(0.0);
//     double tlong = longSideLength.getDouble(0.0);
//     double thor = lengthHor.getDouble(0.0);
//     double tvert = lengthVert.getDouble(0.0);
//     double getpipe = pipeIndex.getDouble(0.0);
    
//     double camMode = mode.getDouble(0.0);

//     double[] botpose = position.getDoubleArray(new double[6]);
//     double[] tid = id.getDoubleArray(new double[6]);

//     SmartDashboard.putNumber("LimelightX", xOffset);
//     SmartDashboard.putNumber("LimelightY", yOffset);
//     SmartDashboard.putNumber("LimelightA", targetArea);
//     SmartDashboard.putNumber("LimelightX", v);
//     SmartDashboard.putNumber("LimelightY", tl);
//     SmartDashboard.putNumber("LimelightA", cl);
//     SmartDashboard.putNumber("LimelightX", tshort);
//     SmartDashboard.putNumber("LimelightY", tlong);
//     SmartDashboard.putNumber("LimelightA", thor);
//     SmartDashboard.putNumber("LimelightY", tvert);
//     SmartDashboard.putNumber("LimelightA", getpipe);
//     SmartDashboard.putNumberArray("LimelightX", position);
//     SmartDashboard.putNumber("LimelightY", id);


//     // NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-shooter");
//     // double tx = table.getEntry("tx").getDouble(0.0);
//     // double ty = table.getEntry("ty").getDouble(0.0);  
    
//     // SmartDashboard.putNumber("ty", ty);
//     // SmartDashboard.putNumber("tx", tx);
//   }

//   // void setPipeline(int id) {
//   //   pipeline.setNumber(id);
//   // }

//   // public Command switchPipeline(PipelineID pipelineID) {
//   //   return new SwitchPipeline(this, pipelineID);
//   // }//TODO find a way to switch pipelines with the new limelight library 

  
// }