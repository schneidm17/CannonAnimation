package edu.up.cs.schneidm17.cannonanimation;

/**
 * this class stores the instance variables (position, velocity, and acceleration) for a cannon ball
 *
 * NOTE to the grader: I ran out of time to implement a slider to adjust acceleration or wind speed
 * on the screen, but if you change the variables acceleration, xWindSpeed and yWindSpeed, all the
 * cannon balls on the screen are affected by the changes... I just ran out of time to add a slider
 *
 * @author Matthew Schneider
 * @version 2015-11-01
 */
public class CannonBall {
    //instance variables
    private double xPos; //current X position of the cannon ball (in pixels)
    private double yPos; //current Y position of the cannon ball (in pixels)
    private double xVel; //current X velocity of the cannon ball (in pixels/tick)
    private double yVel; //current Y velocity of the cannon ball (in pixels/tick)

    //class variables
    public static double acceleration=-1; //acceleration of this world (in pixels/tick^2)
    public static double xWindSpeed =0; //X wind speed of this world (in pixels/tick)
    public static double yWindSpeed =0; //Y wind speed of this world (in pixels/tick)

    /**
     * constructor for a cannon ball
     * @param initXPos initial x position
     * @param initYPos initial y position
     * @param initXVel initial x velocity
     * @param initYVel initial y velocity
     */
    public CannonBall(double initXPos, double initYPos, double initXVel, double initYVel) {
        //initialize the instance variables
        this.xPos = initXPos;
        this.yPos = initYPos;
        this.xVel = initXVel;
        this.yVel = initYVel;
    }

    /**
     * get this cannon ball's x position
     * @return x velocity
     */
    public float getXPos() {
        return (float)xPos;
    }

    /**
     * get this cannon ball's y position
     * @return y velocity
     */
    public float getYPos() {
        return (float)yPos;
    }

    /**
     * get this cannon ball's x velocity
     * @return x velocity
     */
    public double getXVel() {
        return xVel;
    }

    /**
     * get this cannon ball's y velocity
     * @return y velocity
     */
    public double getYVel() {
        return yVel;
    }

    /**
     * update the ball's coordinates at every tick
     */
    public void updateCannonBall() {
        xPos += xVel + xWindSpeed;
        yVel += acceleration;
        yPos += yVel + yWindSpeed;
        if(yPos<0) {
            yPos=0;
            hitGround();
        }
    }

    /**
     * if the ball hits the ground, have it bounce up
     */
    public void hitGround() {
        yVel *= -0.5; //decrease the ball's kinetic energy
        //make sure that the cannon ball does not just sit there and do nothing:
        //force the ball to roll very slowly off the screen so the program cannot
        //be overrun with stationary cannon balls sitting on the ground and crash
        if(xVel<1 && xVel>=0)
            xVel=1;
        else if(xVel<0 && xVel>=-1)
            xVel=-1;
    }

    /**
     * if the target is hit, have it bounce backwards
     */
    public void hitTarget() {
        xVel *= -0.2; //decrease the ball's kinetic energy
        yVel *= 0.6;
        //push the ball bach slightly so it does not "double bounce" off a target
        //this makes sure the ball bounces cleanly off the target after hitting it
        if(xVel<0)
            xPos -= 40;
        else
            xPos += 40;
    }
}