// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package frc.robot.subsystems.ShooterPivot;

// import java.util.function.DoubleSupplier;
// import edu.wpi.first.wpilibj2.command.Command;

// public class MoveShooterPivot extends Command {
//   private final ShooterPivot shooterPivot;
//   private final DoubleSupplier powerSupplier;
//   /** Creates a new MoveShooterPivot. */
//   public MoveShooterPivot(ShooterPivot shooterPivot, DoubleSupplier powerSupplier) {
//     this.shooterPivot = shooterPivot;
//     this.powerSupplier = powerSupplier;
//     // Use addRequirements() here to declare subsystem dependencies.
//     addRequirements(shooterPivot);
//   }

//   // Called when the command is initially scheduled.
//   @Override
//   public void initialize() {}

//   // Called every time the scheduler runs while the command is scheduled.
//   @Override
//   public void execute() {
//     shooterPivot.shooterPivotPID(shooterPivot, powerSupplier);
//   }

//   // Called once the command ends or is interrupted.
//   @Override
//   public void end(boolean interrupted) {
//     shooterPivot.getMotor().set(0);
//   }

//   // Returns true when the command should end.
//   @Override
//   public boolean isFinished() {
//     return false;
//   }
// }
