package frc.robot.subsystems.drivetrain;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.example.ExampleSimpleCommand;

public class SwerveDrive extends SubsystemBase{
    Pigeon2 pigeon = new Pigeon2(5, "CANivore");

    TalonFX driveMotor_FL = new TalonFX(SwerveConstants.LF_DRIVE_MOTOR_PORT, "CANivore");
    TalonFX steerMotor_FL = new TalonFX(SwerveConstants.LF_STEER_MOTOR_PORT, "CANivore");
    CANcoder encoder_FL = new CANcoder(SwerveConstants.LF_ENCODER_PORT, "CANivore");

    TalonFX driveMotor_FR = new TalonFX(SwerveConstants.RF_DRIVE_MOTOR_PORT, "CANivore");
    TalonFX steerMotor_FR = new TalonFX(SwerveConstants.RF_STEER_MOTOR_PORT, "CANivore");
    CANcoder encoder_FR = new CANcoder(SwerveConstants.RF_ENCODER_PORT, "CANivore");

    TalonFX driveMotor_BL = new TalonFX(SwerveConstants.LB_DRIVE_MOTOR_PORT, "CANivore");
    TalonFX steerMotor_BL = new TalonFX(SwerveConstants.LB_STEER_MOTOR_PORT, "CANivore");
    CANcoder encoder_BL = new CANcoder(SwerveConstants.LB_ENCODER_PORT, "CANivore");

    TalonFX driveMotor_BR = new TalonFX(SwerveConstants.RB_DRIVE_MOTOR_PORT, "CANivore");
    TalonFX steerMotor_BR = new TalonFX(SwerveConstants.RF_STEER_MOTOR_PORT, "CANivore");
    CANcoder encoder_BR = new CANcoder(SwerveConstants.RF_ENCODER_PORT, "CANivore");
    
    // TALL-E Locations for the swerve drive modules relative to the robot center.
    Translation2d frontLeftLocation = new Translation2d(0.315, 0.315);
    Translation2d frontRightLocation = new Translation2d(0.315, -0.315);
    Translation2d backLeftLocation = new Translation2d(-0.315, 0.315);
    Translation2d backRightLocation = new Translation2d(-0.315, -0.315);

    SwerveModuleState[] moduleStates;

    // Creating kinematics object using the module locations
    SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
        frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation
    );

    SwerveModule module_FL = new SwerveModule(driveMotor_FL, steerMotor_FL, encoder_FL);
    SwerveModule module_FR = new SwerveModule(driveMotor_FR, steerMotor_FR, encoder_FR);
    SwerveModule module_BL = new SwerveModule(driveMotor_BL, steerMotor_BL, encoder_BL);
    SwerveModule module_BR = new SwerveModule(driveMotor_BR, steerMotor_BR, encoder_BR);

    ChassisSpeeds speeds_FOD;

    Pose2d pose;

    SwerveDriveOdometry odometry = new SwerveDriveOdometry(
        kinematics, pigeon.getRotation2d(),
        new SwerveModulePosition[] {
            module_FL.getPosition(),
            module_FR.getPosition(),
            module_BL.getPosition(),
            module_BR.getPosition()
        }, new Pose2d(5.0, 13.5, new Rotation2d()));

    
    public Pigeon2 getpigeon(){
        return pigeon;
    }

    
    public void periodic() {        
        // Update the pose
        pose = odometry.update(pigeon.getRotation2d(),
            new SwerveModulePosition[] {
            module_FL.getPosition(), 
            module_FR.getPosition(),
            module_BL.getPosition(), 
            module_BR.getPosition()
            });
        }
          
    public void drive(double xSpeed, double ySpeed, double angleSpeed){
        speeds_FOD = ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, angleSpeed, pigeon.getRotation2d());
        moduleStates = kinematics.toSwerveModuleStates(speeds_FOD);
        module_FL.setDesiredState(moduleStates[0]);
        module_FR.setDesiredState(moduleStates[1]);
        module_BL.setDesiredState(moduleStates[2]);
        module_BR.setDesiredState(moduleStates[3]);
    }

    public Command driveCommand(DoubleSupplier xSupplier, DoubleSupplier ySupplier, DoubleSupplier thetaSupplier) {
		return new ManualDrive(this, xSupplier, ySupplier, thetaSupplier);
	}

}
