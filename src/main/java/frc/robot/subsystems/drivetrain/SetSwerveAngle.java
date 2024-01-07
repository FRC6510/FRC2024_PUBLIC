package frc.robot.subsystems.drivetrain;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class SetSwerveAngle extends Command {

  private final SwerveDrive swerveDrive;

  private final DoubleSupplier xSupplier;
	private final DoubleSupplier ySupplier;

  private final Rotation2d heading;
  private final PIDController pidController;
  private final CommandXboxController driverController;

  Rotation2d thetaSetpoint; 
  HolonomicDriveController holonomicDriveController;

  /** Creates a new SetSwerveAngle. */
  public SetSwerveAngle(CommandXboxController driverController, SwerveDrive swerveDrive, DoubleSupplier xSupplier, DoubleSupplier ySupplier, Rotation2d heading) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.driverController = driverController;
    this.swerveDrive = swerveDrive;
		this.heading = heading;

    this.pidController = new PIDController(
      SwerveConstants.moduleConstants.ANGULAR_kP, 
      SwerveConstants.moduleConstants.ANGULAR_kI, 
      SwerveConstants.moduleConstants.ANGULAR_kD);
      
    pidController.enableContinuousInput(-180, 180);
		thetaSetpoint = heading;

    this.xSupplier = xSupplier;
    this.ySupplier = ySupplier;

		addRequirements(swerveDrive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    pidController.setSetpoint(heading.getDegrees());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    swerveDrive.fieldCentricDrive(
      deadzone(xSupplier.getAsDouble(), 0.1) * SwerveConstants.drivetrainMotorInfo.ROBOTMAXVELOCITY_DRIVE * swerveDrive.speedLimit,
			deadzone(ySupplier.getAsDouble(), 0.1) * SwerveConstants.drivetrainMotorInfo.ROBOTMAXVELOCITY_DRIVE * swerveDrive.speedLimit, //multiply each velocity by max velocity metres per second
      -pidController.calculate(swerveDrive.getIMU().getAngle()) * SwerveConstants.drivetrainMotorInfo.ROBOTMAXVELOCITY_STEER * swerveDrive.speedLimit
    );
  }

  /**
	 * Does deadzoning for the driver inputs.
	 * Target deadzone is the distance on your joystick that needs to be moved before it interacts with the motors
	 * @param in
	 * @param deadzone
	 * @return the output from the driver controlled joysticks
	 */
	private double deadzone(double in, double deadzone) {
    double magnitude = Math.abs(in);
    if (magnitude < deadzone) {
        return 0;
    }
    double multiplier = 1 / (1 - deadzone);
    return multiplier * (magnitude - deadzone) * Math.signum(in);
}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs(driverController.getRightX()) > 0.1;
  }
}