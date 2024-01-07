package frc.robot.subsystems.example;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

class ExampleSubsystem extends SubsystemBase {
	private ExampleSubsystem() {}
	@Override
	public void periodic() {
	}

	/**
	 * sugar method that makes a new {@link ExampleSimpleCommand} and passes this as the parameter
	 */
	public Command simpleCommand() {
		return new ExampleSimpleCommand(this);
	}
}
