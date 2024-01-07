// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Drivetrain;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Shooter.ShooterLimelight;

public class SwerveDrive extends SubsystemBase {

  private final SwerveModule 
    frontLeft,
    frontRight,
    backLeft,
    backRight;

  private final Pigeon2 imu;
  
  ManualDrive manualDrive;

  public double targetAngle;
  public double setpoint;

  private Pose2d estimatedPose = new Pose2d();

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
    private final SwerveDrivePoseEstimator poseEstimatorOdometry;
    private Pose2d newPose;
    private ShooterLimelight limelightShooter;

  /** Creates a new SwerveDrive. */
  public SwerveDrive(ShooterLimelight limelightShooter) {

    backLeft = new SwerveModule(
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

    this.limelightShooter = limelightShooter;
    swerveDriveKinematics = new SwerveDriveKinematics(modulePositionsRelativeToCentre);

    // swerveDriveOdometry = new SwerveDriveOdometry(
    //   swerveDriveKinematics, imu.getRotation2d(), 
    //   new SwerveModulePosition[] {
    //     frontRight.getPosition(),
    //     frontLeft.getPosition(),
    //     backRight.getPosition(),
    //     backLeft.getPosition()
    //   }, new Pose2d(0, 0, new Rotation2d())
    // );//TODO check with Ben if x and y values should be set to 0

    poseEstimatorOdometry = new SwerveDrivePoseEstimator(
      swerveDriveKinematics, 
      imu.getRotation2d(), 
      getCurrentModulePositions(), 
      new Pose2d(0, 0, new Rotation2d()), 
      VecBuilder.fill(0.1, 0.1, Math.toRadians(1)),
      VecBuilder.fill(2.5, 2.5, Math.toRadians(500)));
  
    
  
  }

  public void setSpeedLimit(double speedLimit){
    this.speedLimit = speedLimit;
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
    frontRight.setDriveModuleVelocity(SwerveModule.toRPS(velocity));
    frontLeft.setDriveModuleVelocity(SwerveModule.toRPS(velocity));
    backRight.setDriveModuleVelocity(SwerveModule.toRPS(velocity));
    backLeft.setDriveModuleVelocity(SwerveModule.toRPS(velocity));
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
    // Oscar: we need to rotate inputs for red again it appears
    // if (DriverStation.getAlliance().get() == Alliance.Red) {
    //   xVelocity = -xVelocity;
    //   yVelocity = -yVelocity;
    // }
    ChassisSpeeds speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
      xVelocity, yVelocity, thetaVelocity, imu.getRotation2d());

    setSwerveModuleStates(speeds);
  }

