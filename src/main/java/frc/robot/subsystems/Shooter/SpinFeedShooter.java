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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Drivetrain.SwerveDrive;
import frc.robot.subsystems.Feeder.Feeder;
import frc.robot.subsystems.ShooterFeeder.ShooterFeeder;

public class SpinFeedShooter extends Command {
  private final Shooter shooter;
  private final ShooterFeeder shooterFeeder;
  private final Feeder feeder;
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
  private CommandXboxController driverController;

  public static SpinFeedShooter teleopSpinFeedShoot(Shooter shooter, ShooterFeeder shooterFeeder, Feeder feeder, DoubleSupplier shooterSupplier, DoubleSupplier sFeederSupplier, DoubleSupplier feederSupplier){
    BooleanSupplier inTeleop = () -> ShooterFeeder.readyToShoot;
    return new SpinFeedShooter(shooter, shooterFeeder, feeder, shooterSupplier, sFeederSupplier, feederSupplier, inTeleop);
  }

  public static SpinFeedShooter autoSpinFeedShoot(Shooter shooter, ShooterFeeder shooterFeeder, Feeder feeder, DoubleSupplier shooterSupplier, DoubleSupplier sFeederSupplier, DoubleSupplier feederSupplier){
    return new SpinFeedShooter(shooter, shooterFeeder, feeder, shooterSupplier, sFeederSupplier, feederSupplier, () -> false);
  }

  public static SpinFeedShooter autoSpinFeedShootWithCoords(Shooter shooter, ShooterFeeder shooterFeeder, Feeder feeder, DoubleSupplier shooterSupplier, DoubleSupplier sFeederSupplier, DoubleSupplier feederSupplier, double x, double y){
    return new SpinFeedShooter(shooter, shooterFeeder, feeder, shooterSupplier, sFeederSupplier, feederSupplier, () -> false, x, y);
  }

  /** Creates a new SpinFeedShooter. */
  public SpinFeedShooter(Shooter shooter, ShooterFeeder shooterFeeder, Feeder feeder, DoubleSupplier shooterSupplier, DoubleSupplier sFeederSupplier, DoubleSupplier feederSupplier, BooleanSupplier endCondition) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.shooter = shooter;
    this.shooterFeeder = shooterFeeder;
    this.feeder = feeder;
    this.shooterSupplier = shooterSupplier;
    this.sFeederSupplier = sFeederSupplier;
    this.feederSupplier = feederSupplier;
    this.endCondition = endCondition;
    addRequirements(shooter);
    addRequirements(shooterFeeder);
    timer = new Timer();
    driverController = new CommandXboxController(0);
  }

  public SpinFeedShooter(Shooter shooter, ShooterFeeder shooterFeeder, Feeder feeder, DoubleSupplier shooterSupplier, DoubleSupplier sFeederSupplier, DoubleSupplier feederSupplier, BooleanSupplier endCondition, double x, double y) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.shooter = shooter;
    this.shooterFeeder = shooterFeeder;
    this.feeder = feeder;
    this.shooterSupplier = shooterSupplier;
    this.sFeederSupplier = sFeederSupplier;
    this.feederSupplier = feederSupplier;
    this.endCondition = endCondition;
    this.x = x;
    this.y = y;
    addRequirements(shooter);
    addRequirements(shooterFeeder);
    timer = new Timer();
    driverController = new CommandXboxController(1);
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
    
    if (DriverStation.getAlliance().get() == Alliance.Red){
      distanceFromSpeaker = Math.abs(SwerveDrive.estimatedX - 8);
    } else if (DriverStation.getAlliance().get() == Alliance.Blue){
      distanceFromSpeaker = SwerveDrive.estimatedX + 8;
    }

    if (DriverStation.isTeleopEnabled()){

      if (distanceFromSpeaker <=3){
        setPoint = Math.abs((-0.75/(Math.pow(Math.E, distanceFromSpeaker))+1)+0.95) * Shooter.maxRPM;
      } else if (distanceFromSpeaker > 3){
        setPoint = Math.abs((-0.75/(Math.pow(Math.E, 2*(distanceFromSpeaker-2))+1)+1)) * Shooter.maxRPM;
      }
      
      shooter.getPidControllerTop().setReference(setPoint, CANSparkMax.ControlType.kVelocity);
      shooter.getPidControllerBottom().setReference(setPoint, CANSparkMax.ControlType.kVelocity);
      
      if (shooter.shooterAtSpeed(setPoint) || driverController.rightBumper().getAsBoolean()){// 
        shooterFeeder.shooterFeeder.set(sFeederSupplier.getAsDouble());
        feeder.setFeederVelocity(feederSupplier.getAsDouble());
      } 

    } else if (DriverStation.isAutonomousEnabled()){
      double setPoint = shooterSupplier.getAsDouble() * Shooter.maxRPM;
      shooter.getPidControllerTop().setReference(setPoint, CANSparkMax.ControlType.kVelocity);
      shooter.getPidControllerBottom().setReference(setPoint, CANSparkMax.ControlType.kVelocity);
      
      boolean shouldSpin = true;

      if (x==1000&&y==1000){
        shouldSpin = true;
      } else if (Math.abs(SwerveDrive.estimatedX-x)<=Math.abs(0.5)&&Math.abs(SwerveDrive.estimatedY-y)<=Math.abs(0.5)){
        shouldSpin = true;
      } else {
        shouldSpin = false;
      }

      if (shooter.shooterAtSpeed(setPoint)){//&& shouldSpin
        shooterFeeder.shooterFeeder.set(sFeederSupplier.getAsDouble());
        feeder.setFeederVelocity(feederSupplier.getAsDouble());
      } 
    } else {
      double setPoint = shooterSupplier.getAsDouble() * Shooter.maxRPM;
      if (shooter.shooterAtSpeed(setPoint)){
        shooterFeeder.shooterFeeder.set(sFeederSupplier.getAsDouble());
        feeder.setFeederVelocity(feederSupplier.getAsDouble());
      } 
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
