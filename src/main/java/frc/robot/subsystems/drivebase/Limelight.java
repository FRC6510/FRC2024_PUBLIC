// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.drivebase;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {
  private final NetworkTable limelightTable;
  /** Creates a new Limelight. */
  private Limelight() {
    limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
  }

  public static final Limelight INSTANCE = new Limelight();

  //finds heading of the robot (using horizontal offset)
  public static double getHeading(){
    double heading = INSTANCE.limelightTable.getEntry("tx").getDouble(0.0);
    return heading;
  }

  /**
   * Gets the robot's pose using the AprilTag detection from the Limelight.
   * The botpose entry returns [X, Y, Z, pitch, yaw, roll].
   * Extracts X, Y, and yaw from the botpose array.
   * @return Pose2d object representing the robot's position on the field (x, y, theta)
   */
  public static Pose2d getRobotPosition(){
    double[] botpose = INSTANCE.limelightTable.getEntry("botpose").getDoubleArray(new double[6]);
    double x = botpose[0];
    double y = botpose[1];
    double theta = botpose[4];

    return new Pose2d(x, y, Rotation2d.fromDegrees(theta));
  }

  public static double getLatency(){
    double tl = INSTANCE.limelightTable.getEntry("tl").getDouble(0);
    double cl = INSTANCE.limelightTable.getEntry("cl").getDouble(0);
    return (tl / 1000) + (cl / 1000);
  }

  public static int getTagCount() {
    return INSTANCE.limelightTable.getEntry("tagcount").getNumber(0).intValue();
  }

}
