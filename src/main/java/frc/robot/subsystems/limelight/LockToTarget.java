package frc.robot.subsystems.limelight;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain.SwerveDrive;

public class LockToTarget extends Command{
    final LimelightBack limelight;
    final SwerveDrive swerveDrive;

    final PIDController pidController;
    final PIDController getY;
    private final double TARGET_ANGLE = Math.atan2(0, 0);
    private final double TARGET_HYTP = Math.sqrt(0);

    //below is constructor --> does setup tasks for instances of commmands; parameters

    public LockToTarget(LimelightBack limelight, SwerveDrive swerveDrive){
        this.limelight = limelight;
        this.swerveDrive = swerveDrive;
        addRequirements(swerveDrive);
        //find kp, ki, kd later when testing
        //always want p term, normally d --> i and d not together
        //d typically is for acceleration, if moving towards target too fast, increase d to decrease speed, opposite
        // incrase d to dampen, soften the slow, kd makes graph line cubic and curved
        //calculate runs method to find kp, ki and kd
        // measurement is current position, setpoint is target
        //can use power
        //when use in code no need if, pid calculates
        this.pidController = new PIDController(0.1, 0, 0);
        this.getY = new PIDController(0.1, 0, 0);
    }
    
    public void initialize() {
	}

    /**
     * 
     */
	public void execute() {
    //pidController.calculate(limelight.getTx(), 0);
    //uses (current, 0) --> make (vanishing, current)
    //angle of current position, hypotenuse of current position

    //below gives commands in terms of robot centric
    

        double c_angle = Math.atan2(limelight.getTy(), limelight.getTx());
        double c_hypt = Math.sqrt(limelight.getTy() * limelight.getTy() + limelight.getTx() * limelight.getTx());

        //find target angle and hypotenuse when crosshair is set to the vanishing point of the note
        //place note in ideal position
        //find values of tx and ty in terms of the vanishing crosshair
        //sub values into BELOW

        double power = pidController.calculate(c_angle, TARGET_ANGLE);

        double forward = - getY.calculate(c_hypt, TARGET_HYTP);


        //rotation 2d is angle theta in either radians or degrees
        // below changes above code to become field centric
        Rotation2d turnOffset = Rotation2d.fromDegrees(-limelight.getTx());
        Rotation2d theta = swerveDrive.swerveDriveOdometry.getPoseMeters().getRotation().plus(turnOffset);

        double x = forward * theta.getCos();

        double y = forward * theta.getSin();

        swerveDrive.fieldCentricDrive(x, y, -power);

        SmartDashboard.putNumber("xOffset", Math.PI/20 * (limelight.getTx()/Math.abs(limelight.getTx())));

        SmartDashboard.putNumber("forward", forward);
	}


	public void end(boolean interrupted) {
    System.out.println("ending");
	}

	public boolean isFinished() {
		return false;
	}
}

