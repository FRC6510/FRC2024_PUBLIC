// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package frc.robot.subsystems.lift;

// import java.util.function.DoubleSupplier;

// import edu.wpi.first.wpilibj2.command.Command;

// public class MoveLift extends Command {
//   private final Lift liftMotor;
//   private final DoubleSupplier powerSupplier;
//   /** Creates a new MoveLift. */
//   public MoveLift(Lift liftMotor, DoubleSupplier powerSupplier) {
//     this.liftMotor = liftMotor;
//     this.powerSupplier = powerSupplier;
//     // Use addRequirements() here to declare subsystem dependencies.
//     addRequirements(liftMotor);
//   }

//   // Called when the command is initially scheduled.
//   @Override
//   public void initialize() {}

//   // Called every time the scheduler runs while the command is scheduled.
//   @Override
//   public void execute() {
//     liftMotor.getMotor().set(powerSupplier.getAsDouble());
//   }

//   // Called once the command ends or is interrupted.
//   @Override
//   public void end(boolean interrupted) {
//     liftMotor.getMotor().set(0);
//   }

//   // Returns true when the command should end.
//   @Override
//   public boolean isFinished() {
//     return false;
//   }
// }
