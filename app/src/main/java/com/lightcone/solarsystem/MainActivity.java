package com.lightcone.solarsystem;

import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private static final double delayScaler = 1.2;
    private static final double zoomScaler = 1.1;
    private static final int BACKGROUND_COLOR = Color.argb(255, 0, 0, 0);
    private KeplerRunner krunner;
    Toolbar toolbar;
    LinearLayout LL1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* In the following we lay out the screen entirely in code (activity_main.xml
       * isn't used).  We wish to lay out a stage for planetary motion using LinearLayout.
       * The instance krunner of KeplerRunner is added to a LinearLayout LL1 using addView.
       * Then we use setContent to set the content view to LL1. The formatting of the layouts
       * is controlled using the LinearLayout.LayoutParams lp.
       * */

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 100);

        LL1 = new LinearLayout(this);
        LL1.setOrientation(LinearLayout.VERTICAL);

        // Create top toolbar using code rather than xml.  Note that this assumes that in styles.xml
        // a no action bar theme is set: <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">

        // Instantiate a Toolbar from its constructor and add properties to it
        toolbar = new Toolbar(this);

        // Set background color.  Handle method getColor deprecated as of API 23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
        } else {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        toolbar.setNavigationIcon(R.drawable.solar_system_icon);
        toolbar.setTitle("");

        // Attach the toolbar to the view
        LL1.addView(toolbar);

        // Set the toolbar as the ActionBar for this window
        setSupportActionBar(toolbar);

        // Instantiate the class MotionRunner to define the entry screen display and add it
        // to the view.

        krunner = new KeplerRunner(this);
        krunner.setLayoutParams(lp);
        krunner.setBackgroundColor(BACKGROUND_COLOR);
        LL1.addView(krunner);

        // Set the view as the display

        setContentView(LL1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the toolbar menu
        toolbar.inflateMenu(R.menu.main);

        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop animation loop if going into background
        krunner.stopLooper();
        Log.i("ANIM", "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        // Resume animation loop
        krunner.startLooper();
        Log.i("ANIM", "onResume");
    }

    // Process action bar menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.i("ANIM", "OptionItemsSelected");
        // Handle item selection
        switch (item.getItemId()) {

            // Run slower
            case R.id.speed_decrease:
                krunner.setDelay(delayScaler);
                return true;

            // Run faster
            case R.id.speed_increase:
                long test = krunner.setDelay(1 / delayScaler);

                // Method setDelay() returns -2 if new delay would be < 1
                if (test == -2) {
                    Toast.makeText(this, "Maximum speed. Can't increase",
                            Toast.LENGTH_SHORT).show();
                }
                return true;

            // Zoom out
            case R.id.zoom_out:
                krunner.setZoom(1 / zoomScaler);
                return true;

            // Zoom in
            case R.id.zoom_in:
                krunner.setZoom(zoomScaler);
                return true;

            // Toggle labels
            case R.id.toggle_labels:
                krunner.showLabels = !krunner.showLabels;
                return true;

            // Settings page
            case R.id.action_settings:
                // Actions for settings page
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
