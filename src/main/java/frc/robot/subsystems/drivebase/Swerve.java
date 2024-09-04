package frc.robot.subsystems.drivebase;

import java.io.IOException;
import java.util.function.DoubleSupplier;

import org.json.simple.parser.ParseException;

import com.ctre.phoenix6.hardware.Pigeon2;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public final class Swerve extends SubsystemBase {
	//don't use the heading of the imu, use the heading of the pose except using it to feed the odometry
	private final Pigeon2 imu = new Pigeon2(5, "CANivore");

	public static final double MAX_DRIVE_VELOCITY = 5.89,
							   MAX_DRIVE_ACCELERATION = 8.0,
							   MAX_ANGULAR_VELOCITY = 2*Math.PI;

	private double speedLimit = 1.0;

	private static final Translation2d[] MODULE_TRANSLATIONS = {
		new Translation2d(0.536650 / 2, -0.536650 / 2),
		new Translation2d(0.536650 / 2, 0.536650 / 2),
		new Translation2d(-0.536650 / 2, 0.536650 / 2),
		new Translation2d(-0.536650 / 2, -0.536650 / 2)
	};

	private final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
		MODULE_TRANSLATIONS
	);

	private Pose2d pose = new Pose2d();
	private final SwerveDrivePoseEstimator poseEstimator = new SwerveDrivePoseEstimator(
		kinematics,
		imu.getRotation2d(),
		Module.getPositions(),
		pose
	);

	private Swerve() {
		setName("Swerve");
		//setDefaultCommand(null);
	}

	public static final Swerve INSTANCE = new Swerve();

	private static void setTargetState(ChassisSpeeds chassisSpeeds) {
		SwerveModuleState[] states = INSTANCE.kinematics.toSwerveModuleStates(chassisSpeeds);
		for (int i = 0; i < states.length; i++) {
			Module.values()[i].setTargetState(states[i]);
		}
	}

	{
		RobotConfig config;
		try {
			config = RobotConfig.fromGUISettings();
		} catch (IOException | ParseException e) {
			throw new RuntimeException(e);
		}

		AutoBuilder.configure(
			Swerve::getPose, // Robot pose supplier
			Swerve::setPose, // Method to reset odometry (will be called if your auto has a starting pose)
			Swerve::getChassisSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
			(speeds, feedforwards) -> robotCentric(speeds), // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds. Also optionally outputs individual module feedforwards
			new PPHolonomicDriveController( // PPHolonomicController is the built in path following controller for holonomic drive trains
				new PIDConstants(0.0262, 0.0, 0.0), // Translation PID constants
				new PIDConstants(0.011, 0.0, 0.000000001) // Rotation PID constants
				),
			config, // The robot configuration
			() -> {
				// Boolean supplier that controls when the path will be mirrored for the red alliance
				// This will flip the path being followed to the red side of the field.
				// THE ORIGIN WILL REMAIN ON THE BLUE SIDE

				var alliance = DriverStation.getAlliance();
				if (alliance.isPresent()) {
					return alliance.get() == DriverStation.Alliance.Blue;
				}
				return false;
			},
			this // Reference to this subsystem to set requirements
		);
	}

	@Override
	public void periodic() {
		pose = poseEstimator.update(
			getImu().getRotation2d(),
			Module.getPositions()
		);

		Pose2d detectedPose = Limelight.getRobotPosition();
		double distance = pose.getTranslation().getDistance(detectedPose.getTranslation());

		if(Limelight.getTagCount() >= 2 && distance <= 1.0){
			poseEstimator.addVisionMeasurement(Limelight.getRobotPosition(), Timer.getFPGATimestamp() - Limelight.getLatency());
		}

		for (Module module : Module.values()) {
			module.update();
		}

		SmartDashboard.putString("robotpose", getPose().toString());
	}

	private static Pigeon2 getImu(){
		return INSTANCE.imu;
	}

	static void stop() {
		setTargetState(new ChassisSpeeds(0.0, 0.0, 0.0));
	}

	//field centric drive
	static void drive(double xVelocity, double yVelocity, double thetaVelocity) {
		setTargetState(ChassisSpeeds.fromFieldRelativeSpeeds(xVelocity * MAX_DRIVE_VELOCITY, yVelocity * MAX_DRIVE_VELOCITY, thetaVelocity * MAX_ANGULAR_VELOCITY, getPose().getRotation()));
	}

	//robot centric drive
	static void robotCentric(double xVelocity, double yVelocity, double thetaVelocity){
		ChassisSpeeds speeds = new ChassisSpeeds(
			xVelocity, yVelocity, thetaVelocity
		);
		robotCentric(speeds);
	}

	static ChassisSpeeds getChassisSpeeds(){
		return INSTANCE.kinematics.toChassisSpeeds(Module.getStates());
	}

	static void robotCentric(ChassisSpeeds speeds){
		setTargetState(speeds);
	}

	public static Command drive(DoubleSupplier xSupplier, DoubleSupplier ySupplier, DoubleSupplier thetaSupplier){
		return new Drive(xSupplier, ySupplier, thetaSupplier);
	}

	public static Command lockToHeading(DoubleSupplier xSupplier, DoubleSupplier ySupplier){
		return new LockToHeading(xSupplier, ySupplier);
	}

	public static void setSpeedLimit(double speedLimit){
		INSTANCE.speedLimit = speedLimit;
	}

	public static double getSpeedLimit(){
		return INSTANCE.speedLimit;
	}

	public static Pose2d getPose() {
		return INSTANCE.pose;
	}

	public static void setPose(Pose2d pose) {
		INSTANCE.poseEstimator.resetPosition(
			getImu().getRotation2d(),
			Module.getPositions(),
			pose
		);
	}

	public static void setPose() {
		setPose(new Pose2d());
	}

	public static double deadzone(double in, double deadzone){
		double magnitude = Math.abs(in);
		if (magnitude < deadzone) {
			return 0;
		}
		double multiplier = 1 / (1 - deadzone);
		return multiplier * (magnitude - deadzone) * Math.signum(in);
	}
}
