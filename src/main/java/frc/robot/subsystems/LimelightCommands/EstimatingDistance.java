package frc.robot.subsystems.LimelightCommands;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter.ShooterLimelight;

public class EstimatingDistance extends Command {
	private final ShooterLimelight limelight;

	public EstimatingDistance(ShooterLimelight limelightShooter) {
		this.limelight = limelightShooter;
		addRequirements(limelightShooter);
	}

	@Override
	public void initialize() {
	}

	@Override
	public void execute() {
		NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-shooter");
		NetworkTableEntry ty = table.getEntry("ty");
		double targetOffsetAngle_Vertical = ty.getDouble(0.0);

		// how many degrees back is your limelight rotated from perfectly vertical?
		double limelightMountAngleDegrees = 33.0; 

		// distance from the center of the Limelight lens to the floor
		double limelightLensHeightMetres = 0.224;

		// distance from the target to the floor
		double goalHeightMils = 1.320;

		double angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
		double angleToGoalRadians = Math.toRadians(angleToGoalDegrees);

		//calculate distance
		double distanceFromLimelightToGoalInches = (goalHeightMils - limelightLensHeightMetres) / Math.tan(angleToGoalRadians);

		SmartDashboard.putNumber("estimated distance (m)", distanceFromLimelightToGoalInches);
	}

	@Override
	public void end(boolean interrupted) {
	}

	@Override
	public boolean isFinished() {
		return super.isFinished();
	}
}
