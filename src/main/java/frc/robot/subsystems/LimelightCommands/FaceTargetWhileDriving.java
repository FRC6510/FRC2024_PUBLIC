package frc.robot.subsystems.LimelightCommands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain.SwerveDrive;
import frc.robot.subsystems.Shooter.ShooterLimelight;

import java.util.function.DoubleSupplier;

public class FaceTargetWhileDriving extends Command{
	private final DoubleSupplier xSupplier;
	private final DoubleSupplier ySupplier;
	private final DoubleSupplier thetaSupplier;

  public boolean xOffsetZero = false;
  ShooterLimelight limelight;
  SwerveDrive swerveDrive;
  double setpoint;
  DoubleSupplier xVelocity; 
  DoubleSupplier yVelocity; 

  double xOffset;
  double yOffset;
  double setpointAngle;

  double blueX = -8;
  double blueY = 1.5;

  double redX = 8;
  double redY = 1.5;

  final PIDController pidController;

  //below is constructor --> does setup tasks for instances of commmands; parameters
  public FaceTargetWhileDriving(SwerveDrive swerveDrive, ShooterLimelight limelightShooter, DoubleSupplier xSupplier, DoubleSupplier ySupplier, DoubleSupplier thetaSupplier){
    this.limelight = limelightShooter;
    this.swerveDrive = swerveDrive;
		this.xSupplier = xSupplier;
		this.ySupplier = ySupplier;
		this.thetaSupplier = thetaSupplier;
    addRequirements(swerveDrive);
    this.pidController= new PIDController(0.010, 0, 0.00001);//0.000118, 0, 0 120
  }
    
  public void initialize() {
    // limelight.getTable().getInstance();
    // NetworkTableInstance.getDefault().getTable("limelight-shooter").getEntry("Pipeline").setNumber(1);
    if (DriverStation.getAlliance().get() == Alliance.Blue){
      xOffset = SwerveDrive.poseEstimatorOdometry.getEstimatedPosition().getX() - (blueX);
      yOffset = SwerveDrive.poseEstimatorOdometry.getEstimatedPosition().getY() - (blueY);
      setpointAngle = Math.toDegrees(Math.atan2(yOffset, xOffset));
    } else if (DriverStation.getAlliance().get() == Alliance.Red){
      xOffset = SwerveDrive.poseEstimatorOdometry.getEstimatedPosition().getX() - (redX);
      yOffset = SwerveDrive.poseEstimatorOdometry.getEstimatedPosition().getY() - (redY);
      setpointAngle = Math.toDegrees(Math.atan2(yOffset, xOffset));
    }
    pidController.setSetpoint(setpointAngle);
  }

	public void execute() {
    if (DriverStation.getAlliance().get() == Alliance.Blue){
      xOffset = SwerveDrive.poseEstimatorOdometry.getEstimatedPosition().getX() - (blueX);
      yOffset = SwerveDrive.poseEstimatorOdometry.getEstimatedPosition().getY() - (blueY);
      setpointAngle = Math.toDegrees(Math.atan2(yOffset, xOffset));
    } else if (DriverStation.getAlliance().get() == Alliance.Red){
      xOffset = SwerveDrive.poseEstimatorOdometry.getEstimatedPosition().getX() - (redX);
      yOffset = SwerveDrive.poseEstimatorOdometry.getEstimatedPosition().getY() - (redY);
      setpointAngle = Math.toDegrees(Math.atan2(yOffset, xOffset));
    }

    pidController.setSetpoint(setpointAngle);
    double currentHeading = SwerveDrive.poseEstimatorOdometry.getEstimatedPosition().getRotation().getDegrees();
    double power = pidController.calculate(currentHeading, setpointAngle);

    SmartDashboard.putNumber("tx", limelight.getTx());
    SmartDashboard.putNumber("!Target angle", setpointAngle);
    SmartDashboard.putNumber("!Current yaw", currentHeading);

    //swerveDrive.fieldCentricDrive(xSupplier.getAsDouble(), ySupplier.getAsDouble(), power);
    swerveDrive.fieldCentricDrive(
			deadzone(xSupplier.getAsDouble(), 0.1) * 5.89, //multiply each velocity by max velocity metres per second
			deadzone(ySupplier.getAsDouble(), 0.1) * 5.89,
			power * (2*Math.PI)
		); //multiply power by 1? also speed limit isn't necessary?
    	}

  private double deadzone(double in, double deadzone) {
    double magnitude = Math.abs(in);
    if (magnitude < deadzone) {
        return 0;
    }
    double multiplier = 1 / (1 - deadzone);
    return multiplier * (magnitude - deadzone) * Math.signum(in);
}


	public void end(boolean interrupted) {
    System.out.println("ending");
	}

	public boolean isFinished() {
		return deadzone(thetaSupplier.getAsDouble(), 0.1) != 0;
	}
}