/*
 * Nick Rodriguez
 * April 30, 2013
 * Starfield activity
 * Emulates a field of stars in space
 * Ability to alter speed and star count
 */

package edu.unca.nrodrigu.starfield;

import edu.unca.nrodrigu.starfield.StarfieldRenderer;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
//import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class StarfieldActivity extends Activity {
	/** Hold a reference to our GLSurfaceView */
	private GLSurfaceView mGLSurfaceView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mGLSurfaceView = new GLSurfaceView(this);

		// Check if the system supports OpenGL ES 2.0.
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = true; //configurationInfo.reqGlEsVersion >= 0x20000;
		
		if (supportsEs2) {
			// Request an OpenGL ES 2.0 compatible context.
			mGLSurfaceView.setEGLContextClientVersion(2);

			// Set the renderer to our demo renderer, defined below.
			mGLSurfaceView.setRenderer(new StarfieldRenderer());
		} 
		else {
			// This is where you could create an OpenGL ES 1.x compatible
			// renderer if you wanted to support both ES 1 and ES 2.
			return;
		}
		
		// remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        // make fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(mGLSurfaceView);
		
		// layout that holds UI
        RelativeLayout layout = new RelativeLayout(this);
        layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        // label for stars
        TextView starsLabel = new TextView(this);
        starsLabel.setText("Stars");
        starsLabel.setShadowLayer(0.5f, 0.5f, 0.5f, Color.GRAY);
        starsLabel.setTextColor(Color.WHITE);
        starsLabel.setPadding(10, 10, 10, 10);
        starsLabel.setId(1); // give it an id to reference later
        layout.addView(starsLabel);
        
        // create a new layout for the stars seekbar
        RelativeLayout.LayoutParams starsSeekBarLayout = new RelativeLayout.LayoutParams(
        		LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        starsSeekBarLayout.width = 401;
        starsSeekBarLayout.addRule(RelativeLayout.RIGHT_OF, starsLabel.getId());
        
        // seekbar for number of stars to generate
        SeekBar starsSeekBar = new SeekBar(this);
        starsSeekBar.setMax(200); // max stars
        starsSeekBar.setProgress(50); // current stars
        layout.addView(starsSeekBar, starsSeekBarLayout);
        starsSeekBar.setOnSeekBarChangeListener(
        		new OnSeekBarChangeListener() {
        	   @Override
        	   public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        		if (progress == 0)
        			progress++;
        	    StarfieldRenderer.starCount = progress;
        	   }
        	   @Override
        	   public void onStartTrackingTouch(SeekBar seekBar) {}
        	   @Override
        	   public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        
        // create new layout for speed text
        RelativeLayout.LayoutParams starSpeedLayout = new RelativeLayout.LayoutParams(
        		LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        starSpeedLayout.addRule(RelativeLayout.BELOW, starsLabel.getId());
        
        // star speed label
        TextView speedLabel = new TextView(this);
        speedLabel.setText("Speed");
        speedLabel.setShadowLayer(0.5f, 0.5f, 0.5f, Color.GRAY);
        speedLabel.setTextColor(Color.WHITE);
        speedLabel.setPadding(10, 10, 5, 10);
        speedLabel.setId(2); // set id to be referenced later
        layout.addView(speedLabel, starSpeedLayout);
        
        // create new layout for speed seek bar
        RelativeLayout.LayoutParams speedSeekBarLayout = new RelativeLayout.LayoutParams(
        		LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        speedSeekBarLayout.width = 400; // this makes it even with the bar above it
        speedSeekBarLayout.addRule(RelativeLayout.RIGHT_OF, speedLabel.getId());
        speedSeekBarLayout.addRule(RelativeLayout.BELOW, starsLabel.getId());
        
        // seek bar for speed
        SeekBar speedSeekBar = new SeekBar(this);
        speedSeekBar.setProgress(11);
        speedSeekBar.setMax(20);
        layout.addView(speedSeekBar, speedSeekBarLayout);
        speedSeekBar.setOnSeekBarChangeListener(
        		new OnSeekBarChangeListener() {
        	   @Override
        	   public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        		if (progress == 0)
        			progress++;
        	    Star.speed = progress;
        	   }
        	   @Override
        	   public void onStartTrackingTouch(SeekBar seekBar) {}
        	   @Override
        	   public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        
        this.addContentView(layout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}

	@Override
	protected void onResume() {
		// The activity must call the GL surface view's onResume() on activity onResume().
		super.onResume();
		mGLSurfaceView.onResume();
	}

	@Override
	protected void onPause() {
		// The activity must call the GL surface view's onPause() on activity onPause().
		super.onPause();
		mGLSurfaceView.onPause();
	}	
}

/*
class MyGLSurfaceView extends GLSurfaceView {

    private final StarfieldRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new StarfieldRenderer();
        setRenderer(mRenderer);
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                  dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                  dy = dy * -1 ;
                }

                mRenderer.mAngle += (dx + dy) * TOUCH_SCALE_FACTOR;  // = 180.0f / 320
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}
*/