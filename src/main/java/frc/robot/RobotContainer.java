// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
 
package frc.robot;
 
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.Intake.Intake;
import frc.robot.subsystems.Intake.IntakeLimelight;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Feeder.Feeder;
import frc.robot.subsystems.Shooter.Shooter;
import frc.robot.subsystems.Shooter.ShooterLimelight;
import frc.robot.subsystems.Shooter.SpinFeedShooter;
import frc.robot.subsystems.Shooter.ShooterLimelight.LEDMode;
import frc.robot.subsystems.ShooterFeeder.ShooterFeeder;
import frc.robot.subsystems.ShooterPivot.ShooterPivot;
import frc.robot.subsystems.lift.Lift;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.Autonomous.Auto;
import frc.robot.commands.Autonomous.Auto.AutoList;
import frc.robot.subsystems.Drivetrain.SwerveDrive;


/**
* This class is where the bulk of the robot should be declared. Since Command-based is a
* "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
* periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
* subsystems, commands, and trigger mappings) should be declared here.
*/
public class RobotContainer {
  //can split line initialising but only in methods, one line works anywhere

  // Replace with CommandPS4Controller or CommandJoystick if needed

  private final CommandXboxController driverController =
    new CommandXboxController(0);

  private final CommandXboxController operatorController =
  new CommandXboxController(1);
  ShooterLimelight shooterLimelight = new ShooterLimelight();
  IntakeLimelight intakeLimelight = new IntakeLimelight();

  public SwerveDrive swerveDrive = new SwerveDrive(shooterLimelight);

  public boolean inAuto;

  public Intake intake = new Intake();
  public Shooter shooter = new Shooter();
  public ShooterFeeder shooterFeeder = new ShooterFeeder();
  public Feeder feeder = new Feeder();
  public ShooterPivot shooterPivot = new ShooterPivot();
  public Lift lift = new Lift();
  public SpinFeedShooter spinFeedShooter = SpinFeedShooter.teleopSpinFeedShoot(shooter, shooterFeeder, feeder, () -> 1, () -> 0.4, () -> -40);
  public SpinFeedShooter stopFeedShooter = SpinFeedShooter.teleopSpinFeedShoot(shooter, shooterFeeder, feeder, () -> 0, () -> 0, () -> 0);
  public ShooterLimelight limelight = new ShooterLimelight();


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
    chooser.addOption("Blue 5 note W1", auto.getAuto(AutoList.BLUE_FIVE_W1));// front 3 + leftmost note on white line
    chooser.addOption("Blue 5 note W2", auto.getAuto(AutoList.BLUE_FIVE_W2));// front 3 + second note from left on white line
    chooser.addOption("Red 5 note no w1", auto.getAuto(AutoList.BLUE_FIVE_W1_FOR_RED));// front 3 + leftmost note on white line
    chooser.addOption("Red 5 note W2", auto.getAuto(AutoList.RED_FIVE_W2));// front 3 + second note from left on white line
    chooser.addOption("Blue 3 note W5", auto.getAuto(AutoList.BLUE_THREE_W5));// front 3 + second note from left on white line
    chooser.addOption("Blue NO WHITE", auto.getAuto(AutoList.BLUE_NO_WHITE));//front 3 notes, then correct angle
    chooser.addOption("Shoot preload + just cross the line", auto.getAuto(AutoList.SHOOT_AND_CROSS_LINE_BLUE));//go to the 1st pos and shoot. 5 sec delay at the very beginning
    chooser.addOption("Blue test curving", auto.getAuto(AutoList.BLUE_TEST_CURVING));// front 3 + second note from left on white line
    chooser.addOption("Playoffs - go 3m forward", auto.getAuto(AutoList.PLAYOFFS_AUTO_MOVE_THREE_METRES));//
    chooser.addOption("Playoffs - go and shoot 1", auto.getAuto(AutoList.PLAYOFFS_AUTO_SHOOT));//
    SmartDashboard.putData(chooser);
    
    //right bumper intakes, left outtakes for feeder


    // operatorController.leftBumper().whileTrue(feeder.spinFeeder(()-> 0.3));
    // operatorController.rightBumper().whileTrue(feeder.spinFeeder(()-> -0.3));

     swerveDrive.setDefaultCommand(swerveDrive.driveCommand(
      () -> driverController.getLeftY(), 
      () -> driverController.getLeftX(),  
      () -> driverController.getRightX()));

    driverController.y().onTrue(new InstantCommand(() -> swerveDrive.resetHeading(), swerveDrive));

