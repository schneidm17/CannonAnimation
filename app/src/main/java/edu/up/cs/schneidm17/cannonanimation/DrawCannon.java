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
 * class that animates a cannon and ball
 *
 * @author Matthew Schneider
 * @version 2015-10-31
 */
public class DrawCannon implements Animator {
    //instance variables
    private int numTicks; // counts the number of logical clock ticks
    private Paint black; //black for the cannon ball
    private Paint gray; //gray for platform
    private Paint red; //red for targets
    private Paint white; //white for targets
    private Paint orange; //orange for cannon fire
    private Path path; //path for cannon barrel
    private ArrayList<CannonBall> balls;
    private boolean cannonExists;
    private boolean cannonFired;
    private float cannonXPos;
    private float cannonYPos;
    private float target1XPos;
    private float target1YPos;
    private float target2XPos;
    private float target2YPos;
    private int xSize;
    private int ySize;

    private SoundPool soundPool;
    private int fire;
    private int hit;

    /**
     * Initialize variables
     */
    public DrawCannon(CannonMainActivity activity) {
        numTicks = 0;
        cannonExists = true;
        cannonXPos = (float)(1/Math.sqrt(2));
        cannonYPos = (float)(1/Math.sqrt(2));
        balls = new ArrayList<>();
        path = new Path();
        black = new Paint();
        black.setColor(Color.BLACK);
        black.setStyle(Paint.Style.FILL);
        black.setTextSize(50);
        gray = new Paint();
        gray.setColor(0xff404040);
        gray.setStyle(Paint.Style.FILL);
        red = new Paint();
        red.setColor(Color.RED);
        red.setStyle(Paint.Style.FILL);
        white = new Paint();
        white.setColor(Color.WHITE);
        white.setStyle(Paint.Style.FILL);
        orange = new Paint();
        orange.setColor(0xffffaa00);
        orange.setStyle(Paint.Style.FILL);
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        fire = soundPool.load(activity,R.raw.fire, 1);
        hit = soundPool.load(activity,R.raw.hit, 1);
    }

    /**
     * Interval between animation frames: .03 seconds (i.e., about 33 times
     * per second).
     *
     * @return the time interval between frames, in milliseconds.
     */
    public int interval() {
        return 30;
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
        xSize = canvas.getWidth();
        ySize = canvas.getHeight();
        numTicks++;
        target1XPos = 0.6f*xSize;
        target1YPos = 0.4f*ySize+0.2f*ySize*(float)Math.sin(2*Math.toRadians(numTicks));
        target2XPos = 0.8f*xSize;
        target2YPos = 0.6f*ySize+0.2f*ySize*(float)Math.sin(3*Math.toRadians(numTicks));

        //draw explosion
        if(cannonFired) {
            canvas.drawCircle(140 - 20 * cannonYPos + 140 * cannonXPos, ySize - 100 - 20 * cannonXPos - 140 * cannonYPos, 50, orange);
            soundPool.play(this.fire, 0.5f, 0.2f, 1, 0, 1.0f);
            cannonFired=false;
        }

        //draw platform
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
        }

        //draw targets
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
            if(ballXPos<-40 || ballXPos>xSize) {
                balls.remove(i);
                i--;
                continue;
            } else if (target1XPos-30 < ballXPos && ballXPos < target1XPos+10 &&
                    target1YPos-120 < ballYPos && ballYPos < target1YPos+80) {
                ball.hitTarget();
                soundPool.play(this.hit, 0.5f, 0, 1, 0, 1.0f);
            } else if (target2XPos-30 < ballXPos && ballXPos < target2XPos+10 &&
                    target2YPos-120 < ballYPos && ballYPos < target2YPos+80) {
                ball.hitTarget();
                soundPool.play(this.hit, 0, 0.5f, 1, 0, 1.0f);
            } else if (cannonExists && ballXPos<260 && ball.getYVel()<0 && ballYPos<100 && ballYPos>60) {
                cannonExists=false;
                balls.remove(i);
                canvas.drawPaint(orange);
                soundPool.play(this.fire, 1, 1, 1, 0, 1.0f);
                break;
            }

            canvas.drawCircle(ballXPos + 20, ySize - ballYPos - 20, 20, black);
            ball.updateCannonBall();
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
        }
    }

    public void onTouch(MotionEvent event) {
        if(cannonExists) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if(event.getX() >= 260 || event.getY() <= (ySize-200))
                {
                    cannonXPos = event.getX() - 120 - 20 * cannonYPos; //X location of user touch
                    cannonYPos = ySize - event.getY(); //Y location of user touch
                    double mag = Math.sqrt(cannonXPos * cannonXPos + cannonYPos * cannonYPos);
                    cannonXPos /= mag;
                    cannonYPos /= mag;
                }

                balls.add(new CannonBall(120-20*cannonYPos+100*cannonXPos,80+20*cannonXPos+100*cannonYPos, 50 * cannonXPos, 50 * cannonYPos));
                cannonFired=true;
            }
        }
    }
}
