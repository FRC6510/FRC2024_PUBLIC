package frc.robot.subsystems;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.SpinArm;
import frc.robot.commands.SpinArmPid;


public class Arm extends SubsystemBase{
   private static final int LEFT_CAN_ID = 1;
   private static final int RIGHT_CAN_ID = 2;

   private final CANSparkFlex leftMotor = new CANSparkFlex(LEFT_CAN_ID, MotorType.kBrushless);
   private final CANSparkFlex rightMotor = new CANSparkFlex(RIGHT_CAN_ID, MotorType.kBrushless);

   public Arm(){
        leftMotor.setInverted(false);
        rightMotor.follow(leftMotor,true);
    }


    public double getDegrees(){
        return leftMotor.getEncoder().getPosition();
    }

@Override
   public void periodic() {
   }
   
    public CANSparkFlex getMotor(){
       return leftMotor;
   }

    public Command spinArm(double speed){
       return new SpinArm(this, speed);
   }

   public Command spinArmPid(double setpoint){
       return new SpinArmPid(this, setpoint);
   }
}