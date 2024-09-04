# Current Goals:
- Finish all robot code by adding subsystems and implementing them in autonomous

# Writing swerve code:

## Kinematics / actually driving
~- make a module class~
~- find pids in module class~
~- set up modules in swerve drive class~
    ~- add dashboard info~
    ~- find canIDs for motors and encoders~
    ~- find encoder offsets~
~- find module starting positions, and add them to swerve drive kinematics~
~- add module updates to swerve drive periodic~
~- methods for driving the robot:~
    ~- field centric~
    ~- robot centric~
~- commands for driving~
    ~- drive, ... (DoubleSupplier OR just directly take a gamepad)~
~- driver control enhancements:~
    ~- slowmode - static?~
    - slowmode - adaptive?
~- test~
    ~- fully set up code to test~

## Extra notes:
~- need to add in robot container the binding set up~
~- add a line to set the imu to 0~

### Later:
~- driver control enhancements:~
    ~- slew rate limiter~
        ~-create a slew rate limiter~
        ~- use the slew rate limiter~
    ~- lock to heading~
    ~- heading from limelight~

## Odometry
~- needs the kinematics to be done first~

## Path Planner
~- Two simple paths to run and test~
~- Add path planner library to code~
~- Integrate path planner to the code~
~- Set up and plan the paths ~
~- Experiment with more path planner stuff (if we can get the hall, plan an auto from last season)~

- Named commands

## Autonomous
~- Motion profile PID to point~
    ~- chose not to do ~
~- Planning library~

## Slew Rate Limiter Notes
- MAYBE remove slew rate limiter to the heading and just do it to the x and y velocity (driver feedback based)
- need to find a good value (driver feedback based)
- Finding battery drops and spikes (no ty)
    - Record data and analytics to do so

## Limelight
~- Mount limelight - test limelight relocalisation~
~- Modify the limelight relocalisation if 
  we can see two or more tags and the resulting pose is within 1 metre of our current botpose~


