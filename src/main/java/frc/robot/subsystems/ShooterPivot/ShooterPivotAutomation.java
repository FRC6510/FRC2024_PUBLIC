// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.ShooterPivot;

import java.util.function.DoubleSupplier;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Shooter.ShooterLimelight;
import frc.robot.subsystems.Drivetrain.SwerveDrive;
import frc.robot.subsystems.Drivetrain.SwerveDrive;

public class ShooterPivotAutomation extends Command {
  private final ShooterPivot shooterPivot;
  private double maxSetpoint = 25.40;
  private double minSetpoint = 0;
  private double distanceFromSpeaker;
  private boolean shouldFinish;
  private double manualPos;
  private CommandXboxController operatorController;
  /** Creates a new ShooterPivotAutomation. */
  public ShooterPivotAutomation(ShooterPivot shooterPivot, double manualPos) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.shooterPivot = shooterPivot;
    this.manualPos = manualPos;
    addRequirements(shooterPivot);

    operatorController = new CommandXboxController(1);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    shouldFinish = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-shooter");
		// NetworkTableEntry ty = table.getEntry("ty");
		// double targetOffsetAngle_Vertical = ty.getDouble(0.0);

		// // how many degrees back is your limelight rotated from perfectly vertical?
		// double limelightMountAngleDegrees = 33.0; 

		// // distance from the center of the Limelight lens to the floor
		// double limelightLensHeightMetres = 0.224;

		// // distance from the target to the floor
		// double goalHeightMils = 1.320;

		// double angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
		// double angleToGoalRadians = Math.toRadians(angleToGoalDegrees);

		// //calculate distance
		// double distanceFromLimelightToGoalInches = (goalHeightMils - limelightLensHeightMetres) / Math.tan(angleToGoalRadians);

		// SmartDashboard.putNumber("estimated distance (m)", distanceFromLimelightToGoalInches);

    if (DriverStation.getAlliance().get() == Alliance.Red){
      distanceFromSpeaker = Math.abs(SwerveDrive.estimatedX - 8);//8
    } else if (DriverStation.getAlliance().get() == Alliance.Blue){
      distanceFromSpeaker = SwerveDrive.estimatedX + 8;
    }

    if (operatorController.b().getAsBoolean()){
      shooterPivot.setPivotPosition(0);
    } else {
      //TODO safe option: double pivotSetpoint = 0.3155*Math.pow(distanceFromSpeaker, 3) - 4.5249*Math.pow(distanceFromSpeaker, 2) + 22.829*distanceFromSpeaker - 19.033;
      //TODO estimated changes from excel: double pivotSetpoint = 0.0041*Math.pow(distanceFromSpeaker, 4) + 0.2536*Math.pow(distanceFromSpeaker, 3) +  4.3109*Math.pow(distanceFromSpeaker, 2) + 22.73*distanceFromSpeaker - 19.088;

      //TODO trying to account for overshooting at around 2m:
      double pivotSetpoint = 0.4636*Math.pow(distanceFromSpeaker, 3) - 6.0271*Math.pow(distanceFromSpeaker, 2) + 27.305*distanceFromSpeaker - 22.36;

      if (pivotSetpoint > maxSetpoint) pivotSetpoint = maxSetpoint;
      if (pivotSetpoint < minSetpoint) pivotSetpoint = minSetpoint;
      shooterPivot.setPivotPosition(pivotSetpoint);
      SmartDashboard.putNumber("distance from speaker", distanceFromSpeaker);
      SmartDashboard.putNumber("estimated setpoint", pivotSetpoint);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
