package frc.robot.subsystems.example;

import edu.wpi.first.wpilibj2.command.Command;

class ExampleSimpleCommand extends Command {
	private final ExampleSubsystem exampleSubsystem;
	ExampleSimpleCommand(ExampleSubsystem exampleSubsystem) {
		this.exampleSubsystem = exampleSubsystem;

		addRequirements(exampleSubsystem);
	}

	@Override
	public void initialize() {}

	@Override
	public void execute() {}

	@Override
	public void end(boolean interrupted) {}

	@Override
	public boolean isFinished() {
		return true;
	}
}
