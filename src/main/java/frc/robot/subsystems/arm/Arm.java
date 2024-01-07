package frc.robot.subsystems.arm;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.SparkPIDController;

import java.util.function.BooleanSupplier;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Arm extends SubsystemBase{

  private static final int NOTE_DETECTOR_DIO_CHANNEL = 0;
  private final DigitalInput noteDetector;
  
   private static final int ARM_CAN_BUS = 3;
   public CANSparkFlex armMotor;

   private SparkPIDController pidControllerArm;
   public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;
   public static double maxRPM;

   public Arm(){

    super();

    this.noteDetector = new DigitalInput(NOTE_DETECTOR_DIO_CHANNEL);

    armMotor = new CANSparkFlex(ARM_CAN_BUS, MotorType.kBrushless);
    this.pidControllerArm = armMotor.getPIDController();

    armMotor.setIdleMode(IdleMode.kCoast);
    armMotor.getEncoder().setPosition(0);

    kP = 0.05; 
    kI = 0;
    kD = 0; 
    kIz = 0;  
    kMaxOutput = 1; 
    kMinOutput = -1;
    maxRPM = 6784;

    pidControllerArm.setP(kP);
    pidControllerArm.setI(kI);
    pidControllerArm.setD(kD);
    pidControllerArm.setIZone(kIz);
    pidControllerArm.setFF(kFF);
    pidControllerArm.setOutputRange(kMinOutput, kMaxOutput);
  
    SmartDashboard.putNumber("P Gain", kP);
    SmartDashboard.putNumber("I Gain", kI);
    SmartDashboard.putNumber("D Gain", kD);
    SmartDashboard.putNumber("I Zone", kIz);
    SmartDashboard.putNumber("Feed Forward", kFF);
    SmartDashboard.putNumber("Max Output", kMaxOutput);
    SmartDashboard.putNumber("Min Output", kMinOutput);

    }


@Override
   public void periodic() {

    SmartDashboard.putBoolean("noteDetector", getSensorState());

    SmartDashboard.putNumber("Arm Position", armMotor.getEncoder().getPosition());

   }

   public Command spinArmPid(double PIDSetPointSource){
       return new SpinArmPid(this, PIDSetPointSource);
   }

   public Command moveArm(double power){
       return new MoveArm(this, power);
   }

   public SparkPIDController getPidControllerArm() {
    return pidControllerArm;
  }

  public BooleanSupplier armAtTarget(double PIDSetPointSource) {
    if ((Math.abs(armMotor.getEncoder().getPosition() - PIDSetPointSource)) < 5) {
      return () -> true;
    }
    else {
      return () -> false;
    }
  }

  public boolean getSensorState() {
    return noteDetector.get();
  }

}