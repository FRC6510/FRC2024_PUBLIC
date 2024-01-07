package frc.robot.subsystems.Drivetrain;

class SwerveConstants {

    class moduleConstants{

        static final double
            rotational_kP = 0.011,
            rotational_kI = 0,
            rotational_kD = 0.000000001,

            TALONFX_TICKS_PER_ROTATION = 2048,
            GEAR_RATIO = 6.75, 
            WHEEL_RADIUS = 0.0508, // This is in metres
            WHEEL_CIRCUMFERENCE = 2*Math.PI*WHEEL_RADIUS,
            TICKS_PER_WHEEL_ROTATION = TALONFX_TICKS_PER_ROTATION * GEAR_RATIO,
            WHEEL_TURN_PER_REV = WHEEL_CIRCUMFERENCE / GEAR_RATIO,
            DISTANCE_MULTIPLIER = WHEEL_CIRCUMFERENCE / TICKS_PER_WHEEL_ROTATION;

    }

    class drivetrainMotorPorts{

        static final int 
            FL_DRIVE_MOTOR_PORT = 2,
            BL_DRIVE_MOTOR_PORT = 4,
            FR_DRIVE_MOTOR_PORT = 6,
            BR_DRIVE_MOTOR_PORT = 8,
        
            FL_STEER_MOTOR_PORT = 3,
            BL_STEER_MOTOR_PORT = 5, 
            FR_STEER_MOTOR_PORT = 7,
            BR_STEER_MOTOR_PORT = 9,
        
            FL_ANGLE_ENCODER_PORT = 10,
            BL_ANGLE_ENCODER_PORT = 11,
            BR_ANGLE_ENCODER_PORT = 12,
            FR_ANGLE_ENCODER_PORT = 13;

    }

    class drivetrainMotorInfo{

        static final double 
            FR_ROTATIONAL_OFFSET = 0.072266,
            FL_ROTATIONAL_OFFSET = -0.476074,
            BR_ROTATIONAL_OFFSET = -0.033691,
            BL_ROTATIONAL_OFFSET = -0.109863,
            
            ROBOTMAXSPEED = 4.0;

        static final boolean
            FR_STEER_REVERSED = true,
            FR_DRIVE_REVERSED = false,
        
            FL_STEER_REVERSED = true,
            FL_DRIVE_REVERSED = false,
        
            BR_STEER_REVERSED = true,
            BR_DRIVE_REVERSED = true,
        
            BL_STEER_REVERSED = true,
            BL_DRIVE_REVERSED = true;
    }
   

}