    driverController.rightTrigger(0.1).and(driverController.leftTrigger(0.1)).onTrue(new InstantCommand(
      () -> SwerveDrive.poseEstimatorOdometry.resetPosition(
        swerveDrive.getIMU().getRotation2d(), 
        swerveDrive.getCurrentModulePositions(), 
        new Pose2d(0, 0, new Rotation2d())), swerveDrive));

    driverController.b()
    .whileTrue(new InstantCommand(() -> swerveDrive.setSpeedLimit(0.45), swerveDrive))
    .whileFalse(new InstantCommand(() -> swerveDrive.setSpeedLimit(1), swerveDrive));

    // driverController.rightBumper().onTrue(swerveDrive.turnTo(Rotation2d.fromDegrees(90)));
    // driverController.leftBumper().onTrue(swerveDrive.turnTo(Rotation2d.fromDegrees(-90)));

    driverController.a().whileTrue(new RepeatCommand(
      limelight.faceTargetWhileDriving(swerveDrive,
        () -> -driverController.getLeftY(),
        () -> -driverController.getLeftX(),
        () -> -driverController.getRightX())));

    driverController.leftBumper().onTrue(
      new ParallelCommandGroup(
        feeder.spinFeeder(() -> 50),
        intake.spinIntake(() -> -50, lift),
        shooterFeeder.spinShooterFeeder(() -> -0.3)
      )
    ).onFalse(new InstantCommand(() -> feeder.stop(), feeder));


    //TODO operator controllers start here
    shooterPivot.setDefaultCommand(shooterPivot.shooterPivotAutomation(shooterPivot, 0));

    operatorController.rightBumper().onTrue(
      new ParallelCommandGroup(
        intake.spinIntake(() -> 50, lift), 
        feeder.spinFeeder(() -> -50)))
    .onFalse(
      new ParallelCommandGroup(
        new InstantCommand(() -> intake.stop(), intake),
        new InstantCommand(() -> feeder.stop(), feeder)));

    operatorController.rightTrigger(0.1)
      .whileTrue(spinFeedShooter)
      .whileFalse(new ParallelCommandGroup(
        new InstantCommand(() -> shooter.stop(), shooter),
        new InstantCommand(() -> shooterFeeder.stop(), shooterFeeder),
        new InstantCommand(() -> feeder.stop(), feeder)
      ));
     
    // operatorController.leftBumper().onTrue(
    //   new ParallelCommandGroup(
    //     feeder.spinFeeder(() -> 50),
    //     intake.spinIntake(() -> -50, lift),
    //     shooterFeeder.spinShooterFeeder(() -> -0.3)
    //   )
    // ).onFalse(new InstantCommand(() -> feeder.stop(), feeder));

    operatorController.pov(0).onTrue(lift.TrapAutomation(lift, () -> 90, feeder, () -> 0));
    operatorController.pov(90).onTrue(lift.liftPID(lift, () -> 54.92));//54.92
    // operatorController.pov(180).onTrue(lift.liftPID(lift, () -> 9));
    operatorController.pov(270).onTrue(lift.liftPID(lift, () -> 0));

    // operatorController.x().whileTrue(feeder.spinFeeder(()-> 0.2)).onFalse(feeder.spinFeeder(()->0));
    // operatorController.y().whileTrue(feeder.spinFeeder(()-> -0.2)).onFalse(feeder.spinFeeder(()->0));

    // driverController.pov(0).onTrue(swerveDrive.moveAndTurn(
    //   1, 
    //   0,
    //   new Rotation2d(0)));

    // driverController.pov(90).onTrue(swerveDrive.moveTo(
    //   0, 
    //   -1));

    // driverController.pov(180).onTrue(swerveDrive.moveTo(
    //   -1, 
    //   0));

    // driverController.pov(270).onTrue(swerveDrive.moveTo(
    //   0, 
    //   1));

    // driverController.rightBumper().onTrue(new StartEndCommand(
    //   () -> swerveDrive.setModVelocity(5.89), 
    //   () -> swerveDrive.setModVelocity(5.89), 
    //   swerveDrive)
    // ).onFalse(new StartEndCommand(
    //   () -> swerveDrive.setModVelocity(0), 
    //   () -> swerveDrive.setModVelocity(0),
    //    swerveDrive));

    // driverController.leftBumper().onTrue(new StartEndCommand(
    //   () -> swerveDrive.setModAngle(90), 
    //   () -> swerveDrive.setModAngle(90), 
    //   swerveDrive)
    // ).onFalse(new StartEndCommand(
    //   () -> swerveDrive.setModAngle(0), 
    //   () -> swerveDrive.setModAngle(0), 
    //     swerveDrive));
    
  }
 
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
     return chooser.getSelected();
  }
}
