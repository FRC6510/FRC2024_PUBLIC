package frc.robot.subsystems.drivetrain;

import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public class SwerveModule {
    TalonFX motorDrive;
    TalonFX motorSteer;
    CANcoder encoder;
    Pigeon2 pigeon;
    final DutyCycleOut driveMotorRequest = new DutyCycleOut(0.0);
    final DutyCycleOut steerMotorRequest = new DutyCycleOut(0.0);

    public  SwerveModule(TalonFX motorDrive, TalonFX motorSteer, CANcoder encoder){
        this.motorDrive = motorDrive;
        this.motorSteer = motorSteer;
        this.encoder = encoder;
    }

    public void setDesiredState(SwerveModuleState desiredModuleState){
        for(int i = 0; i < 4; i++){
            desiredModuleState = SwerveModuleState.optimize(desiredModuleState, pigeon.getRotation2d());
            driveMotorRequest.Output = desiredModuleState.speedMetersPerSecond;
            steerMotorRequest.Output = desiredModuleState.angle.getDegrees();
            motorDrive.setControl(driveMotorRequest);
            motorSteer.setControl(steerMotorRequest);
        }
    }

    public SwerveModulePosition getPosition(){
        return new SwerveModulePosition(
            motorDrive.getRotorPosition().getValueAsDouble() / (SwerveConstants.TICKS_PER_WHEEL_ROTATION * SwerveConstants.WHEEL_CIRCUMFERENCE), // multiply and divide
            Rotation2d.fromDegrees(encoder.getAbsolutePosition().getValueAsDouble())
            
        );
    }

}
