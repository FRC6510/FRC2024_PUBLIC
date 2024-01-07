package frc.robot.subsystems.Drivetrain;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class MotionProfiling extends Command {
	private final SwerveDrive swerveDrive;
	private double xGoalpoint;
	private double yGoalpoint;
	private final DoubleSupplier xSupplier, ySupplier;
	private final Supplier<Rotation2d> headingSupplier;
	public Pose2d robotGoalPoint;
	private Timer timer;
	// private double initialX;
	// private double initialY;
	private double speedTowardsGoal; //placeholder

	public Pose2d previousPosition;
	public Pose2d currentPosition;
	private double currentPositionScalar;
	private double currentHeading;
	// private double previousTime;
	private double currentTime;
	private boolean atGoalpoint;

	// private double K;// velocity filter constant 
	private double lookForwardTime = 0.02;

	TrapezoidProfile.State goalState;
	TrapezoidProfile.State previousState;

	TrapezoidProfile.State goalStateHeading;
	TrapezoidProfile.State previousStateHeading;

	HolonomicDriveController holonomicDriveController;
	TrapezoidProfile motionProfile;

	Translation2d targetCoord;
	Rotation2d thetaSetpoint; 

	/**
	 * Creates a new MotionProfiling object
	 * @param swerveDrive
	 * @param xGoalpoint in meters
	 * @param yGoalpoint in meters
	 * @param thetaSetpoint in radians
	 */
	MotionProfiling(SwerveDrive swerveDrive, DoubleSupplier xSupplier, DoubleSupplier ySupplier, Supplier<Rotation2d> headingSupplier) {
		this.swerveDrive = swerveDrive;
		this.xSupplier = xSupplier;
		this.ySupplier = ySupplier;
		this.headingSupplier = headingSupplier;

		holonomicDriveController = new HolonomicDriveController(new PIDController(0.5,0,0), new PIDController(0.5,0,0), 
			new ProfiledPIDController(2.25,0,0,
				new TrapezoidProfile.Constraints(4,8)));
		motionProfile = new TrapezoidProfile(new TrapezoidProfile.Constraints(
			SwerveConstants.drivetrainMotorInfo.ROBOTMAXVELOCITY_DRIVE,
			SwerveConstants.drivetrainMotorInfo.ROBOTMAXACCELERATION * 0.75));

		timer = new Timer();

		thetaSetpoint = headingSupplier.get();

		targetCoord = new Translation2d(xGoalpoint, yGoalpoint);// Defined in initialize
		robotGoalPoint = new Pose2d(targetCoord, thetaSetpoint);
		atGoalpoint = false;

		addRequirements(swerveDrive);

	}

	public MotionProfiling(SwerveDrive swerveDrive, Pose2d target){
		this(swerveDrive, () -> target.getX(), () -> target.getY(), () -> target.getRotation());
	}

	private double wrapTheta(double theta) {
		theta %= (Math.PI * 2);
		if (theta < 0) {
			theta += Math.PI;
		}
		return theta;
	} 
	
	/**
	 * 
	 * @return angle of the trajectory movement, in radians
	 */
	private double calculateTrajectoryAngle(){

		double yLength = yGoalpoint - SwerveDrive.poseEstimatorOdometry.getEstimatedPosition().getY();
		double xLength = xGoalpoint - SwerveDrive.poseEstimatorOdometry.getEstimatedPosition().getX();

		return Math.atan2(yLength, xLength);
	}

	/**
	 * 
	 * @return R
	 * you'll need to set the starting position to be (0,0) if you want this to work 
	 */
	private double calculateTotalDistance(){
		return Math.sqrt(
			Math.pow(xGoalpoint - SwerveDrive.poseEstimatorOdometry.getEstimatedPosition().getX(),2) +
			Math.pow(yGoalpoint - SwerveDrive.poseEstimatorOdometry.getEstimatedPosition().getY(),2));
	}

	private double calculateTotalHeading(){
		return SwerveDrive.poseEstimatorOdometry.getEstimatedPosition().getRotation().getDegrees();
	}

	public Translation2d getVelocityVector(){
		return (currentPosition.minus(previousPosition).getTranslation()).div(lookForwardTime);
	}

	public Translation2d getDisplacementVector(){
		return robotGoalPoint.minus(currentPosition).getTranslation();
	}

	/**
	 * 
	 * @return the magnitude of the distance between the current position and goal position (vector subtraction)
	 */
	public double getDisplacementMagnitude(Translation2d displacementVector){
		return Math.sqrt(
			Math.pow(displacementVector.getX(),2) +
			Math.pow(displacementVector.getY(),2));
	}

	/**
	 * 
	 * @param velocityVector current, true velocity of the robot 
	 * @param displacementVector displacement to goal position
	 * @return the portion of the velocity vector along the displacement vector
	 */
	public double getProjectionAlongDisplacement(Translation2d velocityVector, Translation2d displacementVector){
		return (velocityVector.getX() * displacementVector.getX() + velocityVector.getY() * displacementVector.getY()) 
		/ getDisplacementMagnitude(displacementVector);
	}

	@Override
	public void initialize() {
		timer.reset();
		timer.start();
		
		// initialX = swe rveDrive.swerveDriveOdometry.getPoseMeters().getX();
		// initialY = swerveDrive.swerveDriveOdometry.getPoseMeters().getY();

		xGoalpoint = xSupplier.getAsDouble();
		yGoalpoint = ySupplier.getAsDouble();

		previousPosition = SwerveDrive.poseEstimatorOdometry.getEstimatedPosition();
		// previousTime = timer.get();
		speedTowardsGoal = 0; 

		previousState = new State();
		previousState.velocity = 0;
		// K = 0.8;
	}

	@Override
	public void execute() {
		double totalDistance = calculateTotalDistance();
		double totalHeading = calculateTotalHeading();
		currentPositionScalar = 0;
		currentHeading = 0;
		currentPosition = SwerveDrive.poseEstimatorOdometry.getEstimatedPosition();
		goalState = new TrapezoidProfile.State(totalDistance - currentPositionScalar,0);// note that totalDistance changes with odometry. Trapezoidal profile takes in scalar quantities. 
		goalStateHeading = new TrapezoidProfile.State(totalHeading - currentHeading, 0);

		currentTime = Timer.getFPGATimestamp();
		// speedTowardsGoal = -getProjectionAlongDisplacement(getVelocityVector(), getDisplacementVector());
		speedTowardsGoal = previousState.velocity;

		State currentState = new State(
			currentPositionScalar,
			speedTowardsGoal
		);

		State currentStateHeading = new State(
			currentHeading,
			speedTowardsGoal
		);

		State setpointStateHeading = motionProfile.calculate(
			lookForwardTime, 
			currentStateHeading, 
			goalStateHeading);

		State setpointState = motionProfile.calculate(
			lookForwardTime,// look-forward time, the state expected at this time
			currentState,
			goalState
		);

		// setpointState.velocity = Math.abs(setpointState.velocity);

		Pose2d setpointPose = new Pose2d(
			SwerveDrive.poseEstimatorOdometry.getEstimatedPosition().getX() + setpointState.position * Math.cos(calculateTrajectoryAngle()),
			SwerveDrive.poseEstimatorOdometry.getEstimatedPosition().getY() + setpointState.position * Math.sin(calculateTrajectoryAngle()),
			new Rotation2d(calculateTrajectoryAngle()).plus(new Rotation2d(setpointStateHeading.position))
		);// You need to pass in the angle of your trajectory, not your target heading (setpointTheta) because... (see adjustedSpeeds)

		// Output of the holonomic drive controller
		 ChassisSpeeds adjustedSpeeds = 
			holonomicDriveController.calculate(
				SwerveDrive.poseEstimatorOdometry.getEstimatedPosition(), 
				setpointPose, 
				setpointState.velocity,
				robotGoalPoint.getRotation()
		);// This calculate method splits up your target velocity into x and y components, meaning you need to pass through the trajectory angle, not the target heading. 

		swerveDrive.setSwerveModuleStates(adjustedSpeeds);

		previousPosition = currentPosition;
		// previousTime = currentTime;
		previousState.velocity = setpointState.velocity;

		atGoalpoint = 
		Math.abs(thetaSetpoint.getRadians() - swerveDrive.getPose2d().getRotation().getRadians()) <= (Math.PI / 180) * 10 &&
		Math.abs(xGoalpoint - swerveDrive.getPose2d().getX()) <= 0.3 &&
		Math.abs(yGoalpoint - swerveDrive.getPose2d().getY()) <= 0.3;

		System.out.println("rotation component" + new Rotation2d(calculateTrajectoryAngle()).plus(new Rotation2d(setpointStateHeading.position)));

		SmartDashboard.putNumber("Total distance", calculateTotalDistance());
		SmartDashboard.putNumber("Current velocity", speedTowardsGoal);

		SmartDashboard.putBoolean("Auto running", true);

		// SmartDashboard.putNumber("Current time", currentTime);

		// System.out.println(
		// 	"Displacement mag" + totalDistance + 
		// 	" Velocity" + speedTowardsGoal);

		// System.out.println("x" + xGoalpoint + "y" + yGoalpoint + "theta" + thetaSetpoint);
		// System.out.println("Xr" + swerveDrive.getPose2d().getX() + "Ry" + swerveDrive.getPose2d().getY() + "Rtheta" + swerveDrive.getPose2d().getRotation().getRadians());


		// System.out.println(atGoalpoint);
		// System.out.println(Math.abs(thetaSetpoint.getRadians() - swerveDrive.getPose2d().getRotation().getRadians()));
		// System.out.println(Math.abs(xGoalpoint - swerveDrive.getPose2d().getX()));
		// System.out.println(Math.abs(yGoalpoint - swerveDrive.getPose2d().getY()));
	}

	@Override
	public void end(boolean interrupted) {
		swerveDrive.fieldCentricDrive(0, 0, 0);
		System.out.println("moveto finished");
	}

	@Override
	public boolean isFinished() {
		return atGoalpoint;

	}
}
