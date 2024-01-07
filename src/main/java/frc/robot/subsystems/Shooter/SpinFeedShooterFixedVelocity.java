// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Shooter;

import java.util.function.DoubleSupplier;
import java.util.function.BooleanSupplier;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Drivetrain.SwerveDrive;
import frc.robot.subsystems.Feeder.Feeder;
import frc.robot.subsystems.ShooterFeeder.ShooterFeeder;

public class SpinFeedShooterFixedVelocity extends Command {
  private final Shooter shooter;
  private final ShooterFeeder shooterFeeder;
  private final Feeder feeder;
  private final SwerveDrive swerveDrive;
  private final DoubleSupplier shooterSupplier;
  private final DoubleSupplier sFeederSupplier;
  private final DoubleSupplier feederSupplier;
  private final BooleanSupplier endCondition;
  private BooleanSupplier inAuto;
  private boolean shooting;
  double distanceFromSpeaker;
  double setPoint;
  double x = 10000;
  double y = 10000;
  public int noteCounter;
  private Timer timer;
  private XboxController driverController;




  /** Creates a new SpinFeedShooter. */
  public SpinFeedShooterFixedVelocity(Shooter shooter,
   ShooterFeeder shooterFeeder,
    Feeder feeder, SwerveDrive swerveDrive,
     DoubleSupplier shooterSupplier, DoubleSupplier sFeederSupplier,
      DoubleSupplier feederSupplier, BooleanSupplier endCondition
      ) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.shooter = shooter;
    this.shooterFeeder = shooterFeeder;
    this.feeder = feeder;
    this.swerveDrive = swerveDrive;
    this.shooterSupplier = shooterSupplier;
    this.sFeederSupplier = sFeederSupplier;
    this.feederSupplier = feederSupplier;
    this.endCondition = endCondition;
    addRequirements(shooter);
    addRequirements(shooterFeeder);
    timer = new Timer();
    driverController = new XboxController(0);
  }

  public SpinFeedShooterFixedVelocity(Shooter shooter, ShooterFeeder shooterFeeder, Feeder feeder, SwerveDrive swerveDrive, DoubleSupplier shooterSupplier, DoubleSupplier sFeederSupplier, DoubleSupplier feederSupplier, BooleanSupplier endCondition, double x, double y) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.shooter = shooter;
    this.shooterFeeder = shooterFeeder;
    this.feeder = feeder;
    this.swerveDrive = swerveDrive;
    this.shooterSupplier = shooterSupplier;
    this.sFeederSupplier = sFeederSupplier;
    this.feederSupplier = feederSupplier;
    this.endCondition = endCondition;
    // this.x = x;
    // this.y = y;
    addRequirements(shooter);
    addRequirements(shooterFeeder);
    timer = new Timer();
    driverController = new XboxController(1);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    noteCounter = 0;
    shooting = false;
    timer.reset();
    timer.stop();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {  
    
    double setPoint = shooterSupplier.getAsDouble() * Shooter.maxRPM;
    shooter.getPidControllerTop().setReference(setPoint, CANSparkMax.ControlType.kVelocity);
    shooter.getPidControllerBottom().setReference(setPoint, CANSparkMax.ControlType.kVelocity);
    
      if ((shooter.shooterAtSpeed(setPoint) && driverController.getRightBumper()) || (driverController.getRightTriggerAxis() > 0.1)){// 
        shooterFeeder.shooterFeeder.set(sFeederSupplier.getAsDouble());
        feeder.setFeederVelocity(feederSupplier.getAsDouble());
      } 

    if (shooterFeeder.noted){
      noteCounter++;
    }

    System.out.println(noteCounter);

    if (noteCounter >= 1 && shooting == false){
      shooting = true;
      timer.reset();
      timer.start();
    }
    
    }
  

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("spinfeedshoot finished!");
    if (!interrupted){
      shooterFeeder.shooterFeeder.set(0);
      feeder.setFeederVelocity(0);
      shooter.stop();
    }

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return endCondition.getAsBoolean() || noteCounter >= 2 || timer.get() > 0.6;
  }
}
