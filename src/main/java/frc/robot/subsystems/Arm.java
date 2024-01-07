package frc.robot.subsystems;


import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.PidConfig;
import frc.robot.commands.SpinArm;


public class Arm extends SubsystemBase{
    private final double minAngle = 0.0;
    private final double maxAngle = 0.0;
    private final double angleOffset = 0.0;
    private final boolean isTunable = false;

   private final CANSparkFlex leftMotor = new CANSparkFlex(45, MotorType.kBrushless);
   private final CANSparkFlex rightMotor = new CANSparkFlex(46, MotorType.kBrushless);
   private final DutyCycleEncoder throughboreEncoder = new DutyCycleEncoder(0);


   private double kp = 0.01;
   private double minPowerAtLevel = 0.025;
   private double minPowerAtExtended = 0.03;
   private double setpoint = minAngle;
   private double setpointIncrementer = 0.5;
   private double motorOutput = 0.0;
   private double maxPIDSpeed = 0.3;
   private double downSpeed = -0.2;
   private double speed;

   public Arm(){
    pid.setTolerance(5.0);

    leftMotor.setInverted(false);
    rightMotor.follow(leftMotor,true);

    setSetpoint(minAngle);
   }


   private final PidConfig pidConfig = new PidConfig("arm", kp, isTunable);
   private final PIDController pid = new PIDController(pidConfig.getKp(),pidConfig.getKi(),pidConfig.getKd());


   private boolean usingPID = true;
   private boolean settingMinLevel = true;


   public void raise(){
    if(usingPID){
        double tempSetpoint = setpoint += setpointIncrementer;
        setSetpoint(tempSetpoint);
    } else{
        motorOutput = 0.2;
    }
    }


    public void lower(){
        if(usingPID){
            double tempSetpoint = setpoint -= setpointIncrementer;
            setSetpoint(tempSetpoint);
        } else {
            motorOutput = -0.2;
        }
    }


    public void stop(){
        if(!usingPID){
            motorOutput = 0.0;
        }
    }


    public double getFeedForward(){
        return Math.cos(Math.toRadians(getDegrees()))*getMinPower();
    }

    public double getMinPower(){
        return (minPowerAtExtended - minPowerAtLevel)*1 + minPowerAtLevel;
    }


    public double getPidOutput(){
        speed = pid.calculate(getDegrees(), setpoint) + getFeedForward();
        if(setpoint + 10 < getDegrees()){
            if(isTunable){
                downSpeed = SmartDashboard.getNumber("down speed", downSpeed);
            }
            return downSpeed;
        }
        if(speed >= maxPIDSpeed){
            return maxPIDSpeed;
        }
        return speed;
    }


    public double getDegrees(){
        return(throughboreEncoder.getAbsolutePosition()-0.5)*-1.0*360.0-angleOffset;
    }


    public void setSetpoint(double newSetpoint){
        if (newSetpoint > maxAngle){
            setpoint = maxAngle;
        } else if (newSetpoint < minAngle){
            setpoint = minAngle;
        } else{
            setpoint = newSetpoint;
        }
    }


    public boolean atSetpoint(){
        return pid.atSetpoint();
    }




   @Override
   public void periodic() {
    if(usingPID){
        leftMotor.set(getPidOutput());
    } else {
        if(getDegrees() >= maxAngle) motorOutput = 0.0;
        if(getDegrees() <= minAngle) motorOutput = 0.0;
        if(settingMinLevel) motorOutput = minPowerAtLevel;
        leftMotor.set(motorOutput);
    }
   }

      public CANSparkFlex getMotor(){
       return leftMotor;
   }


   public Command spinArm(double speed){
       return new SpinArm(this, speed);
   }

}
