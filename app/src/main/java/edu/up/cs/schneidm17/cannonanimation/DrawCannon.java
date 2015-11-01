package edu.up.cs.schneidm17.cannonanimation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.widget.SeekBar;
import android.app.Activity;

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
    private Paint paint; //paint for the cannon ball
    private ArrayList<CannonBall> balls;
    private int xSize;
    private int ySize;

    /**
     * Initialize variables
     */
    public DrawCannon() {
        numTicks = 0;
        balls = new ArrayList<>();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
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
    public void tick(Canvas canvas) {
        xSize = canvas.getWidth();
        ySize = canvas.getHeight();
        numTicks++;

        for(int i=0; i<balls.size(); i++) {
            CannonBall ball = balls.get(i);
            if(ball==null) {
                continue;
            }
            float ballXPos = ball.getXPos();
            float ballYPos = ball.getYPos();
            if(ballXPos<0 || ballXPos>xSize) {
                balls.remove(i);
                i--;
                continue;
            }
            canvas.drawCircle(ballXPos+20, ySize-ballYPos-20, 20,paint);
            ball.updateCannonBall();
        }
    }

    public void onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float xLoc = event.getX(); //X location of user touch
            float yLoc = ySize-event.getY(); //Y location of user touch
            double mag = Math.sqrt(xLoc*xLoc + yLoc*yLoc);

            balls.add(new CannonBall(0, 0, 50*xLoc/mag,50*yLoc/mag));
        }
    }
}
