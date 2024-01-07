package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Arm;

public class SpinArm extends Command {
	private final Arm arm;
	private final double speed;

	public SpinArm(Arm arm, double speed) {
		this.arm = arm;
		this.speed = speed;
		addRequirements(arm);
	}
	@Override
	public void initialize() {
		arm.getMotor().set(speed);
	}

	@Override
	public void execute() {
	}

	@Override
	public void end(boolean interrupted) {
		arm.getMotor().set(0);
	}

	@Override
	public boolean isFinished() {
		return super.isFinished();
	}
}