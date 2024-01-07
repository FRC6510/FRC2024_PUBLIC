// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
 
package frc.robot;
 
import java.time.Instant;

import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.Intake.Intake;
import frc.robot.subsystems.Intake.IntakeLimelight;
import frc.robot.subsystems.LimelightCommands.LockToHeading;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Feeder.Feeder;
import frc.robot.subsystems.Shooter.Shooter;
import frc.robot.subsystems.Shooter.ShooterLimelight;
import frc.robot.subsystems.Shooter.SpinFeedShooter;
import frc.robot.subsystems.Shooter.SpinFeedShooterFixedVelocity;
import frc.robot.subsystems.Shooter.ShooterLimelight.LEDMode;
import frc.robot.subsystems.ShooterFeeder.ShooterFeeder;
import frc.robot.subsystems.ShooterPivot.ShooterPivot;
import frc.robot.subsystems.ShooterPivot.ShooterPivotPID;
import frc.robot.subsystems.ShooterPivot.ShooterPivotPIDEndless;
import frc.robot.subsystems.lift.Lift;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
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
  public Feeder feeder = new Feeder(operatorController);
  public Lift lift = new Lift();
  public ShooterPivot shooterPivot = new ShooterPivot(lift);
  public SpinFeedShooter spinFeedShooter = SpinFeedShooter.teleopSpinFeedShoot(shooter, shooterFeeder, feeder, swerveDrive, () -> 1, () -> 0.4, () -> -40);
  public SpinFeedShooterFixedVelocity shootClose = shooter.spinFeedShootFixed(shooter, shooterFeeder, feeder, swerveDrive, () -> 0.75, () -> 0.4, () -> -40);
  public SpinFeedShooterFixedVelocity shootFar = shooter.spinFeedShootFixed(shooter, shooterFeeder, feeder, swerveDrive, () -> 0.9, () -> 0.4, () -> -40);
  public SpinFeedShooterFixedVelocity shootFar2 = shooter.spinFeedShootFixed(shooter, shooterFeeder, feeder, swerveDrive, () -> 0.9, () -> 0.4, () -> -40);
  // public SpinFeedShooterFixedVelocity shootFurtherst = shooter.spinFeedShootFixed(shooter, shooterFeeder, feeder, swerveDrive, () -> 0.9, () -> 0.4, () -> -40);
  public SpinFeedShooter stopFeedShooter = SpinFeedShooter.teleopSpinFeedShoot(shooter, shooterFeeder, feeder, swerveDrive, () -> 0, () -> 0, () -> 0);
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
    chooser.addOption("BLUE 5 note Vegas", auto.getAuto(AutoList.BLUE_W1_REDONE));
    chooser.addOption("BLUE Right Vegas", auto.getAuto(AutoList.BLUE_RIGHT_REDONE));
    chooser.addOption("RED 5 note Vegas", auto.getAuto(AutoList.RED_W1_REDONE));
    chooser.addOption("RED Left Vegas", auto.getAuto(AutoList.RED_LEFT_REDONE));
    chooser.addOption("RED 5 new Vegas", auto.getAuto(AutoList.test));
    SmartDashboard.putData(chooser);
    
    //right bumper intakes, left outtakes for feeder


    // operatorController.leftBumper().whileTrue(feeder.spinFeeder(()-> 0.3));
    // operatorController.rightBumper().whileTrue(feeder.spinFeeder(()-> -0.3));
    feeder.getContainsNoteTrigger().onTrue(
      limelight.ShooterLED(LEDMode.BLINK).deadlineWith(new WaitCommand(10)).andThen(limelight.ShooterLED(LEDMode.OFF))
    );

    //Drive command
    swerveDrive.setDefaultCommand(swerveDrive.driveCommand(
      () -> driverController.getLeftY(),
      () -> driverController.getLeftX(),
      () -> driverController.getRightX())
    );

    driverController.y().onTrue(new InstantCommand(() -> swerveDrive.resetHeading(), swerveDrive));

    driverController.rightTrigger(0.1).and(driverController.leftTrigger(0.1)).onTrue(new InstantCommand(
      () -> swerveDrive.resetPosition(
        swerveDrive.getIMU().getRotation2d(), 
        swerveDrive.getCurrentModulePositions(), 
        new Pose2d(0, 0, swerveDrive.getIMU().getRotation2d())), swerveDrive));

    driverController.b()
    .whileTrue(new InstantCommand(() -> swerveDrive.setSpeedLimit(0.45), swerveDrive))
    .whileFalse(new InstantCommand(() -> swerveDrive.setSpeedLimit(1), swerveDrive));

    // driverController.rightBumper().onTrue(swerveDrive.turnTo(Rotation2d.fromDegrees(90)));
    // driverController.leftBumper().onTrue(swerveDrive.turnTo(Rotation2d.fromDegrees(-90)));

    driverController.x().whileTrue(
      new RepeatCommand(
        limelight.faceTargetWhileDriving(
          swerveDrive,
          () -> -driverController.getLeftY(),
          () -> -driverController.getLeftX(),
          () -> -driverController.getRightX()
        )
      )
    );
    //2.8m distance to shoot for back one.
    driverController.a().whileTrue( new RepeatCommand(
        new LockToHeading(
          swerveDrive,
          () -> -driverController.getLeftY(),
          () -> -driverController.getLeftX(),
          () -> -driverController.getRightX()
        )));



    driverController.leftBumper().onTrue(
      new ParallelCommandGroup(
        feeder.spinFeeder(() -> -Feeder.FEEDER_SPEED
        ),
        intake.spinIntakeProper(() -> -Intake.INTAKING_SPEED, feeder, lift),
        shooterFeeder.spinShooterFeeder(() -> -0.3)
      )
    ).onFalse(new InstantCommand(() -> feeder.stop(), feeder));


    //TODO operator controllers start here
    shooterPivot.setDefaultCommand(shooterPivot.shooterPivotAutomation(swerveDrive, shooterPivot, 0));
    //TODO feeder stops once note is detected and limelight leds flash 3 times
    operatorController.rightBumper().and(() -> !feeder.getContainsNote()).onTrue(
      new ParallelCommandGroup(
        intake.spinIntakeProper(() -> Intake.INTAKING_SPEED, feeder, lift), 
        feeder.spinFeeder(() -> Feeder.FEEDER_SPEED)))
    .onFalse(new InstantCommand(() -> feeder.stop(), feeder));//AsaphChanged
     // new ParallelCommandGroup(
       //intake.outtakeBriefly(feeder, lift), //Asaph changed this
        //feeder.holdFeeder())); Asaph Changed this
        
    //));


    operatorController.rightTrigger(0.1)
      .whileTrue(spinFeedShooter)
      .whileFalse(new ParallelCommandGroup(
        new InstantCommand(() -> shooter.stop(), shooter),
        new InstantCommand(() -> shooterFeeder.stop(), shooterFeeder),
        new InstantCommand(() -> feeder.stop(), feeder)
      ));

    operatorController.a().whileTrue(new RunCommand(()->shooterPivot.setPivotPositionDegrees((-operatorController.getLeftX() + 1) * 10.25),feeder));

    // operatorController.a().whileTrue(new RunCommand(()->shooterPivot.shooterPivot.set(-operatorController.getLeftX()*0.1),feeder));
    //Shoot close is 2.75m from speaker and 2.3m from blue line
    operatorController.b().whileTrue(
      new ParallelCommandGroup(new ShooterPivotPIDEndless(shooterPivot, ()->0), shootClose))
      .whileFalse(new ParallelCommandGroup(
        new InstantCommand(() -> shooter.stop(), shooter),
        new InstantCommand(() -> shooterFeeder.stop(), shooterFeeder),
        new InstantCommand(() -> feeder.stop(), feeder)
      ));

    operatorController.y().whileTrue(
      new ParallelCommandGroup(new ShooterPivotPIDEndless(shooterPivot, ()-> 21), shootFar))
      .whileFalse(new ParallelCommandGroup(
        new InstantCommand(() -> shooter.stop(), shooter),
        new InstantCommand(() -> shooterFeeder.stop(), shooterFeeder),
        new InstantCommand(() -> feeder.stop(), feeder)
      ));

    // operatorController.x().whileTrue(
    //   new ParallelCommandGroup(new ShooterPivotPIDEndless(shooterPivot, ()-> 24), shootFar2))
    //   .whileFalse(new ParallelCommandGroup(
    //     new InstantCommand(() -> shooter.stop(), shooter),
    //     new InstantCommand(() -> shooterFeeder.stop(), shooterFeeder),
    //     new InstantCommand(() -> feeder.stop(), feeder)
    //   ));




    //Remove notes that shouldn't be there out via the shooter.
    driverController.pov(0).onTrue(new ParallelCommandGroup(
       
        intake.spinIntakeProper(() -> -Intake.INTAKING_SPEED, feeder, lift), 
        feeder.spinFeederWithoutNoteDetectionStops(40),
        new InstantCommand(()-> shooterFeeder.shooterFeeder.set(-0.4), shooterFeeder)))
    .whileFalse(new ParallelCommandGroup(
        new InstantCommand(() -> shooter.stop(), shooter),
        new InstantCommand(() -> shooterFeeder.stop(), shooterFeeder),
        new InstantCommand(() -> feeder.stop(), feeder) 
      ));

    //Meant to get rid of a note
    driverController.pov(180).whileTrue(new ParallelCommandGroup(
        shooter.spinShooter(() -> 0.25),
        intake.spinIntakeProper(() -> Intake.INTAKING_SPEED, feeder, lift), 
        feeder.spinFeederWithoutNoteDetectionStops(-40),
        new InstantCommand(()-> shooterFeeder.shooterFeeder.set(0.4), shooterFeeder))
        ).whileFalse(new ParallelCommandGroup(
        new InstantCommand(() -> shooter.stop(), shooter),
        new InstantCommand(() -> shooterFeeder.stop(), shooterFeeder),
        new InstantCommand(() -> feeder.stop(), feeder)
      ));
    operatorController.y().and(operatorController.x()).onTrue(feeder.resetContainsFlag());
    operatorController.a().and(operatorController.back()).whileTrue(new RunCommand(()->shooterPivot.shooterPivot.set(-operatorController.getLeftX()*0.1),feeder));
    

    //Unlock intake command:
    operatorController.back().onTrue(new InstantCommand(() -> feeder.setContainsNote(false), feeder));
    
    operatorController.leftBumper().onTrue(
        feeder.spinFeederWithoutNoteDetectionStops(-Feeder.FEEDER_SPEED)
    ).onFalse(new InstantCommand(()-> feeder.stop(), feeder));
    //   new ParallelCommandGroup(
    //     feeder.spinFeeder(() -> 50),
    //     intake.spinIntake(() -> -50, lift),
    //     shooterFeeder.spinShooterFeeder(() -> -0.3)
    //   )
    // ).onFalse(new InstantCommand(() -> feeder.stop(), feeder));

    operatorController.pov(0).onTrue(lift.TrapAutomation(lift, () -> 93, feeder, () -> 0));
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
