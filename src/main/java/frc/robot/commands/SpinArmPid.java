package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Arm;

public class SpinArmPid extends Command {
    private Arm arm;
    private double setpoint;
    private double speed;

    private static final double 
        KP = 0,
        KI = 0,
        KD = 0;
    private final PIDController pid = new PIDController(KP, KI, KD);

    public SpinArmPid(Arm arm, double setpoint) {
		this.arm = arm;
        this.setpoint = setpoint;
        pid.setSetpoint(setpoint);
		addRequirements(arm);
	}

    @Override
	public void initialize() {
	}

	@Override
	public void execute() {        
        speed = pid.calculate(arm.getDegrees(), setpoint);
        arm.getMotor().set(speed);
	}

    @Override
	public void end(boolean interrupted) {
	}

	@Override
	public boolean isFinished() {
		return super.isFinished();
	}

}
