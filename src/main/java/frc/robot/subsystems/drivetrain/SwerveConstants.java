package frc.robot.subsystems.drivetrain;

public class SwerveConstants {
    public static final int 
        LF_DRIVE_MOTOR_PORT = 2,
        LB_DRIVE_MOTOR_PORT = 4,
        RF_DRIVE_MOTOR_PORT = 6,
        RB_DRIVE_MOTOR_PORT = 8;

    public static final int
        LF_STEER_MOTOR_PORT = 3,
        LB_STEER_MOTOR_PORT = 5, 
        RF_STEER_MOTOR_PORT = 7,
        RB_STEER_MOTOR_PORT = 9; 

    public static final int
        LF_ENCODER_PORT = 10,
        LB_ENCODER_PORT = 11,
        RB_ENCODER_PORT = 12,
        RF_ENCODER_PORT = 13;

    public static final double
        BL_OFFSET = -0.109863,
        BR_OFFSET = -0.033691,
        FL_OFFSET = -0.476074,
        FR_OFFSET = 0.072266;

    public static final double 
        TICKS_PER_WHEEL_ROTATION = 2048,
        WHEEL_CIRCUMFERENCE = 0.1016*Math.PI;

}
