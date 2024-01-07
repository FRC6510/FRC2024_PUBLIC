package frc.robot.subsystems.Drivetrain;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;

class MotionProfiling extends Command {
	private final SwerveDrive swerveDrive;
	private final DoubleSupplier xSetpoint;
	private final DoubleSupplier ySetpoint;
	private final DoubleSupplier thetaSetpoint;
	private final Timer timer;
	private double totalDistance;
	private double initialX;
	private double initialY;
	private Rotation2d initialTheta;
	private double currentVelocity; //placeholder
	TrapezoidProfile.State setpointState;

	HolonomicDriveController holonomicDriveController;
	TrapezoidProfile motionProfile;

	MotionProfiling(SwerveDrive swerveDrive, DoubleSupplier xSetpoint, DoubleSupplier ySetpoint, DoubleSupplier thetaSetpoint) {
		this.swerveDrive = swerveDrive;
		this.thetaSetpoint = thetaSetpoint;
		this.xSetpoint = xSetpoint;
		this.ySetpoint = ySetpoint;

		holonomicDriveController = new HolonomicDriveController(new PIDController(1,0,0), new PIDController(1,0,0), 
			new ProfiledPIDController(1,0,0,
				new TrapezoidProfile.Constraints(5,10)));
		
		motionProfile = new TrapezoidProfile(new TrapezoidProfile.Constraints(5,10));
		timer = new Timer();
		}

	private double wrapTheta(double theta) {
		theta %= (Math.PI * 2);
		if (theta < 0) {
			theta += Math.PI;
		}
		return theta;
	} 

	private double calculateTotalDistance(){
		return Math.sqrt(
			Math.pow(xSetpoint.getAsDouble() - swerveDrive.swerveDriveOdometry.getPoseMeters().getX(),2) +
			Math.pow(ySetpoint.getAsDouble() - swerveDrive.swerveDriveOdometry.getPoseMeters().getY(),2));
	}

	private double calculateCurrentDistanceAlongTrajectory(){
		return totalDistance - Math.sqrt(
			Math.pow(swerveDrive.swerveDriveOdometry.getPoseMeters().getX() - initialX,2) +
			Math.pow(swerveDrive.swerveDriveOdometry.getPoseMeters().getY() - initialY,2));
	}

	@Override
	public void initialize() {
		double totalDistance = calculateTotalDistance();
		initialX = swerveDrive.swerveDriveOdometry.getPoseMeters().getX();
		initialY = swerveDrive.swerveDriveOdometry.getPoseMeters().getY();
		initialTheta = swerveDrive.swerveDriveOdometry.getPoseMeters().getRotation();
		new TrapezoidProfile.Constraints(5,10);
		setpointState = new TrapezoidProfile.State(totalDistance,0);
	}

	@Override
	public void execute() {
		double theta = wrapTheta(thetaSetpoint.getAsDouble());

		Translation2d coordSetpoint = new Translation2d(xSetpoint.getAsDouble(), ySetpoint.getAsDouble());
		Rotation2d headingSetpoint = new Rotation2d(thetaSetpoint.getAsDouble());

		Pose2d robotSetpoint = new Pose2d(coordSetpoint, headingSetpoint);

		State currentState = new State(
			calculateCurrentDistanceAlongTrajectory(),
			currentVelocity
		);

		State goalState = motionProfile.calculate(
			timer.getFPGATimestamp(),
			currentState,
			setpointState
		);

		Pose2d goalPose = new Pose2d(
			goalState.position * Math.cos(initialTheta.getRadians()),
			goalState.position * Math.sin(initialTheta.getRadians()),
			initialTheta
		);
		//use trig to find goalpose from goalstate

		ChassisSpeeds adjustedSpeeds = 
			holonomicDriveController.calculate(
				swerveDrive.swerveDriveOdometry.getPoseMeters(), 
				goalPose, 
				goalState.velocity,
				Rotation2d.fromDegrees(theta)
		);

		swerveDrive.autonDriveCommand(
			adjustedSpeeds.vxMetersPerSecond, 
			adjustedSpeeds.vyMetersPerSecond, 
			adjustedSpeeds.omegaRadiansPerSecond
		);

		//update PID
	}

	@Override
	public void end(boolean interrupted) {

	}

	@Override
	public boolean isFinished() {
		return true;
	}
}
