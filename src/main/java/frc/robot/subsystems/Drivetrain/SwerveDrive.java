// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Drivetrain;

import java.lang.reflect.Array;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveDriveWheelPositions;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveDrive extends SubsystemBase {

  private final SwerveModule 
    frontLeft,
    frontRight,
    backLeft,
    backRight;

  private final Pigeon2 imu;
  
  AutonDrive autonDrive;

  public double targetAngle;
  public double setpoint;

  public static double speedLimit = 1.0;

    // The locations for the modules must be relative to the center of the robot. Positive x values represent moving toward the front of the robot whereas positive y values represent moving toward the left of the robot.
    private final Translation2d
    frontLeftLocation = new Translation2d(SwerveConstants.drivetrainMotorInfo.FROM_CENTRE_X, SwerveConstants.drivetrainMotorInfo.FROM_CENTRE_Y),
    frontRightLocation = new Translation2d(SwerveConstants.drivetrainMotorInfo.FROM_CENTRE_X, -SwerveConstants.drivetrainMotorInfo.FROM_CENTRE_Y),
    backLeftLocation = new Translation2d(-SwerveConstants.drivetrainMotorInfo.FROM_CENTRE_X, SwerveConstants.drivetrainMotorInfo.FROM_CENTRE_Y),
    backRightLocation = new Translation2d(-SwerveConstants.drivetrainMotorInfo.FROM_CENTRE_X, -SwerveConstants.drivetrainMotorInfo.FROM_CENTRE_Y);

    Translation2d[] modulePositionsRelativeToCentre = {frontRightLocation, frontLeftLocation, backLeftLocation, backRightLocation}; // Unit circle declaration 

    public SwerveDriveKinematics swerveDriveKinematics;
    public SwerveDriveOdometry swerveDriveOdometry;

    // Pose2d pose2d = new Pose2d();
    
    // private boolean angleSet = false;

  /** Creates a new SwerveDrive. */
  public SwerveDrive() {

    backLeft =  new SwerveModule(
      SwerveConstants.drivetrainMotorPorts.BL_DRIVE_MOTOR_PORT,
      SwerveConstants.drivetrainMotorPorts.BL_STEER_MOTOR_PORT, 
      SwerveConstants.drivetrainMotorPorts.BL_ANGLE_ENCODER_PORT, 
      SwerveConstants.drivetrainMotorInfo.BL_DRIVE_REVERSED, 
      SwerveConstants.drivetrainMotorInfo.BL_STEER_REVERSED, 
      SwerveConstants.drivetrainMotorInfo.BL_ROTATIONAL_OFFSET, 
      false);

    backRight = new SwerveModule(
      SwerveConstants.drivetrainMotorPorts.BR_DRIVE_MOTOR_PORT, 
      SwerveConstants.drivetrainMotorPorts.BR_STEER_MOTOR_PORT, 
      SwerveConstants.drivetrainMotorPorts.BR_ANGLE_ENCODER_PORT, 
      SwerveConstants.drivetrainMotorInfo.BR_DRIVE_REVERSED, 
      SwerveConstants.drivetrainMotorInfo.BR_STEER_REVERSED, 
      SwerveConstants.drivetrainMotorInfo.BR_ROTATIONAL_OFFSET, 
      false);

    frontLeft = new SwerveModule(
      SwerveConstants.drivetrainMotorPorts.FL_DRIVE_MOTOR_PORT, 
      SwerveConstants.drivetrainMotorPorts.FL_STEER_MOTOR_PORT, 
      SwerveConstants.drivetrainMotorPorts.FL_ANGLE_ENCODER_PORT, 
      SwerveConstants.drivetrainMotorInfo.FL_DRIVE_REVERSED, 
      SwerveConstants.drivetrainMotorInfo.FL_STEER_REVERSED, 
      SwerveConstants.drivetrainMotorInfo.FL_ROTATIONAL_OFFSET, 
      false);

    frontRight = new SwerveModule(
      SwerveConstants.drivetrainMotorPorts.FR_DRIVE_MOTOR_PORT, 
      SwerveConstants.drivetrainMotorPorts.FR_STEER_MOTOR_PORT, 
      SwerveConstants.drivetrainMotorPorts.FR_ANGLE_ENCODER_PORT, 
      SwerveConstants.drivetrainMotorInfo.FR_DRIVE_REVERSED, 
      SwerveConstants.drivetrainMotorInfo.FR_STEER_REVERSED, 
      SwerveConstants.drivetrainMotorInfo.FR_ROTATIONAL_OFFSET, 
      false);

    imu = new Pigeon2(5, "CANivore"); 

    resetAllDrivePos();
    setStartingState();

    swerveDriveKinematics = new SwerveDriveKinematics(modulePositionsRelativeToCentre);

    swerveDriveOdometry = new SwerveDriveOdometry(
      swerveDriveKinematics, imu.getRotation2d(), 
      new SwerveModulePosition[] {
        frontRight.getPosition(),
        frontLeft.getPosition(),
        backRight.getPosition(),
        backLeft.getPosition()
      }, new Pose2d(0, 0, new Rotation2d())
    );//TODO check with Ben if x and y values should be set to 0

  }

  public void setSpeedLimit(double speedLimit){
    SwerveDrive.speedLimit = speedLimit;
  }

  public void setModAngle(double targetAngle){
    this.targetAngle = targetAngle;
    frontRight.setModuleAngle(targetAngle);
    frontLeft.setModuleAngle(targetAngle);
    backRight.setModuleAngle(targetAngle);
    backLeft.setModuleAngle(targetAngle);
  }

  public void setModVelocity(double velocity){
    this.setpoint = velocity;
    frontRight.setModuleVelocity(SwerveModule.toRPS(velocity));
    frontLeft.setModuleVelocity(SwerveModule.toRPS(velocity));
    backRight.setModuleVelocity(SwerveModule.toRPS(velocity));
    backLeft.setModuleVelocity(SwerveModule.toRPS(velocity));
  }

  /**
   * Takes in a set of chassisSpeeds, which are target velocities for the robot, and communicates them to each swerve module. 
   * This is done by making 2 Arrays: one for the swerve modules, another for swerveModuleStates, looping through the swerveModules array
   * and swerveModuleStates array, and setting each module's 'state'. 
   * @param chassisSpeeds - robot-centric values. Same for every module. 
   */
  public void setSwerveModuleStates(ChassisSpeeds chassisSpeeds){

    SwerveModule[] swerveModules = {frontRight, frontLeft, backLeft, backRight};

    // Converts universal ChassisSpeeds (for robot)containing linear and angular velocities to usable speeds for each individual type of drivetrain. 
    SwerveModuleState[] swerveModuleStates = swerveDriveKinematics.toSwerveModuleStates(chassisSpeeds);

    for (int i = 0; i < swerveModuleStates.length; i++){
      swerveModules[i].setDesiredState(swerveModuleStates[i]);
    }

  }

  /**
   * Takes in driver, field-relative values, and converts them to robot-centric values as chassis speeds. The chassis speeds are put through 
   * to the swerve modules, via the setSwerveModuleStates method. 
   * @param xVelocity - left Y on the driver controller
   * @param yVelocity - left X on the driver controller
   * @param thetaVelocity - angular velocity (right X on the driver controller)
   */
  public void fieldCentricDrive(double xVelocity, double yVelocity, double thetaVelocity) {

    ChassisSpeeds speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
      xVelocity, yVelocity,thetaVelocity, imu.getRotation2d());

    setSwerveModuleStates(speeds);

  }

  /**
   * @param xSupplier
   * @param ySupplier
   * @param thetaSupplier
   * @return the ManualDrive command 
   */
  public Command driveCommand(DoubleSupplier xSupplier, DoubleSupplier ySupplier, DoubleSupplier thetaSupplier) {// Vroom vroom
		return new ManualDrive(this, xSupplier, ySupplier, thetaSupplier);
  }

  public void updateOdometry(){
    swerveDriveOdometry.update(imu.getRotation2d(), 
    new SwerveModulePosition[]{
      frontRight.getPosition(),
      frontLeft.getPosition(),
      backRight.getPosition(),
      backLeft.getPosition()
    });
  }

  public void modulesPeriodic(){
    frontRight.swerveModulePeriodic();
    frontLeft.swerveModulePeriodic();
    backRight.swerveModulePeriodic();
    backLeft.swerveModulePeriodic();
  }

  public void stopModules(){
    frontRight.stop();
    frontLeft.stop();
    backRight.stop();
    backLeft.stop();
  }

  public void setStartingState(){
    frontRight.resetModule();
    frontLeft.resetModule();
    backRight.resetModule();
    backLeft.resetModule();
  }

  public void resetAllDrivePos(){
    frontRight.resetDrivePos();
    frontLeft.resetDrivePos();
    backRight.resetDrivePos();
    backLeft.resetDrivePos();
  }

  public void resetHeading(){
    imu.reset();
  }

  public SwerveModulePosition[] getCurrentModulePositions(){
    return new SwerveModulePosition[] {
      frontRight.getPosition(),
      frontLeft.getPosition(),
      backRight.getPosition(),
      backLeft.getPosition()
    };
  }

  public Pigeon2 getIMU(){
    return imu;
  }

  public DoubleSupplier getHeading(){
    return new DoubleSupplier() {
      public double getAsDouble(){
        return imu.getYaw().getValueAsDouble();
      }
    };
  }

  public double getAverageModuleVelocity(){
    return (
      frontRight.getState().speedMetersPerSecond + 
      frontLeft.getState().speedMetersPerSecond + 
      backRight.getState().speedMetersPerSecond + 
      backLeft.getState().speedMetersPerSecond) / 4;
  }

  public double getAverageModuleAngle(){
    return (
      SwerveModule.toDegrees(frontRight.getState().angle.getDegrees()) + 
      SwerveModule.toDegrees(frontLeft.getState().angle.getDegrees()) + 
      SwerveModule.toDegrees(backRight.getState().angle.getDegrees()) + 
      SwerveModule.toDegrees(backLeft.getState().angle.getDegrees())
    ) / 4;
  }

  public Command autonDriveCommand(double xSetpoint, double ySetpoint, double thetaSetpoint) {
		return new AutonDrive(this, () -> xSetpoint, () -> ySetpoint, () -> thetaSetpoint);
	}

  @Override
  public void periodic() {

    updateOdometry();

    modulesPeriodic();

    // This method will be called once per scheduler run

    // SmartDashboard.putNumber("Target angle", targetAngle);
    // SmartDashboard.putNumber("Tangential velocity target", SwerveModule.toTangentialVelocity(setpoint));
    // SmartDashboard.putNumber("Average voltage", getAverageModuleVoltage());
    // SmartDashboard.putNumber("Average module velocity", getAverageModuleVelocity());
    // SmartDashboard.putNumber("Average module angle", getAverageModuleAngle());

    SmartDashboard.putNumber("FR mod angle", SwerveModule.toDegrees(frontRight.getAbsolutePosition()));
    SmartDashboard.putNumber("FR abs pos", SwerveModule.toDegrees(frontRight.getState().angle.getDegrees()));
    SmartDashboard.putNumber("FR velocity", frontRight.getState().speedMetersPerSecond);


    SmartDashboard.putNumber("FL mod angle", SwerveModule.toDegrees(frontLeft.getAbsolutePosition()));
    SmartDashboard.putNumber("FL abs pos", SwerveModule.toDegrees(frontLeft.getState().angle.getDegrees()));
    SmartDashboard.putNumber("FL velocity", frontLeft.getState().speedMetersPerSecond);


    SmartDashboard.putNumber("BR mod angle", SwerveModule.toDegrees(backRight.getAbsolutePosition()));
    SmartDashboard.putNumber("BR abs pos", SwerveModule.toDegrees(backRight.getState().angle.getDegrees()));
    SmartDashboard.putNumber("BR velocity", backRight.getState().speedMetersPerSecond);


    SmartDashboard.putNumber("BL mod angle", SwerveModule.toDegrees(backLeft.getAbsolutePosition()));
    SmartDashboard.putNumber("BL abs pos", SwerveModule.toDegrees(backLeft.getState().angle.getDegrees()));
    SmartDashboard.putNumber("BL velocity", backLeft.getState().speedMetersPerSecond);

    SmartDashboard.putNumber("Pose X",swerveDriveOdometry.getPoseMeters().getX());
    SmartDashboard.putNumber("Pose Y",swerveDriveOdometry.getPoseMeters().getY());
    SmartDashboard.putNumber("Pose degrees",swerveDriveOdometry.getPoseMeters().getRotation().getDegrees());


  }
}
