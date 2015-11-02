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
 * @author Matthew Schneider
 * @version 2015-10-31
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
