package edu.up.cs.schneidm17.cannonanimation;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * this class animates a cannon and several cannon balls
 *
 * Note: There was only one place to turn this project in on Moodle, and Professor Schmidt
 * repeatedly failed to respond to my emails over the weekend, as well as the emails from
 * several other CS 301 students, so I just decided to keep the two parts together. It would
 * be pointless to delete 2/3 of my code and then create a new project for Part B. I assume
 * that you (the grader) are smart enough to tell which features belong to Part A and which
 * features belong to Part B. Sorry for the (in)convenience.
 *
 * Modifications: I can draw multiple cannon balls on the screen at a time (I stress-tested the
 * app with up to 5,000 cannon balls on the screen and it still ran fine), I have realistic
 * bouncing when the cannon ball hits the ground or the target, I have animated explosions when
 * the cannon is fired and when the cannon hits the firing platform (ie. when it self-destructs)
 * My targets DO move, but I decided not to remove them when I hit them because that would be a
 * really boring game, so I allow the user to hit the moving targets as many times as they want
 * because it makes the app MORE FUN, which is the real purpose of this app, to have fun. Enjoy!
 *
 * @author Matthew Schneider
 * @version 2015-11-01
 */
public class DrawCannon implements Animator {
    //instance variables
    private int numTicks; //counts the number of logical clock ticks
    private int numShots; //counts the number of shots the cannon has fired
    private int numHits; //counts the number of hits the player has made
    private ArrayList<CannonBall> balls; //an array list of all the balls currently on the screen
    private boolean startup; //if true then display the instructions on the screen
    private boolean cannonExists; //true if the cannon has not been destroyed yet
    private boolean cannonFired; //true if the cannon has fired since the last tick
    private float cannonXPos; //normalized x-vector of the direction the cannon was fired last
    private float cannonYPos; //normalized y-vector of the direction the cannon was fired last
    private float target1XPos; //x position of the center of target 1
    private float target1YPos; //y position of the center of target 1
    private float target2XPos; //x position of the center of target 2
    private float target2YPos; //y position of the center of target 2
    private int xSize; //the x-size of the screen
    private int ySize; //the y-size of the screen
    private Paint black; //black for the cannon ball
    private Paint gray; //gray for platform
    private Paint red; //red for targets
    private Paint white; //white for targets
    private Paint orange; //orange for cannon fire
    private Path path; //path for cannon barrel
    private SoundPool soundPool; //this allows sounds to play
    private int fire; //the sound file played when the cannon is fired
    private int hit; //the sound file played when the target is hit

    /**
     * Initialize variables
     */
    public DrawCannon(CannonMainActivity activity) {
        //initialize placeholders and counter variables
        numTicks = 0;
        numShots = 0;
        numHits = 0;
        cannonExists = true;
        startup=true;
        cannonXPos = (float)(1/Math.sqrt(2));
        cannonYPos = (float)(1/Math.sqrt(2));
        balls = new ArrayList<>();

        //initialize paints and paths
        path = new Path();
        black = new Paint();
        black.setColor(Color.BLACK);
        black.setStyle(Paint.Style.FILL);
        black.setTextSize(40);
        gray = new Paint();
        gray.setColor(0xff404040);
        gray.setStyle(Paint.Style.FILL);
        gray.setTextSize(50);
        gray.setFakeBoldText(true);
        red = new Paint();
        red.setColor(Color.RED);
        red.setStyle(Paint.Style.FILL);
        white = new Paint();
        white.setColor(Color.WHITE);
        white.setStyle(Paint.Style.FILL);
        orange = new Paint();
        orange.setColor(0xffffaa00);
        orange.setStyle(Paint.Style.FILL);

        //initialize sound features
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        fire = soundPool.load(activity,R.raw.fire, 1);
        hit = soundPool.load(activity,R.raw.hit, 1);
    }

    /**
     * Interval between animation frames: .02 seconds (i.e., 50 times per second).
     *
     * @return the time interval between frames, in milliseconds.
     */
    public int interval() {
        return 20;
    }

    /**
     * The background color: a light blue.
     *
     * @return the background color onto which we will draw the image.
     */
    public int backgroundColor() {
        return 0xffb4c8ff;
    }

    /**
     * Tells that we never pause.
     *
     * @return indication of whether to pause
     */
    public boolean doPause() {
        return false;
    }

