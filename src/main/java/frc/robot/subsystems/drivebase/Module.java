package frc.robot.subsystems.drivebase;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import com.ctre.phoenix6.configs.SlotConfigs;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

enum Module {

	FRONT_RIGHT(5, 2, 11, -0.421143, SensorDirectionValue.CounterClockwise_Positive, false, true),
	FRONT_LEFT(9, 8, 12, -107.5/360+0.5, SensorDirectionValue.CounterClockwise_Positive, true, true),
	BACK_LEFT(7, 6, 13, -0.184814, SensorDirectionValue.CounterClockwise_Positive, false, true),
	BACK_RIGHT(3, 4, 10, -0.4440912, SensorDirectionValue.CounterClockwise_Positive, false, true);

	private final TalonFX angleMotor;
	private final TalonFX driveMotor;
	private final CANcoder encoder;
	private SwerveModuleState targetState;
	public final PIDController anglePIDController;
	private final VelocityDutyCycle driveRequest;
	private final DutyCycleOut angleRequest;
	private final boolean driveMotorReversed = false;
	private final boolean steerMotorReversed = false;

	public static final double GEAR_RATIO = 6.12,
							   WHEEL_RADIUS = 0.0508, // This is in metres
							   WHEEL_CIRCUMFERENCE = 2*Math.PI*WHEEL_RADIUS,
							   DISTANCE_PER_MOTOR_REV = WHEEL_CIRCUMFERENCE / GEAR_RATIO; // aka distance multiplier


	private Module(final int angleID, int driveID, final int encoderID, final double encoderOffset, final SensorDirectionValue encoderDirection, boolean driveMotorReversed, boolean steerMotorReversed) {
		this.angleMotor = new TalonFX(angleID, "CANivore");
		this.anglePIDController = new PIDController(0.011, 0.0, 0.000000001);
		anglePIDController.enableContinuousInput(-180, 180);

		this.driveMotor = new TalonFX(driveID, "CANivore");
		driveMotor.setNeutralMode(NeutralModeValue.Brake);
		driveMotor.getConfigurator().apply(
			new SlotConfigs()
				.withKP(0.02465)
				.withKI(0.0)
				.withKD(0.0)
				.withKV(0.01)
		);

		this.encoder = new CANcoder(encoderID, "CANivore");
		encoder.getConfigurator().apply(
			new MagnetSensorConfigs()
				.withMagnetOffset(encoderOffset)
				.withAbsoluteSensorRange(AbsoluteSensorRangeValue.Signed_PlusMinusHalf)
				.withSensorDirection(encoderDirection)
		);

		driveMotor.setInverted(driveMotorReversed);
		angleMotor.setInverted(steerMotorReversed);		

		targetState = new SwerveModuleState();
		driveRequest = new VelocityDutyCycle(0.0);
		angleRequest = new DutyCycleOut(0.0);
	}

	public void update() {
		angleMotor.setControl(
			angleRequest.withOutput(
				anglePIDController.calculate(getAngle().getDegrees())
			)
		);
	}

	public void setTargetState(final SwerveModuleState targetState) {
		if(this.targetState.equals(targetState)) return;

		this.targetState = SwerveModuleState.optimize(targetState, getAngle());
		this.targetState.speedMetersPerSecond *= this.targetState.angle.minus(getAngle()).getCos();
		setDriveModuleVelocity(this.targetState.speedMetersPerSecond);
		setModuleAngle(this.targetState.angle.getDegrees());
	}

	public Rotation2d getAngle() {
		return Rotation2d.fromRotations(encoder.getAbsolutePosition().getValueAsDouble());
	}

	public void setDriveModuleVelocity(double velocity){
		driveRequest.Velocity = velocity/DISTANCE_PER_MOTOR_REV;
		driveMotor.setControl(driveRequest);
	}

	public void setModuleAngle(double angleDegrees){
		anglePIDController.setSetpoint(angleDegrees);
	}

	public SwerveModuleState getState(){
		return new SwerveModuleState(
			driveMotor.getVelocity().getValueAsDouble() * DISTANCE_PER_MOTOR_REV,
			getAngle()
		);
	}

	public static SwerveModuleState[] getStates(){
		var states = new SwerveModuleState[Module.values().length];
		for (int i = 0; i < states.length; i++) {
			states[i] = Module.values()[i].getState();
		}
		return states;
	}

	public SwerveModulePosition getPosition() {
		return new SwerveModulePosition(
				driveMotor.getPosition().getValueAsDouble() * DISTANCE_PER_MOTOR_REV,
				getAngle()
		);
	}

	public static SwerveModulePosition[] getPositions() {
		var positions = new SwerveModulePosition[Module.values().length];
		for (int i = 0; i < positions.length; i++) {
			positions[i] = Module.values()[i].getPosition();
		}
		return positions;
	
	}
}
