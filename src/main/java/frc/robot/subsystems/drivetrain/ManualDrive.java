package frc.robot.subsystems.drivetrain;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

public class ManualDrive extends Command {
	private final SwerveDrive swerveDrive;
	private final DoubleSupplier xSupplier;
	private final DoubleSupplier ySupplier;
	private final DoubleSupplier thetaSupplier;
	
	ManualDrive(SwerveDrive swerveDrive, DoubleSupplier xSupplier DoubleSupplier ySupplier, DoubleSupplier thetaSupplier) {
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
		swerveDrive.drive(
			deadzone(xSupplier.getAsDouble(), 0.2),
			deadzone(ySupplier.getAsDouble(), 0.2),
			deadzone(thetaSupplier.getAsDouble(), 0.2)
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

	@Override
	public void end(boolean interrupted) {}

	@Override
	public boolean isFinished() {
		return true;
	}
}
