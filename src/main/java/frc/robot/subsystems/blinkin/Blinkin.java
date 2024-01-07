// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.blinkin;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Blinkin extends SubsystemBase {
  private static final int BLINKIN_PWM_PORT = 9;

  private final Spark blinkin;

// counts the number of flashes
  private double counter;
  private boolean previousState;
  private boolean blinking;
  private Colours colour;

// flashing speed in Hz
  private final static double FREQUENCY = 3.0;

  /** Creates a new Blinkin. */
  public Blinkin() {
    blinkin = new Spark(BLINKIN_PWM_PORT);
    previousState = false;
    blinking = false;
    colour = Colours.OFF;
  }

// mehtod to set colours
  void setColourInternal(Colours colour){
    this.colour = colour;
  }

  void setBlinkingInternal(boolean blinking){
    this.blinking = blinking;
  }

  public Command setColour(Colours colour){
    return new Set(this, () -> colour, () -> blinking);
  }
  public Command setBlinking(boolean blinking){
    return new Set(this, () -> colour, () -> blinking);
  }
  public Command set(Colours colour, boolean blinking){
    return new Set(this, () -> colour, () -> blinking);
  }

  private void set(double val) {
    if ((val >= -1.0) && (val <= 1.0)) {
      blinkin.set(val);
    }
  }

  @Override
  public void periodic() {
      counter++;
      
      if (counter * 0.02 >= (1.0 / FREQUENCY)) {
        // change leds on -> off or off -> on
        counter = 0;
        previousState = !previousState;
        // switches false and true
      }

      if (blinking && previousState) {
        // turn off
        set(Colours.OFF.value());
      }
      else {
        set(colour.value());
      }
  }

  public enum Colours{
    ORANGE(0.65),
    GREEN(0.77),
    OFF(0.99); // value doesn't matter

    private final double value;
    Colours(double value){
      this.value = value;
  }

  double value(){
    return value;
   }
 }
}