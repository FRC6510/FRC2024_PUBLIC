package frc.robot.subsystems.LimelightCommands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTableInstance;
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

  final PIDController pidController;

  //below is constructor --> does setup tasks for instances of commmands; parameters
  public FaceTargetWhileDriving(SwerveDrive swerveDrive, ShooterLimelight limelightShooter, DoubleSupplier xSupplier, DoubleSupplier ySupplier, DoubleSupplier thetaSupplier){
    this.limelight = limelightShooter;
    this.swerveDrive = swerveDrive;
		this.xSupplier = xSupplier;
		this.ySupplier = ySupplier;
		this.thetaSupplier = thetaSupplier;
    addRequirements(swerveDrive);
    this.pidController= new PIDController(0.15, 0, 0.00001);//0.000118, 0, 0 120
  }
    
  public void initialize() {
    setpoint = 0;
    limelight.getTable().getInstance();
    NetworkTableInstance.getDefault().getTable("limelight-shooter").getEntry("Pipeline").setNumber(1);
    pidController.setSetpoint(setpoint);

  }

	public void execute() {
    double power = pidController.calculate(limelight.getTable().getEntry("tx").getDouble(0), setpoint);

    SmartDashboard.putNumber("tx", limelight.getTx());
    SmartDashboard.putNumber("output of pid controller", power);

    //swerveDrive.fieldCentricDrive(xSupplier.getAsDouble(), ySupplier.getAsDouble(), power);
    swerveDrive.fieldCentricDrive(
			deadzone(xSupplier.getAsDouble(), 0.1) * 5.89, //multiply each velocity by max velocity metres per second
			deadzone(ySupplier.getAsDouble(), 0.1) * 5.89,
			power
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