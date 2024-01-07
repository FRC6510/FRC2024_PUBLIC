import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.subsystems.Drivetrain.MotionProfiling;
import frc.robot.subsystems.Drivetrain.SwerveDrive;


public class SwerveDriveTest {
    static final double DELTA = 1e-8;
    private SwerveDrive swerveDrive;
    private MotionProfiling motionProfiling;

    @BeforeEach
    void beforeEach(){
        assert HAL.initialize(500, 0);
        swerveDrive = new SwerveDrive();
    }

    @AfterEach
    void afterEach(){
        swerveDrive = null;
    }

    @Test 
    void testVelocityForwards(){
        motionProfiling = new MotionProfiling(swerveDrive, new Pose2d(0, 0, new Rotation2d(0)));
        motionProfiling.currentPosition = new Pose2d(0, 0, new Rotation2d(0));
        motionProfiling.previousPosition = new Pose2d(-1, 0, new Rotation2d(0));
        motionProfiling.getVelocityVector();
        
        Assertions.assertTrue(motionProfiling.getVelocityVector().equals(new Translation2d(50, 0)));

    }

    @Test 
    void testVelocityBackwards(){
        motionProfiling = new MotionProfiling(swerveDrive, new Pose2d(0, 0, new Rotation2d(0)));
        motionProfiling.currentPosition = new Pose2d(-1, 0, new Rotation2d(0));
        motionProfiling.previousPosition = new Pose2d(0, 0, new Rotation2d(0));
        motionProfiling.getVelocityVector();
        
        Assertions.assertTrue(motionProfiling.getVelocityVector().equals(new Translation2d(-50, 0)));

    }

    @Test 
    void testVelocityDiagonal(){
        motionProfiling = new MotionProfiling(swerveDrive, new Pose2d(0, 0, new Rotation2d(0)));
        motionProfiling.currentPosition = new Pose2d(0, 0, new Rotation2d(0));
        motionProfiling.previousPosition = new Pose2d(-1, -1, new Rotation2d(0));
        motionProfiling.getVelocityVector();
        
        Assertions.assertTrue(motionProfiling.getVelocityVector().equals(new Translation2d(50, 50)));

    }

    @Test
    void testDisplacementVectorForwards(){
        motionProfiling = new MotionProfiling(swerveDrive, new Pose2d(0, 0, new Rotation2d(0)));
        motionProfiling.currentPosition = new Pose2d(0, 0, new Rotation2d(0));
        motionProfiling.robotGoalPoint = new Pose2d(1, 0, new Rotation2d(0));
        motionProfiling.getDisplacementVector();
        
        Assertions.assertTrue(motionProfiling.getDisplacementVector().equals(new Translation2d(1,0)));
    }

    @Test
    void testDisplacementVectorBackwards(){
        motionProfiling = new MotionProfiling(swerveDrive, new Pose2d(0, 0, new Rotation2d(0)));
        motionProfiling.currentPosition = new Pose2d(0, 0, new Rotation2d(0));
        motionProfiling.robotGoalPoint = new Pose2d(-4, 0, new Rotation2d(0));
        motionProfiling.getDisplacementVector();
        
        Assertions.assertTrue(motionProfiling.getDisplacementVector().equals(new Translation2d(-4,0)));
    }

    @Test
    void testDisplacementVectorDiagonal(){
        motionProfiling = new MotionProfiling(swerveDrive, new Pose2d(0, 0, new Rotation2d(0)));
        motionProfiling.currentPosition = new Pose2d(0, 0, new Rotation2d(0));
        motionProfiling.robotGoalPoint = new Pose2d(6, 3, new Rotation2d(0));
        motionProfiling.getDisplacementVector();
        
        Assertions.assertTrue(motionProfiling.getDisplacementVector().equals(new Translation2d(6,3)));
    }

    @Test
    void testDisplacementMagnitude(){
        motionProfiling = new MotionProfiling(swerveDrive, new Pose2d(0, 0, new Rotation2d(0)));
        
        Assertions.assertTrue(motionProfiling.getDisplacementMagnitude(new Translation2d(3.0, 4.0)) == 5.0);
    }

    @Test
    void testProjectionAlongDisplacementForwards(){
        motionProfiling = new MotionProfiling(swerveDrive, new Pose2d(0, 0, new Rotation2d(0)));
        
        Assertions.assertEquals(motionProfiling.getProjectionAlongDisplacement(new Translation2d(9,-6), new Translation2d(3, -2)), Math.sqrt(117), DELTA);
    }

    @Test
    void testProjectionAlongDisplacementDiagonal(){
        motionProfiling = new MotionProfiling(swerveDrive, new Pose2d(0, 0, new Rotation2d(0)));
        
        Assertions.assertEquals(motionProfiling.getProjectionAlongDisplacement(new Translation2d(4,8*Math.sin(Math.PI/3)), new Translation2d(2, 0)), (4.0), DELTA);
    }
    

}
