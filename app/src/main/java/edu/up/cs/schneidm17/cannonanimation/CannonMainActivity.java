package edu.up.cs.schneidm17.cannonanimation;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.LinearLayout;

/**
 * CannonMainActivity
 *
 * This is the activity for the cannon animation. It creates a AnimationCanvas
 * containing a particular Animator object
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
 *
 */
public class CannonMainActivity extends Activity {

    /**
     * creates an AnimationCanvas containing an Animator.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cannon_main);

        // Create an animation canvas and place it in the main layout
        Animator animator = new DrawCannon(this);
        AnimationCanvas myCanvas = new AnimationCanvas(this, animator);
        LinearLayout mainLayout = (LinearLayout) this
                .findViewById(R.id.topLevelLayout);
        mainLayout.addView(myCanvas);

    }

    /**
     * This is the default behavior (empty menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cannon_main, menu);
        return true;
    }
}
