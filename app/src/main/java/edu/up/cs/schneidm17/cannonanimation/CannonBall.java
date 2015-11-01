package edu.up.cs.schneidm17.cannonanimation;

/**
 * class that animates a cannon ball
 *
 * @author Matthew Schneider
 * @version 2015-10-31
 */
public class CannonBall {
    //instance variables
    private double xPos; //current X position of the cannon ball (in pixels)
    private double yPos; //current Y position of the cannon ball (in pixels)
    private double xVel; //current X velocity of the cannon ball (in pixels/tick)
    private double yVel; //current Y velocity of the cannon ball (in pixels/tick)

    //class variables
    public static double acceleration=-1; //acceleration of this world (in pixels/tick^2)
    public static double xWS=0; //X wind speed of this world (in pixels/tick)
    public static double yWS=0; //Y wind speed of this world (in pixels/tick)

    public CannonBall(double initXPos, double initYPos, double initXVel, double initYVel) {
        this.xPos = initXPos;
        this.yPos = initYPos;
        this.xVel = initXVel;
        this.yVel = initYVel;
    }

    public float getXPos() {
        return (float)xPos;
    }

    public float getYPos() {
        return (float)yPos;
    }

    public void updateCannonBall() {
        xPos += xVel + xWS;
        yVel += acceleration;
        yPos += yVel + yWS;
        if(yPos<0) {
            yPos=0;
            hitGround();
        }
    }

    public void hitGround() {
        yVel *= -0.7;
        if(xVel<1 && xVel>=0)
            xVel=1;
        else if(xVel<0 && xVel>=-1)
            xVel=-1;
    }

    public void hitTarget() {
        xVel *= -0.7;
    }
}