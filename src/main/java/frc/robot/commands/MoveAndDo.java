package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.Feeder.Feeder;
import frc.robot.subsystems.Intake.Intake;
import frc.robot.commands.limelight.LockToHeading;
import frc.robot.subsystems.shooter.Shooter;
// import frc.robot.subsystems.shooter.SpinFeedShooter;
import frc.robot.subsystems.shooterfeeder.ShooterFeeder;
import frc.robot.subsystems.shooterpivot.ShooterPivot;
// import frc.robot.subsystems.shooterpivot.ShooterPivotPIDEndless;
import frc.robot.subsystems.lift.Lift;

//TODO These commands need the updated shooter, intake and feeder commands
public class MoveAndDo{
    
    public static class MoveAndIntake extends SequentialCommandGroup{
        public MoveAndIntake(Pose2d endPosition, SwerveDrive swerveDrive, Intake intake, Feeder feeder, Lift lift) {
            addCommands(
                // new InstantCommand(() -> feeder.setContainsNote(false)),
                // new ParallelCommandGroup(
                // swerveDrive.moveAndTurn(endPosition.getX(), endPosition.getY(), endPosition.getRotation()),
                // intake.spinIntakeProper(() -> Intake.INTAKING_SPEED_AUTO, feeder, lift), 
                // feeder.spinFeeder(() -> Feeder.FEEDER_SPEED))
                );
                // Drive forward the specified distance
            
        }
    }
    public static class StationaryShot extends ParallelDeadlineGroup{

        public static StationaryShot ShootStationary(double shooterSpeed, double pivotAngle, double heading, RobotContainer robotContainer){
            return new StationaryShot(shooterSpeed, pivotAngle, heading, robotContainer.shooter, robotContainer.shooterFeeder, robotContainer.feeder, robotContainer.shooterPivot, robotContainer.swerveDrive);
        }

        public StationaryShot(double shooterSpeed, double pivotAngle, double heading, Shooter shooter, ShooterFeeder shooterFeeder, Feeder feeder, ShooterPivot shooterPivot, SwerveDrive swerveDrive) {
            super(
                // //Deadline bit
                //  SpinFeedShooter.autoSpinFeedShoot(
                //     shooter,
                //     shooterFeeder,
                //     feeder,
                //     swerveDrive,
                //     () -> shooterSpeed,
                //     () -> shooterFeeder.SHOOTER_FEEDER_AUTO_SPEED,
                //     () -> feeder.FEEDER_AUTO_SHOOT_SPEED
                // ),
                // new ShooterPivotPIDEndless(shooterPivot, ()->pivotAngle),
                new LockToHeading(swerveDrive, () -> 0, () -> 0, () -> 0, heading)
                );
           
                // Drive forward the specified distance
            
        }
    }
}