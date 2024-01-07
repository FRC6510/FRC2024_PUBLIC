package frc.robot.commands;

import frc.robot.subsystems.Shooter.Shooter;
import frc.robot.subsystems.ShooterFeeder.ShooterFeeder;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class ComplexShooterCommands {
    public static Command spinShooterThenFeeder (Shooter shooter, ShooterFeeder shooterFeeder, DoubleSupplier setPoint){
        return new ParallelCommandGroup(
            shooter.shooterPID(setPoint),
            new ConditionalCommand(
                shooterFeeder.spinShooterFeeder(() -> 1), 
                shooterFeeder.spinShooterFeeder(() -> 0),
                () -> Math.abs(shooter.top.getEncoder().getVelocity()) > Math.abs(setPoint.getAsDouble()) * Shooter.maxRPM * 0.9
            )
        );
    }
}