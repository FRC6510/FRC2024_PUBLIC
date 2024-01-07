package frc.robot.subsystems.Drivetrain;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * A command for moving the swerve drive during autonomous. Takes in a target position and moves the robot there. 
 */
public class AutonDrive extends Command {
	private final SwerveDrive swerveDrive;
	private final DoubleSupplier xSupplier;
	private final DoubleSupplier ySupplier;
	private final DoubleSupplier thetaSupplier;
	
	AutonDrive(SwerveDrive swerveDrive, DoubleSupplier xSupplier, DoubleSupplier ySupplier, DoubleSupplier thetaSupplier) {
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
			xSupplier.getAsDouble(),
			ySupplier.getAsDouble(),
			thetaSupplier.getAsDouble()
		);
	}

	@Override
	public void end(boolean interrupted) {}

	@Override
	public boolean isFinished() {
		return true;
	}
}
