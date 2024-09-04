// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.drivebase;

import edu.wpi.first.math.MathSharedStore;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

/**
 * A class that limits the rate of change of an input value. Useful for implementing voltage,
 * setpoint, and/or output ramps. A slew-rate limit is most appropriate when the quantity being
 * controlled is a velocity or a voltage; when controlling a position, consider using a {@link
 * edu.wpi.first.math.trajectory.TrapezoidProfile} instead.
 */
public class SwerveSlewRateLimiter {
  private final double m_positiveRateLimit;
  private final double m_negativeRateLimit;
  private double m_prevVal;
  private double m_prevTime;

  /**
   * Creates a new SlewRateLimiter with the given positive and negative rate limits and initial
   * value.
   *
   * @param positiveRateLimit The rate-of-change limit in the positive direction, in units per
   *     second. This is expected to be positive.
   * @param negativeRateLimit The rate-of-change limit in the negative direction, in units per
   *     second. This is expected to be negative.
   * @param initialValue The initial value of the input.
   */
  public SwerveSlewRateLimiter(double positiveRateLimit, double negativeRateLimit, double initialValue) {
    m_positiveRateLimit = positiveRateLimit;
    m_negativeRateLimit = negativeRateLimit;
    m_prevVal = initialValue;
    m_prevTime = MathSharedStore.getTimestamp();
  }

  /**
   * Creates a new SlewRateLimiter with the given positive rate limit and negative rate limit of
   * -rateLimit.
   *
   * @param rateLimit The rate-of-change limit, in units per second.
   */
  public SwerveSlewRateLimiter(double rateLimit) {
    this(rateLimit, -rateLimit, 0);
  }

  /**
   * Filters the input to limit its slew rate.
   *
   * @param input The input value whose slew rate is to be limited, it will be modified
   */
  public void calculate(ChassisSpeeds input) {
    double currentTime = MathSharedStore.getTimestamp();
    double elapsedTime = currentTime - m_prevTime;
    // vector magnitude of 3 inputs
		double magnitude = Math.sqrt(
				input.vxMetersPerSecond * input.vxMetersPerSecond +
				input.vyMetersPerSecond * input.vyMetersPerSecond +
				input.omegaRadiansPerSecond * input.omegaRadiansPerSecond
		);
    m_prevVal +=
        MathUtil.clamp(
					magnitude - m_prevVal,
					m_negativeRateLimit * elapsedTime,
					m_positiveRateLimit * elapsedTime
				);
    m_prevTime = currentTime;

    // m_prevVal is our new output magnitude
    // we'll scale all parts of input to match the new magnitude
    double scale = m_prevVal / magnitude;
    input.vxMetersPerSecond = input.vxMetersPerSecond * scale;
    input.vyMetersPerSecond = input.vyMetersPerSecond * scale;
    input.omegaRadiansPerSecond = input.omegaRadiansPerSecond * scale;
  }

  /**
   * Returns the value last calculated by the SlewRateLimiter.
   *
   * @return The last value.
   */
  public double lastValue() {
    return m_prevVal;
  }

  /**
   * Resets the slew rate limiter to the specified value; ignores the rate limit when doing so.
   *
   * @param value The value to reset to.
   */
  public void reset(double value) {
    m_prevVal = value;
    m_prevTime = MathSharedStore.getTimestamp();
  }
}
