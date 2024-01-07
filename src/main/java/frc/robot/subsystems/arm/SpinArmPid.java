package frc.robot.subsystems.arm;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class SpinArmPid extends Command {
    private Arm arm;
    private double PIDSetPointSource;

    public SpinArmPid(Arm arm, double PIDSetPointSource) {
		this.arm = arm;
        this.PIDSetPointSource = PIDSetPointSource;
		addRequirements(arm);
	}

    @Override
	public void initialize() {
	}

	@Override
	public void execute() {        
        double setPoint = PIDSetPointSource;
        arm.getPidControllerArm().setReference(setPoint, CANSparkMax.ControlType.kPosition);
        SmartDashboard.putNumber("SetPoint", setPoint);
    }

    @Override
	public void end(boolean interrupted) {
		
	}

	@Override
	public boolean isFinished() {
		return false;
	}

}
