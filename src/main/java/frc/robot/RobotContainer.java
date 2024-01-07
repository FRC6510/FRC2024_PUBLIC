// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.autonomous.Auto;
import frc.robot.commands.autonomous.Auto.AutoList;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.drivetrain.LockToHeading;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.lift.Lift;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.ShooterPID;
import frc.robot.subsystems.shooter.SpinShooter;
import frc.robot.subsystems.shooterfeeder.ShooterFeeder;
import frc.robot.subsystems.shooterpivot.ShooterPivot;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  public final Lift lift = new Lift();
  public final ShooterPivot shooterPivot = new ShooterPivot();
  public final ShooterFeeder shooterFeeder = new ShooterFeeder();
  public final Intake intake = new Intake();
  public final Feeder feeder = new Feeder();
  public final Shooter shooter = new Shooter();

  // swerve
  public SwerveDrive swerveDrive = new SwerveDrive();

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController driverController = new CommandXboxController(0);
  private final CommandXboxController operatorController = new CommandXboxController(1);

  public Auto auto = new Auto(this);
  SendableChooser<Command> chooser = new SendableChooser<>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    //amp scoring
    operatorController.rightBumper().onTrue(lift.liftPID(lift, 0));
    operatorController.leftBumper().onTrue(Commands.parallel(feeder.spinFeederAutomatic(-4), lift.liftPID(lift, -90)));

    //intake and feeder spin together
    operatorController.b().onTrue(Commands.parallel(intake.spinThrice(20), feeder.spinFeeder(-40)));

    //dispenses the note
    operatorController.a().whileTrue(Commands.parallel(intake.spinIntake(-0.5), feeder.simpleSpinFeeder(0.5), shooterFeeder.spinShooterFeeder(-0.5))).whileFalse(Commands.parallel(intake.spinIntake(0), feeder.simpleSpinFeeder(0), shooterFeeder.spinShooterFeeder(0)));
    
    //dispenses the note for amp
    operatorController.leftTrigger().whileTrue(Commands.parallel(intake.spinIntake(-0.5), feeder.simpleSpinFeeder(0.5), shooterFeeder.spinShooterFeeder(-0.5), lift.liftPID(lift, -90))).whileFalse(Commands.parallel(intake.spinIntake(0), feeder.simpleSpinFeeder(0), shooterFeeder.spinShooterFeeder(0)));

    //shoot the note (moves feeder, shooter feeder, shooter and stops intake)
    operatorController.y().whileTrue(new ShooterPID(shooter, ()->1.5)).whileFalse(shooter.spinShooter(shooter, 0));
    operatorController.x().whileTrue(Commands.parallel(shooterFeeder.spinShooterFeeder(1), feeder.simpleSpinFeeder(-1))).whileFalse(Commands.parallel(shooterFeeder.spinShooterFeeder(0), feeder.simpleSpinFeeder(0), intake.spinIntake(0)));

    //54.92 --> position for climbing

    // shooter pivot code 
    // operatorController.a().onTrue(shooterPivot.shooterPivotPID(shooterPivot, 0));
    // operatorController.b().onTrue(shooterPivot.shooterPivotPID(shooterPivot, 10)); //change number of rotations required to shoot in speaker
  
    
    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
    //operatorController.rightTrigger().onTrue(new SpinShooter(shooter, 0.95)).onFalse(new SpinShooter(shooter, 0));
    //operatorController.leftTrigger().onTrue(new SpinShooter(shooter, -0.95)).onFalse(new SpinShooter(shooter, 0));

    //second use spinshooter method to stop the motor, not pid
    //saves energy and doesnt oscillate
    //find rpm counts per second and velocity? w/o pid and compare with pid
    //operatorController.rightTrigger().onTrue(new ShooterPID(shooter, () -> 0.95)).onFalse(new SpinShooter(shooter, 0));
    //operatorController.leftTrigger().onTrue(shooter.spinShooter(shooter, 0));
    
    //Commands.parallel(intake.spinIntake(-0.8), feeder.simpleSpinFeeder(0.8))).whileFalse(Commands.parallel(intake.spinIntake(0), feeder.simpleSpinFeeder(0)));

    //intake and feeder spin together, then move lift upwards
    //operatorController.a().onTrue(Commands.sequence(Commands.parallel(intake.spinThrice(20), feeder.spinFeeder(-20))).andThen(lift.liftPID(lift,-90)));

    //drive command
    swerveDrive.setDefaultCommand(swerveDrive.driveCommand(
      () -> driverController.getLeftY(),
      () -> driverController.getLeftX(),
      () -> driverController.getRightX())
    );

    driverController.y().onTrue(new InstantCommand(()-> swerveDrive.resetHeading(), swerveDrive));

    //reset odometry
    driverController.rightTrigger(0.1).and(driverController.leftTrigger(0.1)).onTrue(new InstantCommand(
      () -> swerveDrive.resetPosition(
        swerveDrive.getIMU().getRotation2d(), 
        swerveDrive.getCurrentModulePositions(), 
        new Pose2d(0, 0, swerveDrive.getIMU().getRotation2d())), swerveDrive));

    // Slow SwerveDrive
    driverController.b()
    .whileTrue(new InstantCommand(() -> swerveDrive.setSpeedLimit(1), swerveDrive))
    .whileFalse(new InstantCommand(() -> swerveDrive.setSpeedLimit(0.5), swerveDrive));

    // driverController.rightBumper().onTrue(swerveDrive.turnTo(Rotation2d.fromDegrees(90)));
    // driverController.leftBumper().onTrue(swerveDrive.turnTo(Rotation2d.fromDegrees(-90)));

    // // 
    // driverController.x().whileTrue(
    //   new RepeatCommand(
    //     shooterLimelight.faceTargetWhileDriving(
    //       swerveDrive,
    //       () -> -driverController.getLeftY(),
    //       () -> -driverController.getLeftX(),
    //       () -> -driverController.getRightX()
    //     )
    //   )
    // );
    //2.8m distance to shoot for back one.
    driverController.a().whileTrue(new RepeatCommand(
        new LockToHeading(
          swerveDrive,
          () -> -driverController.getLeftY(),
          () -> -driverController.getLeftX(),
          () -> -driverController.getRightX()
        )));

    //driverController.leftBumper().onTrue(lift.liftPID(lift, -54.92));
    //driverController.rightBumper().onTrue(lift.liftPID(lift, 0));




    //ALL the auto options
    //chooser.addOption("Blue 5 note W1", auto.getAuto(AutoList.BLUE_FIVE_W1));// front 3 + leftmost note on white line
    //chooser.addOption("Blue 5 note W2", auto.getAuto(AutoList.BLUE_FIVE_W2));// front 3 + second note from left on white line
    //chooser.addOption("Red 5 note no w1", auto.getAuto(AutoList.BLUE_FIVE_W1_FOR_RED));// front 3 + leftmost note on white line
    //chooser.addOption("Red 5 note W2", auto.getAuto(AutoList.RED_FIVE_W2));// front 3 + second note from left on white line
    //chooser.addOption("Blue 3 note W5", auto.getAuto(AutoList.BLUE_THREE_W5));// front 3 + second note from left on white line
    //chooser.addOption("Blue NO WHITE", auto.getAuto(AutoList.BLUE_NO_WHITE));//front 3 notes, then correct angle
    //chooser.addOption("Shoot preload + just cross the line", auto.getAuto(AutoList.SHOOT_AND_CROSS_LINE_BLUE));//go to the 1st pos and shoot. 5 sec delay at the very beginning
    //chooser.addOption("Blue test curving", auto.getAuto(AutoList.BLUE_TEST_CURVING));// front 3 + second note from left on white line
    //chooser.addOption("Playoffs - go 3m forward", auto.getAuto(AutoList.PLAYOFFS_AUTO_MOVE_THREE_METRES));//
    //chooser.addOption("Playoffs - go and shoot 1", auto.getAuto(AutoList.PLAYOFFS_AUTO_SHOOT));//
    //chooser.addOption("las Vegas", auto.getAuto(AutoList.BLUE_MIIDLE_3_NOTES));//FOR_AYSEL
    //chooser.addOption("FOR_AYSEL", auto.getAuto(AutoList.FOR_AYSEL)); //TEST_INTAKING
    //chooser.addOption("TEST_INTAKING", auto.getAuto(AutoList.TEST_INTAKING));
    // chooser.addOption("BLUE 5 note Vegas", auto.getAuto(AutoList.BLUE_W1_REDONE));
    // chooser.addOption("BLUE Right Vegas", auto.getAuto(AutoList.BLUE_RIGHT_REDONE));
    // chooser.addOption("RED 5 note Vegas", auto.getAuto(AutoList.RED_W1_REDONE));
    // chooser.addOption("RED Left Vegas", auto.getAuto(AutoList.RED_LEFT_REDONE));
    // chooser.addOption("RED 5 new Vegas", auto.getAuto(AutoList.test));

    chooser.addOption("DDU Blue Auto", auto.getAuto(AutoList.DDU_BLUE_AUTO));
    chooser.addOption("DDU Red Auto", auto.getAuto(AutoList.DDU_RED_AUTO));
    chooser.addOption("DDU Shoot Preload", auto.getAuto(AutoList.DDU_SHOOT_PRELOAD));
    chooser.setDefaultOption("DDU Shoot Preload", auto.getAuto(AutoList.DDU_SHOOT_PRELOAD));
    SmartDashboard.putData(chooser);
  }
  
  /**
   *
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    //return auto.getAuto(AutoList.DDU_RED_AUTO);
    return chooser.getSelected();
  }
}