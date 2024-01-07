// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Intake;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Feeder.Feeder;
import frc.robot.subsystems.lift.Lift;
//Intake for some reason has to be -ve power to intake
public class Intake extends SubsystemBase {
  private static final int INTAKE_CTRE_BUS = 17;
  private final VelocityDutyCycle intakeVelocityRequest;
  public static final double INTAKING_SPEED = 40;
  public static final double INTAKING_SPEED_AUTO = 50;
  //public static boolean intaken;

  public TalonFX intakeMotor;
  /** Creates a new Intake. */
  public Intake() {
    Slot0Configs slot0ConfigsIntake = new Slot0Configs();
    slot0ConfigsIntake.kV = 0.0100;//TODO test for velocity
    slot0ConfigsIntake.kP = 0.02465;
    slot0ConfigsIntake.kI = 0;
    slot0ConfigsIntake.kD = 0;
    this.intakeMotor = new TalonFX(INTAKE_CTRE_BUS, "rio");
    intakeMotor.getConfigurator().apply(slot0ConfigsIntake);
    intakeVelocityRequest = new VelocityDutyCycle(0.0);
    //intaken = false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Intake velocity", intakeMotor.getVelocity().getValueAsDouble());

  }
  
  /**
   * FALSE ADVERTISING. The speed you give it is actually meaningless.
   * @param rps
   * @param feeder
   * @param lift
   * @return
   */
  @Deprecated
  public Command spinIntake(DoubleSupplier rps, Feeder feeder, Lift lift){
    return new SpinIntake(this, feeder, lift, rps);
  }
   
  public Command spinIntakeProper(DoubleSupplier rps, Feeder feeder, Lift lift){
    return new SpinIntakeVariablePower(this, feeder, lift, rps);
  }

   public Command outtakeBriefly(Feeder feeder, Lift lift){
    return spinIntakeProper(()-> -30, feeder, lift).withTimeout(0.5);
  }

  public void stop(){
    intakeMotor.stopMotor();
  }

  public void setIntakeVelocity(DoubleSupplier rps){//TODO test for velocity
    intakeVelocityRequest.Velocity = rps.getAsDouble();
    intakeMotor.setControl(intakeVelocityRequest); 
  }
}
