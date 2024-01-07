// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Shooter;

import java.util.function.DoubleSupplier;
import java.util.function.BooleanSupplier;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
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
  public int noteCounter;
  private Timer timer;

  public static SpinFeedShooter teleopSpinFeedShoot(Shooter shooter, ShooterFeeder shooterFeeder, Feeder feeder, DoubleSupplier shooterSupplier, DoubleSupplier sFeederSupplier, DoubleSupplier feederSupplier){
    BooleanSupplier inTeleop = () -> ShooterFeeder.readyToShoot;
    return new SpinFeedShooter(shooter, shooterFeeder, feeder, shooterSupplier, sFeederSupplier, feederSupplier, inTeleop);
  }

  public static SpinFeedShooter autoSpinFeedShoot(Shooter shooter, ShooterFeeder shooterFeeder, Feeder feeder, DoubleSupplier shooterSupplier, DoubleSupplier sFeederSupplier, DoubleSupplier feederSupplier){
    return new SpinFeedShooter(shooter, shooterFeeder, feeder, shooterSupplier, sFeederSupplier, feederSupplier, () -> false);
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

    if (shooter.shooterAtSpeed()){
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
    System.out.println("finished!");
    shooterFeeder.shooterFeeder.set(0);
    feeder.setFeederVelocity(0);
    shooter.stop();

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return endCondition.getAsBoolean() || noteCounter >= 2 || timer.get() > 0.7;
  }
}
