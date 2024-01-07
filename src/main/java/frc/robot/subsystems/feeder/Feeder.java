// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Feeder;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase {
    private static final int FEEDER_CTRE_BUS = 16;
    public TalonFX feeder;
  /** Creates a new Feeder. */
  public Feeder() {
    this.feeder = new TalonFX(FEEDER_CTRE_BUS);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public Command spinFeeder(DoubleSupplier powerSupplier){
    return new SpinFeeder(this, powerSupplier);
  }
}
