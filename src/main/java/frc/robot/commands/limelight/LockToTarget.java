package frc.robot.commands.limelight;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.shooter.ShooterLimelight;

public class LockToTarget extends Command{
  public boolean xOffsetZero = false;
  ShooterLimelight limelight;
  SwerveDrive swerveDrive;
  double setpoint;

  final PIDController pidController;

  //below is constructor --> does setup tasks for instances of commmands; parameters
  public LockToTarget(SwerveDrive swerveDrive, ShooterLimelight limelightShooter){
    this.limelight = limelightShooter;
    this.swerveDrive = swerveDrive;
    addRequirements(swerveDrive);
    this.pidController= new PIDController(0.000128, 0, 0);
  }
    
  public void initialize() {}

	public void execute() {
    // if(limelight.getTx()>0){
    //   setpoint = -5.56;
    // }

    // if(limelight.getTx()<0){
    //   setpoint = 5.56;
    // }
      setpoint = 0;

    double power = pidController.calculate(limelight.getTx(), setpoint);

    if(limelight.getTx() > -0.2 && limelight.getTx() < 0.2){
      swerveDrive.fieldCentricDrive(0, 0, 0);
      //xOffsetZero = true;   
    }

    else{
      swerveDrive.fieldCentricDrive(0, 0, power);
    }

    /*
    float KpAim = -0.1f;
    float min_aim_command = 0.05f;

    double heading_error = -limelight.getTx();
    double steering_adjust = 0.0;

    if (limelight.getTx()> 1.0){
      steering_adjust = KpAim*heading_error - min_aim_command;
    }

    else if(limelight.getTx() < -1.0){
      steering_adjust = KpAim*heading_error + min_aim_command;
    }

    if(limelight.getTx() == 0){
      swerveDrive.fieldCentricDrive(0, 0, 0);  
      xOffsetZero = true;   
    }

    else{
      swerveDrive.fieldCentricDrive(0, 0, -steering_adjust);
    }
    */

    //below code testing later

    // if(limelight.getTx() == 0){
    //   swerveDrive.fieldCentricDrive(0, 0, 0);
    // }
    // if (limelight.getTx() > 1){
    //   swerveDrive.fieldCentricDrive(0, 0, -Math.PI/20);
    // }
    // if (limelight.getTx() < 1){
    //   swerveDrive.fieldCentricDrive(0, 0, Math.PI/20);
    // }

    //math retrieves the sign --> either pos or neg for turning direction
    //abs for absolute value

    //SmartDashboard.putNumber("steering adjust", steering_adjust);

    SmartDashboard.putNumber("xOffset", Math.PI/20 * (limelight.getTx()/Math.abs(limelight.getTx())));
	}


	public void end(boolean interrupted) {
    System.out.println("ending");
	}

	public boolean isFinished() {
		return false;
	}
}