// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.limelight;

import edu.wpi.first.wpilibj2.command.Command;

public class SwitchShooterPipeline extends Command {
  private final LimelightShooter limelight;
  private final ShooterPipelineID pipelineID;

  /** Creates a new pipeline. */
  public SwitchShooterPipeline(LimelightShooter limelightShooter, ShooterPipelineID pipelineID) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.limelight = limelightShooter;
    this.pipelineID = pipelineID;
    addRequirements(limelightShooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    limelight.setPipeline(pipelineID.id());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }

  public enum ShooterPipelineID {
    AMPS(1),
    SPEAKER_FRONT(2),
    SPEAKER_SIDE(3),
    SOURCE_LEFT(4),
    SOURCE_RIGHT(5),
    CENTRE_STAGE(6),
    RIGHT_STAGE(7),
    LEFT_STAGE(8);
    
    private final int id;
    ShooterPipelineID(int id) {
      this.id = id;
    }

    int id() {
      return id;
    }
  }

}