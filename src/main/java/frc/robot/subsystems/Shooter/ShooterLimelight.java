package frc.robot.subsystems.Shooter;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Drivetrain.SwerveDrive;
import frc.robot.subsystems.LimelightCommands.EstimatingDistance;
import frc.robot.subsystems.LimelightCommands.FaceTargetWhileDriving;
import frc.robot.subsystems.LimelightCommands.LockToTarget;

public class ShooterLimelight extends SubsystemBase{
    private double xOffset;
    private double yOffset;
    private double targetArea;
    NetworkTable table;
    private static final String limelightName = "limelight-shooter";
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

 @Override
  public void periodic() {
    // This method will be called once per scheduler run
 
  }

public ShooterLimelight(){
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("botpose_wpiblue").getDoubleArray(new double[6]);

    table = NetworkTableInstance.getDefault().getTable(limelightName);

    xOffset = table.getEntry("tx").getDouble(0);
    yOffset = table.getEntry("ty").getDouble(0);
    targetArea = table.getEntry("ta").getDouble(0);
    
}


public double getTx(){
    return xOffset;
  }

  public double getTy(){
    return yOffset;
  }

public NetworkTable getTable(){
    return table;
}

public Pose3d getBotPoseBlue(){
    double[] array = NetworkTableInstance.getDefault().getTable(limelightName).getEntry("botpose_wpiblue").getDoubleArray(new double[6]);
    return new Pose3d(
        new Translation3d(array[0], array[1], array[2]),
        new Rotation3d(Units.degreesToRadians(array[3]), Units.degreesToRadians(array[4]),Units.degreesToRadians(array[5]))
    );
    }

public Command faceTargetWhileDriving(SwerveDrive swerveDrive, DoubleSupplier xVelocity, DoubleSupplier yVelocity, DoubleSupplier thetaVelocity) {
    return new FaceTargetWhileDriving(swerveDrive, this, xVelocity, yVelocity, thetaVelocity); 
  }

public Command estimatingDistance() {
    return new EstimatingDistance(this);
  }

  public Command lockToTarget(SwerveDrive swerveDrive, ShooterLimelight limelight){
    return new LockToTarget(swerveDrive, this);
  }
}