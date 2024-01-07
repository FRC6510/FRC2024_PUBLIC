package frc.robot.subsystems.limelight;

import edu.wpi.first.wpilibj2.command.Command;

public class SwitchPipeline extends Command {
  private final LimelightBack limelight;
  private final PipelineID pipelineID;

  /** Creates a new pipeline. */
  public SwitchPipeline(LimelightBack limelight, PipelineID pipelineID) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.limelight = limelight;
    this.pipelineID = pipelineID;
    addRequirements(limelight);
    
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

  public enum PipelineID {
    NeuralNetwork(0),
    Thresholding(4),
    LowLight(5);
    
    private final int id;
    PipelineID(int id) {
      this.id = id;
    }

    int id() {
      return id;
    }
  }
}