// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package frc.robot.subsystems.limelight;

// import edu.wpi.first.wpilibj2.command.Command;

// public class SwitchPipeline extends Command {
//   private final Limelight limelight;
//   private final PipelineID pipelineID;

//   /** Creates a new pipeline. */
//   public SwitchPipeline(Limelight limelight, PipelineID pipelineID) {
//     // Use addRequirements() here to declare subsystem dependencies.
//     this.limelight = limelight;
//     this.pipelineID = pipelineID;
//     addRequirements(limelight);
    
//   }

//   // Called when the command is initially scheduled.
//   @Override
//   public void initialize() {
//     // limelight.setPipeline(pipelineID.id());
//   }

//   // Called every time the scheduler runs while the command is scheduled.
//   @Override
//   public void execute() {}

//   // Called once the command ends or is interrupted.
//   @Override
//   public void end(boolean interrupted) {}

//   // Returns true when the command should end.
//   @Override
//   public boolean isFinished() {
//     return true;
//   }

//   public enum PipelineID {
//     AMPS(1),
//     SPEAKER_FRONT(2),
//     SPEAKER_SIDE(3),
//     SOURCE_LEFT(4),
//     SOURCE_RIGHT(5),
//     CENTRE_STAGE(6),
//     RIGHT_STAGE(7),
//     LEFT_STAGE(8);
    
//     private final int id;
//     PipelineID(int id) {
//       this.id = id;
//     }

//     int id() {
//       return id;
//     }
//   }
// }
