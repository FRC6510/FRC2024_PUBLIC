// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
 
package frc.robot;
 
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.Intake.Intake;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Feeder.Feeder;
import frc.robot.subsystems.Shooter.Shooter;
import frc.robot.subsystems.Shooter.ShooterLimelight;
import frc.robot.subsystems.Shooter.SpinFeedShooter;
import frc.robot.subsystems.ShooterFeeder.ShooterFeeder;
import frc.robot.subsystems.ShooterPivot.ShooterPivot;
import frc.robot.subsystems.lift.Lift;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
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
    // chooser.addOption("Figure 8s ", auto.getAuto(AutoList.FIGURE_EIGHTS));
    chooser.addOption("Test turning", auto.getAuto(AutoList.TEST_TURNING));
    chooser.addOption("Blue full", auto.getAuto(AutoList.BLUE_FULL));// Max 32 points
    chooser.addOption("3 cycles test", auto.getAuto(AutoList.BLUE_FIRST_THREE));// Max 32 points
    chooser.addOption("Cycles test", auto.getAuto(AutoList.CYCLES_TEST));// Max 32 points
    SmartDashboard.putData(chooser);
    
    //right bumper intakes, left outtakes for feeder


    // operatorController.leftBumper().whileTrue(feeder.spinFeeder(()-> 0.3));
    // operatorController.rightBumper().whileTrue(feeder.spinFeeder(()-> -0.3));

    swerveDrive.setDefaultCommand(swerveDrive.driveCommand(
      () -> -driverController.getLeftY(), 
      () -> -driverController.getLeftX(),  
      () -> -driverController.getRightX()));

    driverController.y().onTrue(new InstantCommand(() -> swerveDrive.resetHeading(), swerveDrive));

    driverController.a().onTrue(new InstantCommand(
        () -> swerveDrive.swerveDriveOdometry.resetPosition(
          swerveDrive.getIMU().getRotation2d(), 
          swerveDrive.getCurrentModulePositions(), 
          new Pose2d(0, 0, new Rotation2d())), swerveDrive));

    driverController.b()
    .whileTrue(new InstantCommand(() -> swerveDrive.setSpeedLimit(0.45), swerveDrive))
    .whileFalse(new InstantCommand(() -> swerveDrive.setSpeedLimit(1), swerveDrive));

    // driverController.rightBumper().onTrue(swerveDrive.turnTo(Rotation2d.fromDegrees(90)));
    // driverController.leftBumper().onTrue(swerveDrive.turnTo(Rotation2d.fromDegrees(-90)));

    driverController.rightBumper().whileTrue(limelight.faceTargetWhileDriving(swerveDrive,
       () -> -driverController.getLeftY(),
       () -> -driverController.getLeftX(),
       () -> -driverController.getRightX()));

    driverController.rightTrigger(0.1)
    .onTrue(new StartEndCommand(() -> shooterPivot.setIdle(NeutralModeValue.Coast), () -> shooterPivot.setIdle(NeutralModeValue.Coast), shooterPivot));
    // .whileFalse(stopFeedShooter);

    driverController.leftTrigger(0.1)
    .onTrue(new StartEndCommand(() -> shooterPivot.setIdle(NeutralModeValue.Brake), () -> shooterPivot.setIdle(NeutralModeValue.Brake), shooterPivot));
    // .whileFalse(stopFeedShooter);

    
    //TODO operator controllers start here
    operatorController.rightBumper().onTrue(
      new ParallelCommandGroup(
        intake.spinIntake(() -> 40), 
        feeder.spinFeeder(() -> -40)))
    .onFalse(
      new ParallelCommandGroup(
        intake.spinIntake(() -> 0), 
        feeder.spinFeeder(() -> 0)));

     operatorController.leftBumper().onTrue(
      new ParallelCommandGroup(
        intake.spinIntake(() -> -8), 
        feeder.spinFeeder(() -> 8)))
    .onFalse(
      new ParallelCommandGroup(
        intake.spinIntake(() -> 0), 
        feeder.spinFeeder(() -> 0)));

  operatorController.leftTrigger(0.1)
    .whileTrue(shooter.shooterPID(() -> 0.33));
    // .whileFalse(stopFeedShooter);

    operatorController.rightTrigger(0.1)
    .whileTrue(spinFeedShooter)
    .whileFalse(stopFeedShooter);

    // operatorController.rightTrigger(0.1).whileTrue(
    //   new ParallelCommandGroup(
    //     shooterFeeder.spinShooterFeeder(() -> 0.4),
    //     feeder.spinFeeder(() -> -40))).whileFalse(
    //   new ParallelCommandGroup(
    //     shooterFeeder.spinShooterFeeder(() -> 0),
    //     feeder.spinFeeder(() -> 0)));
        
    // operatorController.leftTrigger(0.1).whileTrue(
    //   new ParallelCommandGroup(
    //     shooterFeeder.spinShooterFeeder(() -> 0.4),
    //     shooter.shooterPID(() -> 1))).whileFalse(
    //   new ParallelCommandGroup(
    //     shooterFeeder.spinShooterFeeder(() -> 0),
    //     shooter.shooterPID(() -> 0)));
    
    operatorController.pov(0).onTrue(lift.liftPID(lift, () -> 90));
    operatorController.pov(90).onTrue(lift.liftPID(lift, () -> 19.5));
    operatorController.pov(180).onTrue(lift.liftPID(lift, () -> 0));
  
    operatorController.y()
    .whileTrue(shooterPivot.shooterPivotPID(shooterPivot, () -> 18))
    .whileFalse(shooterPivot.shooterPivotPID(shooterPivot, () -> 0));

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

    // driverController.rightBumper().onTrue(Commands.startEnd(
    //   () -> swerveDrive.setModVelocity(4), 
    //   () -> swerveDrive.setModVelocity(4), 
    //   swerveDrive)
    // ).onFalse(Commands.startEnd(
    //   () -> swerveDrive.setModVelocity(0), 
    //   () -> swerveDrive.setModVelocity(0),
    //    swerveDrive));

    // driverController.leftBumper().onTrue(Commands.startEnd(
    //   () -> swerveDrive.setModAngle(90), 
    //   () -> swerveDrive.setModAngle(90), 
    //   swerveDrive)
    // ).onFalse(Commands.startEnd(
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
