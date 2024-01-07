// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.ShooterFeeder;

import java.util.function.DoubleSupplier;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterFeeder extends SubsystemBase {
  private static final int SHOOTER_FEEDER_CAN_BUS = 7;
  public CANSparkFlex shooterFeeder;
  /** Creates a new ShooterFeeder. */
  public ShooterFeeder() {
    shooterFeeder = new CANSparkFlex(SHOOTER_FEEDER_CAN_BUS, MotorType.kBrushless);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public Command spinShooterFeeder(DoubleSupplier powerSupplier) {
    return new SpinShooterFeeder(this, powerSupplier);
  }
}