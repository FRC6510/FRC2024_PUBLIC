// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooterfeeder;

import com.revrobotics.CANSparkBase.IdleMode;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterFeeder extends SubsystemBase {
  private static final int SHOOTER_FEEDER_CAN_BUS = 7;

  public CANSparkFlex shooterFeeder;

  /** Creates a new ShooterFeeder. */
  private ShooterFeeder() {
    shooterFeeder = new CANSparkFlex(SHOOTER_FEEDER_CAN_BUS, MotorType.kBrushless);
    shooterFeeder.setInverted(false);
    shooterFeeder.setIdleMode(IdleMode.kBrake);
  }

  public static final ShooterFeeder INSTANCE = new ShooterFeeder();

  @Override
  public void periodic() {
    SmartDashboard.putNumber("shooter feeder velocity", shooterFeeder.getEncoder().getVelocity());
    // This method will be called once per scheduler run
  }

  public static Command spinShooterFeeder(double power){
    return new SpinShooterFeeder(power);
  } 
}
 // jahle is so cool #epiccc #best ever mech/cad kid