package frc.robot.subsystems.Drivetrain;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * A command for manually controlling the drivetrain with driver inputs (via the driver controller)
 * TODO Cleanup
 */
public class ManualDrive extends Command {
	private final SwerveDrive swerveDrive;
	private final DoubleSupplier xSupplier;
	private final DoubleSupplier ySupplier;
	private final DoubleSupplier thetaSupplier;
	
	/**
	 * Creates an instance of the ManualDrive command. 
	 * @param swerveDrive - from the SwerveDrive subsystem
	 * @param xSupplier - target velocity along the x-axis (left Y on the joystick)
	 * @param ySupplier - target velocity along the y-axis (left X on the joystick)
	 * @param thetaSupplier - target rotational velocity  
	 */
	ManualDrive(SwerveDrive swerveDrive, DoubleSupplier xSupplier, DoubleSupplier ySupplier, DoubleSupplier thetaSupplier) {
		this.swerveDrive = swerveDrive;
		this.xSupplier = xSupplier;
		this.ySupplier = ySupplier;
		this.thetaSupplier = thetaSupplier;
		addRequirements(swerveDrive);
	}

	@Override
	public void initialize() {}

	@Override
	public void execute() {
		swerveDrive.fieldCentricDrive(
			deadzone(xSupplier.getAsDouble(), 0.1) * SwerveConstants.drivetrainMotorInfo.ROBOTMAXVELOCITY_DRIVE * swerveDrive.speedLimit,
			deadzone(ySupplier.getAsDouble(), 0.1) * SwerveConstants.drivetrainMotorInfo.ROBOTMAXVELOCITY_DRIVE * swerveDrive.speedLimit, //multiply each velocity by max velocity metres per second
			deadzone(thetaSupplier.getAsDouble(), 0.1) * SwerveConstants.drivetrainMotorInfo.ROBOTMAXVELOCITY_STEER * swerveDrive.speedLimit
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

	@Override
	public void end(boolean interrupted) {}

	@Override
	public boolean isFinished() {
		return false;
	}
}