  public void robotCentricDrive(double xVelocity, double yVelocity, double thetaVelocity) {

    ChassisSpeeds speeds = new ChassisSpeeds(
      xVelocity, yVelocity, thetaVelocity);

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

  /**
   * 
   * @param xSupplier
   * @param ySupplier
   * @param heading
   * @return the driveParallel command 
   */
  public Command driveAngled(CommandXboxController driverController, DoubleSupplier xSupplier, DoubleSupplier ySupplier, Rotation2d headingRotation2d){
    return new SetSwerveAngle(driverController, this,  xSupplier, ySupplier, headingRotation2d);
  }

  /**
   * 
   * @param swerveDrive
   * @param x setpoint X
   * @param y setpoint Y
   * @param heading setpoint theta 
   * @return the MotionProfiling command, moving and turning
   */
  public Command moveAndTurn(double x, double y, Rotation2d heading){
    return new MotionProfiling(this, ()-> x, () -> y, () -> heading);
  }

  public Command moveAndTurnCurve(double x, double y, double targetVelocity, Rotation2d heading){
    return new MotionCurving(this, () -> x, () -> y, targetVelocity, () -> heading);
  }

  /**
   * 
   * @param heading setpoint theta
   * @return the MotionProfiling command; turning only
   */
  public Command turnTo(Rotation2d heading) { 
    return new MotionProfiling(this, () -> estimatedPose.getX(), () -> estimatedPose.getY(), () -> heading);
  }

  /**
   * 
   * @param x setpoint X, field Y
   * @param y setpoint Y, field X
   * @return the MotionProfiling command; moving only
   */
  public Command moveTo(double x, double y) { 
    return new MotionProfiling(this, () -> x, () -> y, () -> estimatedPose.getRotation());
  }

  // public void updateOdometry(){
  //   swerveDriveOdometry.update(imu.getRotation2d(), 
  //   new SwerveModulePosition[]{
  //     frontRight.getPosition(),
  //     frontLeft.getPosition(),
  //     backRight.getPosition(),
  //     backLeft.getPosition()
  //   });
  // }

  public void updatePoseEstimator(){
    poseEstimatorOdometry.updateWithTime(Timer.getFPGATimestamp(),
      imu.getRotation2d(), 
      getCurrentModulePositions());
  }

  public void modulesPeriodic(){// TODO Perhaps consider making an array for the modules and using a for loop
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
    imu.setYaw(0);
    newPose = new Pose2d(getPose2d().getTranslation(), Rotation2d.fromDegrees(0));
    poseEstimatorOdometry.resetPosition(imu.getRotation2d(), getCurrentModulePositions(), newPose);
  }

  public SwerveModulePosition[] getCurrentModulePositions(){
    return new SwerveModulePosition[] {
      frontRight.getPosition(),
      frontLeft.getPosition(),
      backRight.getPosition(),
      backLeft.getPosition()
    };
  }

  public Pose2d getPose2d(){
    return estimatedPose;
  }

  public Pigeon2 getIMU(){
    return imu;
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

  public void setRobotPosition(Pose2d currentRobotPosition){
    setYaw(currentRobotPosition.getRotation().getDegrees());
    poseEstimatorOdometry.resetPosition(
    getIMU().getRotation2d(), 
    getCurrentModulePositions(), 
    currentRobotPosition);
  }

  public void setYaw(double degrees){
    imu.setYaw(-degrees);
  }

  @Override
  public void periodic() {

    // updateOdometry();

    updatePoseEstimator();

    modulesPeriodic();
    SmartDashboard.putString("Alliance", DriverStation.getAlliance().get().toString());


    double tl = limelightShooter.getTable().getEntry("tl").getDouble(0);
    double cl = limelightShooter.getTable().getEntry("cl").getDouble(0);
    double difference = (tl / 1000) + (cl / 1000);

    if(limelightShooter.tagCount() >= 2 && DriverStation.isTeleopEnabled()){
      if (DriverStation.getAlliance().get() == Alliance.Red) {
        Pose2d botPosetwoD = limelightShooter.getBotPose().toPose2d();
        Pose2d redPose = new Pose2d(-botPosetwoD.getX(), -botPosetwoD.getY(), imu.getRotation2d());
        poseEstimatorOdometry.addVisionMeasurement(redPose, Timer.getFPGATimestamp() - difference);
      }
      else {
        poseEstimatorOdometry.addVisionMeasurement(limelightShooter.getBotPose().toPose2d(), Timer.getFPGATimestamp() - difference);
      }
    }
     
    estimatedPose = poseEstimatorOdometry.getEstimatedPosition();

    SmartDashboard.putNumber("estimated x", estimatedPose.getX());
    SmartDashboard.putNumber("estimated y", estimatedPose.getY());
    SmartDashboard.putNumber("estimated yaw (rotation)", estimatedPose.getRotation().getDegrees());

    SmartDashboard.putNumber("imu angle", imu.getAngle());

    SmartDashboard.putNumber("FR velocity", frontRight.getState().speedMetersPerSecond);
    SmartDashboard.putNumber("FL velocity", frontLeft.getState().speedMetersPerSecond);
    SmartDashboard.putNumber("BR velocity", backRight.getState().speedMetersPerSecond);
    SmartDashboard.putNumber("BL velocity", backLeft.getState().speedMetersPerSecond);

    SmartDashboard.putNumber("FR angle", SwerveModule.toDegrees(frontRight.getAbsolutePosition()));
    SmartDashboard.putNumber("FL angle", SwerveModule.toDegrees(frontLeft.getAbsolutePosition()));
    SmartDashboard.putNumber("BR angle", SwerveModule.toDegrees(backRight.getAbsolutePosition()));
    SmartDashboard.putNumber("BL angle", SwerveModule.toDegrees(backLeft.getAbsolutePosition()));

  }

  public void resetPosition(Rotation2d rotation2d, SwerveModulePosition[] currentModulePositions, Pose2d pose2d) {
    poseEstimatorOdometry.resetPosition(
      rotation2d, 
      currentModulePositions, 
      pose2d);  
    }
}