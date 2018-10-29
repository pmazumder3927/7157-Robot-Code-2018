
public class RobotPose {
    private volatile double x, y, theta;
    private volatile double lastDistance, deltaDistance, lastTheta, deltaX, deltaY;

    public RobotPose(){
        this.x = 0;
        this.y = 0;
        this.theta = 0;

        this.lastDistance =0;
        this.deltaDistance = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void updatePose(double distance, double gyro) {
        deltaDistance = distance - lastDistance;
        theta = Math.toRadians(gyro);

        //Smallest angle between two angles
        double dTheta = Math.atan2(Math.sin(theta - lastTheta), Math.cos(theta - lastTheta));

        //Find change in position relative to robot
        //(The robot moved this amount if you are looking from the POV of the robot)
        //The X and Y here are not representative of the coordinate system
        //Swapping X and Y here requires swapping X and Y below
        deltaX = Math.cos(dTheta / 2) * deltaDistance;
        deltaY = Math.sin(dTheta / 2) * deltaDistance;

        //Swap x and y to swap the two axises;
        //Rotate change in x and y to robot's old angle
        x += deltaX * Math.cos(lastTheta) - deltaY * Math.sin(lastTheta);
        y += deltaX * Math.sin(lastTheta) + deltaY * Math.cos(lastTheta);

        lastDistance = distance;
        lastTheta = theta;
    }
}