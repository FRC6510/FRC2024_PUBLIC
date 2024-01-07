package frc.robot.subsystems.Drivetrain;

public class SwerveConstants {

    class moduleConstants{

        static final double
            rotational_kP = 0.011,
            rotational_kI = 0,
            rotational_kD = 0.000000001,

            GEAR_RATIO = 6.75, 
            WHEEL_RADIUS = 0.0508, // This is in metres
            WHEEL_CIRCUMFERENCE = 2*Math.PI*WHEEL_RADIUS,
            DISTANCE_PER_WHEEL_REV = WHEEL_CIRCUMFERENCE / GEAR_RATIO; // aka distance multiplier
    }

    class drivetrainMotorPorts{

        static final int 
            BR_DRIVE_MOTOR_PORT = 2,
            FR_DRIVE_MOTOR_PORT = 4,
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

    public class drivetrainMotorInfo{

        public static final double 
            BL_ROTATIONAL_OFFSET = 0.072266,
            BR_ROTATIONAL_OFFSET = -0.476074,
            FL_ROTATIONAL_OFFSET = -0.033691,
            FR_ROTATIONAL_OFFSET = -0.109863,

            FROM_CENTRE_X = 551.650 / 2,
            FROM_CENTRE_Y = 616.650 / 2,
            
            ROBOTMAXSPEED_DRIVE = 4.0,
            ROBOTMAXSPEED_STEER = 0.01;

        static final boolean
            BL_STEER_REVERSED = true,
            BL_DRIVE_REVERSED = false,
        
            BR_STEER_REVERSED = true,
            BR_DRIVE_REVERSED = false,
        
            FL_STEER_REVERSED = true,
            FL_DRIVE_REVERSED = true,
        
            FR_STEER_REVERSED = true,
            FR_DRIVE_REVERSED = true;
    }
   

}
