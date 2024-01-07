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

public class LockToHeading extends Command{
	private final DoubleSupplier xSupplier;
	private final DoubleSupplier ySupplier;
	private final DoubleSupplier thetaSupplier;
  double heading;

  public boolean xOffsetZero = false;
  SwerveDrive swerveDrive;
  double setpoint;
  DoubleSupplier xVelocity; 
  DoubleSupplier yVelocity; 

  double xOffset;
  double yOffset;
  double setpointAngle;

  double currentHeading;
  double power;

  double blueX = -8.5;
  double blueY = 1.5;

  double redX = -8.5;
  double redY = -1.5;

  final PIDController pidController;

  /**
   * SETS HEADING TO LOCK TO TO Zero
   * @param swerveDrive
   * @param xSupplier
   * @param ySupplier
   * @param thetaSupplier
   */
  public LockToHeading(SwerveDrive swerveDrive, DoubleSupplier xSupplier, DoubleSupplier ySupplier, DoubleSupplier thetaSupplier){
    this.swerveDrive = swerveDrive;
		this.xSupplier = xSupplier;
		this.ySupplier = ySupplier;
		this.thetaSupplier = thetaSupplier;
    addRequirements(swerveDrive);
    this.pidController= new PIDController(0.025, 0, 0.00001);//0.000118, 0, 0 120//0.015
    pidController.enableContinuousInput(-180, 180);
    this.heading = 0;
  }

  /**
   * Can lock to any heading.
   * @param swerveDrive
   * @param xSupplier
   * @param ySupplier
   * @param thetaSupplier
   * @param heading
   */
  public LockToHeading(SwerveDrive swerveDrive, DoubleSupplier xSupplier, DoubleSupplier ySupplier, DoubleSupplier thetaSupplier, double heading){
    this.swerveDrive = swerveDrive;
		this.xSupplier = xSupplier;
		this.ySupplier = ySupplier;
		this.thetaSupplier = thetaSupplier;
    addRequirements(swerveDrive);
    this.pidController= new PIDController(0.025, 0, 0.00001);//0.000118, 0, 0 120//0.015
    pidController.enableContinuousInput(-180, 180);
    this.heading = heading;
  }
    
  public void initialize() {
    // limelight.getTable().getInstance();
    // NetworkTableInstance.getDefault().getTable("limelight-shooter").getEntry("Pipeline").setNumber(1);

  }

	public void execute() {

      currentHeading = swerveDrive.getIMU().getRotation2d().getDegrees();
      power = pidController.calculate(currentHeading, heading);


    //swerveDrive.fieldCentricDrive(xSupplier.getAsDouble(), ySupplier.getAsDouble(), power);
    swerveDrive.fieldCentricDrive(
			deadzone(xSupplier.getAsDouble(), 0.1) * 5.89, //multiply each velocity by max velocity metres per second
			deadzone(ySupplier.getAsDouble(), 0.1) * 5.89,
			power*(2*Math.PI)
		);
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