package frc.robot.subsystems.Drivetrain;

class SwerveConstants {

    class moduleConstants{

        static final double
            ANGULAR_kP = 0.011,// in terms of degrees 
            ANGULAR_kI = 0,
            ANGULAR_kD = 0.000000001,

            GEAR_RATIO = 6.12, 
            WHEEL_RADIUS = 0.0508, // This is in metres
            WHEEL_CIRCUMFERENCE = 2*Math.PI*WHEEL_RADIUS,
            DISTANCE_PER_MOTOR_REV = WHEEL_CIRCUMFERENCE / GEAR_RATIO; // aka distance multiplier
    }

    class drivetrainMotorPorts{

        static final int 
            BR_DRIVE_MOTOR_PORT = 4,
            FR_DRIVE_MOTOR_PORT = 2,
            BL_DRIVE_MOTOR_PORT = 6,
            FL_DRIVE_MOTOR_PORT = 8,
        
            BR_STEER_MOTOR_PORT = 3,
            FR_STEER_MOTOR_PORT = 5, 
            BL_STEER_MOTOR_PORT = 7,
            FL_STEER_MOTOR_PORT = 9,
        
            BR_ANGLE_ENCODER_PORT = 10,
            FR_ANGLE_ENCODER_PORT = 11,
            FL_ANGLE_ENCODER_PORT = 12,
            BL_ANGLE_ENCODER_PORT = 13;
    }

    class drivetrainMotorInfo{

        static final double 
            BL_ROTATIONAL_OFFSET = -0.184814,
            BR_ROTATIONAL_OFFSET = -0.4440912,
            FL_ROTATIONAL_OFFSET = 0.205566,
            FR_ROTATIONAL_OFFSET = -0.421143,

            FROM_CENTRE_X = 0.536650 / 2, 
            FROM_CENTRE_Y = 0.536650 / 2,
            
            ROBOTMAXVELOCITY_DRIVE = 5.89,
            ROBOTMAXACCELERATION = 8.0,
            ROBOTMAXVELOCITY_STEER = 2*Math.PI;

        static final boolean
            BL_STEER_REVERSED = true,
            BL_DRIVE_REVERSED = false,
        
            BR_STEER_REVERSED = true,
            BR_DRIVE_REVERSED = false,
        
            FL_STEER_REVERSED = true,
            FL_DRIVE_REVERSED = true,
        
            FR_STEER_REVERSED = true,
            FR_DRIVE_REVERSED = false;
    }

}

/*
 * recorded pos2d values
 * 5.300213109993658
 * 5.273109966206656
 * 5.3463996842941395
 * 5.375366065035504
 * 5.374396686067162
 */