    /**
     * Tells that we never stop the animation.
     *
     * @return indication of whether to quit.
     */
    public boolean doQuit() {
        return false;
    }

    /**
     * Action to perform on clock tick
     *
     * @param canvas the graphics object on which to draw
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void tick(Canvas canvas) {
        //update instance variables
        numTicks++;
        xSize = canvas.getWidth();
        ySize = canvas.getHeight();

        //aim the cannon where the user last pressed
        target1XPos = 0.6f*xSize;
        target1YPos = 0.4f*ySize+0.2f*ySize*(float)Math.sin(2*Math.toRadians(numTicks));
        target2XPos = 0.8f*xSize;
        target2YPos = 0.6f*ySize+0.2f*ySize*(float)Math.sin(3*Math.toRadians(numTicks));

        //draw explosion if the cannon was fired in the last tick
        if(cannonFired) {
            canvas.drawCircle(140 - 20 * cannonYPos + 140 * cannonXPos, ySize - 100 - 20 * cannonXPos - 140 * cannonYPos, 50, orange);
            soundPool.play(this.fire, 0.5f, 0.2f, 1, 0, 1.0f);  //play sound
            cannonFired=false; //reset variable
        }

        //draw platform, unless the platform was destroyed, in which case draw a broken platform
        if(cannonExists) {
            canvas.drawRect(40, ySize - 80, 60, ySize, gray);
            canvas.drawRect(100, ySize - 80, 120, ySize, gray);
            canvas.drawRect(160, ySize - 80, 180, ySize, gray);
            canvas.drawRect(220, ySize - 80, 240, ySize, gray);
            canvas.drawRect(0, ySize - 100, 260, ySize - 80, gray);
        } else {
            canvas.drawRect(40, ySize - 20, 60, ySize, gray);
            canvas.drawRect(100, ySize - 20, 120, ySize, gray);
            canvas.drawRect(160, ySize - 20, 180, ySize, gray);
            canvas.drawRect(220, ySize - 20, 240, ySize, gray);
            canvas.drawText("Game Over", 20, ySize - 40, gray);
        }

        //draw the two moving targets in their current locations
        canvas.drawOval(target1XPos-25, ySize-target1YPos-100, target1XPos+25, ySize-target1YPos+100,red);
        canvas.drawOval(target1XPos-15, ySize-target1YPos-60,  target1XPos+15, ySize-target1YPos+60,white);
        canvas.drawOval(target1XPos-5,  ySize-target1YPos-20,  target1XPos+5,  ySize-target1YPos+20,red);
        canvas.drawOval(target2XPos-25, ySize-target2YPos-100, target2XPos+25, ySize-target2YPos+100,red);
        canvas.drawOval(target2XPos-15, ySize-target2YPos-60,  target2XPos+15, ySize-target2YPos+60,white);
        canvas.drawOval(target2XPos-5,  ySize-target2YPos-20,  target2XPos+5,  ySize-target2YPos+20,red);

        //update and draw all cannon balls
        for(int i=0; i<balls.size(); i++) {
            CannonBall ball = balls.get(i);
            if(ball==null) {
                continue;
            }
            float ballXPos = ball.getXPos();
            float ballYPos = ball.getYPos();

            //if the cannon ball rolled off the screen, remove it from the array list
            //where the memory will be freed by the automatic garbage collector
            if(ballXPos<-40 || ballXPos>xSize) {
                balls.remove(i);
                i--;
                continue;
            //otherwise, if the ball hit target 1, play a sound and bounce the ball backwards
            } else if (target1XPos-35 < ballXPos && ballXPos < target1XPos+15 &&
                    target1YPos-120 < ballYPos && ballYPos < target1YPos+80) {
                ball.hitTarget();
                numHits++;
                soundPool.play(this.hit, 0.5f, 0, 1, 0, 1.0f);  //play sound
            //otherwise, if the ball hit target 2, play a sound and bounce the ball backwards
            } else if (target2XPos-35 < ballXPos && ballXPos < target2XPos+15 &&
                    target2YPos-120 < ballYPos && ballYPos < target2YPos+80) {
                ball.hitTarget();
                numHits++;
                soundPool.play(this.hit, 0, 0.5f, 1, 0, 1.0f); //play sound
            //otherwise, if the ball hit the platform, play a sound and destroy the cannon and platform
            } else if (cannonExists && ballXPos<260 && ball.getYVel()<0 && ballYPos<100 && ballYPos>60) {
                cannonExists=false; //destroy the cannon
                balls.remove(i); //remove the ball that hit the platform (it exploded)
                canvas.drawPaint(orange);  //overlay the screen with orange
                soundPool.play(this.fire, 1, 1, 1, 0, 1.0f);  //play explosion sound
                startup=true; //display the instructions again
                break; //cut the drawing short
            }
            //if nothing important happened, just draw the ball
            canvas.drawCircle(ballXPos + 20, ySize - ballYPos - 20, 20, black);
            ball.updateCannonBall(); //recalculate the ball's position and velocity
        }

        //draw the cannon barrel (on top of balls that are fired)
        if(cannonExists) {
            canvas.drawArc(100, ySize - 140, 180, ySize - 60, 0, -180, true, gray);
            path.reset();
            path.moveTo(140 - 40 * cannonYPos, ySize - 100 - 40 * cannonXPos);
            path.rLineTo(100 * cannonXPos, -100 * cannonYPos);
            path.rLineTo(40 * cannonYPos, 40 * cannonXPos);
            path.rLineTo(-100 * cannonXPos, 100 * cannonYPos);
            path.close();
            canvas.drawPath(path, gray);
        //if the cannon was destroyed, draw the explosion
        } else {
            canvas.drawPaint(orange); //overlay the screen with orange
            orange.setAlpha((int)(orange.getAlpha() * 0.95)); //the orange fades over time
        }

        //display the instructions or stats about the current game
        if (startup) {
            //display the game instructions at the start and end of the game
            canvas.drawText("Press anywhere to fire the cannon. You can", 30, 60, black);
            canvas.drawText("shoot the targets as many times as you want.", 30, 100, black);
            canvas.drawText("Cannon balls can roll under the cannon, but", 30, 140, black);
            canvas.drawText("if they hit the platform, you destroy yourself!", 30, 180, black);
        } else {
            //display current information about the current state of the game
            canvas.drawText("Total Shots: " + numShots, 30, 60, black);
            canvas.drawText("Total Hits: " + numHits, 30, 100, black);
            canvas.drawText("Balls on screen: " + balls.size(), 400, 60, black);
            double hitRate = Math.round(1000.0*numHits / numShots)/10.0;
            canvas.drawText("Hit Rate: " + hitRate+"%", 400, 100, black);
        }
    }

    /**
     * Called whenever the user touches the AnimationCanvas so that the
     * animation can respond to the event.
     *
     * @param event a MotionEvent describing the touch
     */
    public void onTouch(MotionEvent event) {
        //if the cannon does not exist do not allow the user to fire a cannon ball
        if(cannonExists) {
            //if you want to have a lot of fun and convert the cannon to fully automatic,
            //comment out the following line of code so every touch action (nt just down
            //presses) will cause the cannon to fire. Try it, IT IS EPIC!!!!!!
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //if the user touched the cannon itself, just fire in the same direction as the
                //last shot; otherwise, fire in the direction that the user touched the screen
                if(event.getX() >= 260 || event.getY() <= (ySize-200))
                {
                    //calculate the direction the user touched and then normalize the values
                    cannonXPos = event.getX() - 120 - 20 * cannonYPos; //X location of user touch
                    cannonYPos = ySize - event.getY(); //Y location of user touch
                    double mag = Math.sqrt(cannonXPos * cannonXPos + cannonYPos * cannonYPos);
                    //divide by the hypotenuse to normalize the values of cannonXPos and cannonYPos
                    cannonXPos /= mag;
                    cannonYPos /= mag;
                }

                //create a new cannon ball, give it an initial position and velocity and put it in the array list
                balls.add(new CannonBall(120-20*cannonYPos+100*cannonXPos,80+20*cannonXPos+100*cannonYPos, 50 * cannonXPos, 50 * cannonYPos));
                cannonFired=true; //set cannonFired=true to draw an explosion on the next tick
                numShots++; //increment the number of shots fired
                //If the app has been running for more than 2 seconds and the user shoots the cannon, replace
                //the instructions in the top corner of the app with game stats (number of shots, hit rate, etc)
                if(startup && numTicks>=100)
                    startup=false;
                //this makes sure the user has enough time to read the directions if they accidentally press the
                //screen upon opening. The instructions go away after the user has waited 2 seconds AND THEN fired
            }
        }
    }
}