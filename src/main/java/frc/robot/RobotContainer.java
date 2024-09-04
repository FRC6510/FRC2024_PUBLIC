// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.drivebase.Drive;
import frc.robot.subsystems.drivebase.Limelight;
import frc.robot.subsystems.drivebase.LockToHeading;
import frc.robot.subsystems.drivebase.Swerve;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.lift.Lift;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.SpinShooter;
import frc.robot.subsystems.shooterfeeder.ShooterFeeder;
import frc.robot.subsystems.shooterpivot.ShooterPivot;

public class RobotContainer {
	private final CommandXboxController driverController = new CommandXboxController(0);
	private final CommandXboxController operatorController = new CommandXboxController(1);
	private final SendableChooser<Command> autoChooser;

	public RobotContainer() {
		configureBindings();
		autoChooser = AutoBuilder.buildAutoChooser();
		SmartDashboard.putData("Auto Chooser", autoChooser);

		
	}

	private void configureBindings() {

		//DRIVER CONTROLLER
		//default drive command
		Swerve.INSTANCE.setDefaultCommand(Swerve.drive(
			() -> driverController.getLeftY(),
			() -> driverController.getLeftX(),
			() -> driverController.getRightX())
		);

		driverController.y().onTrue(new InstantCommand(() -> Swerve.setPose()));

		//lock to heading with button X
		driverController.x().onTrue(new LockToHeading(
			() -> driverController.getLeftY(),
			() -> driverController.getLeftX()
		));

		//speed limit control with button B
		driverController.b()
		.whileTrue(new InstantCommand(() -> Swerve.setSpeedLimit(0.1)))
		.whileFalse(new InstantCommand(() -> Swerve.setSpeedLimit(1)));

		//reset pose
		driverController.a().onTrue(new InstantCommand(() -> Swerve.setPose()));

		//OPERATOR CONTROLLER
		//moving lift
		operatorController.rightBumper().onTrue(Lift.moveLift(0)); //down
		operatorController.leftBumper().onTrue(Lift.moveLift(-90)); //up

		//intake + feeder
		operatorController.b().onTrue(Commands.parallel(Intake.spinIntakeVelocity(20), Feeder.spinFeederVelocity(-20)));
		operatorController.a().onTrue(Commands.parallel(Intake.spinIntake(-0.5), Feeder.spinFeeder(0.5), ShooterFeeder.spinShooterFeeder(-1))).whileFalse(Commands.parallel(Intake.spinIntake(0), Feeder.spinFeeder(0), ShooterFeeder.spinShooterFeeder(0)));

		//shooter
		operatorController.y().whileTrue(Shooter.spinShooter(()->1.5)).whileFalse(Shooter.spinShooterSimple(0));

		//shooter feeder + feeder
		operatorController.x().whileTrue(Commands.parallel(ShooterFeeder.spinShooterFeeder(0.5), Feeder.spinFeeder(-0.5), Shooter.spinShooter(()->1.5))).whileFalse(Commands.parallel(ShooterFeeder.spinShooterFeeder(0), Feeder.spinFeeder(0), Intake.spinIntake(0), Shooter.spinShooterSimple(0)));
	}

	public Command getAutonomousCommand() {
		return autoChooser.getSelected();
	}
}
