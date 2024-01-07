package frc.robot.subsystems.Drivetrain;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.units.Unit;
import edu.wpi.first.units.Velocity;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The purposes of this class are to: control the angle of the wheel, control the speed of the wheel, and do the maths to convert from Falcon velocity measurements and PID readings into tangential velocity of the swerve module. 
 */
public class SwerveModule {
  private final TalonFX driveMotor; // use the in-built PID controller 
  private final TalonFX steerMotor; // manually control power set to motor using the CANcoder as our sensor 

  private final PIDController turningPidController;

  private final CANcoder absoluteEncoder;
  private final boolean absoluteEncoderReversed;
  private final double absoluteEncoderOffsetRad;

  private static double ANGLE_OUTPUT;

  private DutyCycleOut driveRequest;
  private DutyCycleOut steerRequest;

  private final VelocityDutyCycle driveVelocityRequest;

  public SwerveModule (int driveMotorId, int steerMotorId, int absoluteEncoderId, boolean driveMotorReversed, boolean steerMotorReversed, double absoluteEncoderOffset, boolean absoluteEncoderReversed){
    this.absoluteEncoderOffsetRad = absoluteEncoderOffset;
    this.absoluteEncoderReversed = absoluteEncoderReversed;

    driveMotor = new TalonFX(driveMotorId, "CANivore");
    steerMotor = new TalonFX(steerMotorId, "CANivore");

    driveMotor.setNeutralMode(NeutralModeValue.Brake);

    driveMotor.setInverted(driveMotorReversed);
    steerMotor.setInverted(steerMotorReversed);

    absoluteEncoder = new CANcoder(absoluteEncoderId, "CANivore");

    CANcoderConfiguration CANcoderConfig = new CANcoderConfiguration();
    CANcoderConfig.MagnetSensor.MagnetOffset = absoluteEncoderOffset;
    absoluteEncoder.getConfigurator().apply(CANcoderConfig);

    turningPidController = new PIDController(
      SwerveConstants.moduleConstants.rotational_kP, 
      SwerveConstants.moduleConstants.rotational_kI, 
      SwerveConstants.moduleConstants.rotational_kD);
    turningPidController.enableContinuousInput(-180, 180);

    driveRequest = new DutyCycleOut(0.0);
    steerRequest = new DutyCycleOut(0.0);
    driveVelocityRequest = new VelocityDutyCycle(0.0);

    var slot0Configs = new Slot0Configs();
    slot0Configs.kS = 0;
    slot0Configs.kV = 0.000429;
    slot0Configs.kP = 0.00000015;
    slot0Configs.kI = 0;
    slot0Configs.kD = 0;
    driveMotor.getConfigurator().apply(slot0Configs, 0.050);
  }

  public void updatePID(){
    ANGLE_OUTPUT = turningPidController.calculate(absoluteEncoder.getAbsolutePosition().getValueAsDouble());
    steerMotor.set(ANGLE_OUTPUT);
    //make it actually change speeds
  }

  public double getAbsolutePosition(){
    return absoluteEncoder.getAbsolutePosition().getValueAsDouble();
  }

  public double getModuleAngle(){
    return toDegrees(absoluteEncoder.getAbsolutePosition().getValueAsDouble());
  }

  /**
  * Returns the current state of the module.
  *
  * @return The current state of the module.
  */
  public SwerveModuleState getState() {
    return new SwerveModuleState(
    driveMotor.getVelocity().getValueAsDouble(), 
    Rotation2d.fromDegrees(absoluteEncoder.getAbsolutePosition().getValueAsDouble()));
  }

  public SwerveModulePosition getPosition(){
    return new SwerveModulePosition(
      driveMotor.getPosition().getValueAsDouble() * SwerveConstants.moduleConstants.DISTANCE_MULTIPLIER, 
      Rotation2d.fromRotations(absoluteEncoder.getAbsolutePosition().getValueAsDouble()));

  }


  /**
   * Sets the desired swerveModuleState to the drive and steer motors. The target angle is set in the setModuleAngle method. This
   * puts the target angle through the turningPidController, which calculates the 'distance' between its current angle and the target angle, 
   * before outputting a power for the steer motor. 
   * @param desiredState
   */
  public void setDesiredState(SwerveModuleState desiredState){
    desiredState = SwerveModuleState.optimize(desiredState, Rotation2d.fromRotations(absoluteEncoder.getAbsolutePosition().getValueAsDouble()));
    turningPidController.setSetpoint(desiredState.angle.getDegrees());
    setModuleAngle(desiredState.angle.getDegrees());
    driveMotor.set(desiredState.speedMetersPerSecond / 3.5);// check this constant (3.5)
  }

  public void setModuleVelocity(double rps){
    driveVelocityRequest.Velocity = rps;
    driveMotor.setControl(driveVelocityRequest); //0.0017874598502600196
  }

  public void setModuleAngle(double angleDegrees){
    var controllerOutput = turningPidController.calculate(
      getModuleAngle(), angleDegrees); // This does the PIDing for you 
    steerMotor.set(controllerOutput);
  }


  public void stop(){
    driveMotor.set(0);
    steerMotor.set(0);
  }

  public static double toRPS(double tangentialVelocity){
    return tangentialVelocity / SwerveConstants.moduleConstants.WHEEL_TURN_PER_REV;
  }

  /**
   * v = rx , where v = velocity, r = radius of the wheel, x = change in theta (refer to Ben's diagram)
   *  @param rotationsPerSecond
   *  @return the velocity (tangential) of the wheel 
   * */

  public static double toTangentialVelocity(double rps){
    return rps * SwerveConstants.moduleConstants.WHEEL_TURN_PER_REV;
  }

  public static double toDegrees(double rotations){
    return 360 * rotations;
  }

  public double toRotations(double degrees){
    return degrees / 360;
  }

  /**
   * Sets the robot's heading
   * @param currentHeading - the robot's current heading (in degrees)
   * @param targetHeading - the robot's target heading (in degrees)
   * @return the amount the steer motors have to move, to get to the target position
   */
  private double findHeadingError(double currentHeading, double targetHeading) {
    currentHeading %= 360;
    targetHeading %= 360;

    if (currentHeading < 0) {
      currentHeading += 360;
    }

    if (targetHeading < 0) {
      targetHeading += 360;
    }
    
    double difference = currentHeading - targetHeading;
    if (difference > 180) {
      return (-360) + difference;
    } else if (difference < -180) {
      return 360 + difference;
    } else {
        return difference;
    }
  }
}


