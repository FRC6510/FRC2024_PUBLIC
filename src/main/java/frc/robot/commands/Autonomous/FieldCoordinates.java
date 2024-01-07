package frc.robot.commands.Autonomous;

//This class contains all the field coordinates for autos. Your auto should avoid magic numbers and always refer to this.
//If the auto doesn't work -> consider if these values are reflective of what odometry measures. 
//Can add approximation factor relative to this in code -> that is okay form of magic number
public class FieldCoordinates {
    public static final double DISTANCE_CENTRE_TO_ALLIANCE = 8.26;

    public static final double BLUE_NOTE_RIGHT_X = 2.9856-DISTANCE_CENTRE_TO_ALLIANCE;
    public static final double BLUE_NOTE_RIGHT_Y = 0;
    public static final double BLUE_NOTE_CENTRE_X = 2.9856-DISTANCE_CENTRE_TO_ALLIANCE;
    public static final double BLUE_NOTE_CENTRE_Y = 1.4478;
    public static final double BLUE_NOTE_LEFT_X = 2.9856-DISTANCE_CENTRE_TO_ALLIANCE;
    public static final double BLUE_NOTE_LEFT_Y = 1.4478*2;

    public static final double RED_NOTE_LEFT_Y = 0;
    public static final double RED_NOTE_LEFT_X = 2.9856-DISTANCE_CENTRE_TO_ALLIANCE;
    public static final double RED_NOTE_CENTRE_X = 2.9856-DISTANCE_CENTRE_TO_ALLIANCE;
    public static final double RED_NOTE_CENTRE_Y = -1.4478;
    public static final double RED_NOTE_RIGHT_X = 2.9856-DISTANCE_CENTRE_TO_ALLIANCE;
    public static final double RED_NOTE_RIGHT_Y = -1.4478*2;

    public static final double CENTRE_LINE_X = 0;
    
    public static final double FIELD_CENTRE_LL_Y = 2*1.6764;
    public static final double FIELD_CENTRE_CL_Y = 1.6764;
    public static final double FIELD_CENTRE_C_Y = 0;
    public static final double FIELD_CENTRE_CR_Y = -1.6764;
    public static final double FIELD_CENTRE_RR_Y = 2*-1.6764;

    public static final double BLUE_ROBOT_START_SPEAKER_X = -7.1;
    public static final double BLUE_ROBOT_START_SPEAKER_Y = 0;
    public static final double RED_ROBOT_START_SPEAKER_X = 0;
    public static final double RED_ROBOT_START_SPEAKER_Y = 0;

    public static final double BLUE_ROBOT_START__RIGHT_SPEAKER_X = 0;
    public static final double BLUE_ROBOT_START__RIGHT_SPEAKER_Y = 0;
    public static final double RED_ROBOT_START__LEFT_SPEAKER_X = 0;
    public static final double RED_ROBOT_START__LEFTSPEAKER_Y = 0;

    public static final double STAGE_NOTE_OFFSET = 0.2;
    public static final double NOTE_AVOIDANCE = 1;

    public static final double CENTRE_NOTE_ANGLE = 19;

    public static final double STAGE_TAPE_X = -DISTANCE_CENTRE_TO_ALLIANCE + 5.8725;


    
}
