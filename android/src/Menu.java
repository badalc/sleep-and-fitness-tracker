package com.technofreax;

//import com.technofreax.Jogging.MyAdapter;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ViewFlipper;

public class Menu extends Activity {

	//String classes[] = { "Jogging","Walking", "Skipping", "Sleep", "Alarm", "Pushup", "example6" };
	Button mBtnSleep,mBtnFitness;
	 private ViewFlipper viewFlipper;
     private float lastX;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newmenu);
		findViewById(R.id.newMenuBtn2).clearAnimation();
		mBtnSleep = (Button) findViewById(R.id.newMenuBtn1);
		mBtnFitness = (Button) findViewById(R.id.newMenuBtn2);
		viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
		final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
		final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
		final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		mBtnSleep.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				arg0.startAnimation(animRotate);		
				Intent intent = new Intent(getApplicationContext(),
						Sleep.class);
				
				startActivity(intent);
			}
		});
		mBtnFitness.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				findViewById(R.id.newMenuBtn2).clearAnimation();
				 TranslateAnimation translation;
				 translation = new TranslateAnimation(0f, 0F, 0f, getDisplayHeight()+93);
				 translation.setStartOffset(500);
				 translation.setDuration(1700);
				 translation.setFillAfter(true);
		         translation.setInterpolator(new BounceInterpolator());
		         findViewById(R.id.newMenuBtn2).startAnimation(translation);
		         	
				
		         final Timer myt = new Timer();
		        
		         myt.schedule(new TimerTask() {

		         @Override
		         public void run() {
		         // TODO Auto-generated method stub
		        	 
		         try {
		         Intent intent= new Intent(Menu.this, FitnessMenu.class);
		         
		         startActivity(intent);  
		         } catch (Exception e) {
		         // TODO: handle exception
		         }

		         myt.cancel();
		         }
		         }, 750);
			}
			
		});
		
	}
	private int getDisplayHeight() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}
	 public boolean onTouchEvent(MotionEvent touchevent)
     {	findViewById(R.id.newMenuBtn2).clearAnimation();
                  switch (touchevent.getAction())
                  {
                         // when user first touches the screen to swap
                          case MotionEvent.ACTION_DOWN:
                          {
                              lastX = touchevent.getX();
                              break;
                         }
                          case MotionEvent.ACTION_UP:
                          {
                              float currentX = touchevent.getX();
                             
                              // if left to right swipe on screen
                              if (lastX < currentX)
                              {
                                   // If no more View/Child to flip
                                  if (viewFlipper.getDisplayedChild() == 0)
                                      break;
                                 
                                  // set the required Animation type to ViewFlipper
                                  // The Next screen will come in form Left and current Screen will go OUT from Right
                                  viewFlipper.setInAnimation(this, R.anim.in_from_left);
                                  viewFlipper.setOutAnimation(this, R.anim.out_to_right);
                                  // Show the next Screen
                                  viewFlipper.showNext();
                              }
                             
                              // if right to left swipe on screen
                              if (lastX > currentX)
                              {
                                  if (viewFlipper.getDisplayedChild() == 1)
                                      break;
                                  // set the required Animation type to ViewFlipper
                                  // The Next screen will come in form Right and current Screen will go OUT from Left
                                  viewFlipper.setInAnimation(this, R.anim.in_from_right);
                                  viewFlipper.setOutAnimation(this, R.anim.out_to_left);
                                  // Show The Previous Screen
                                  viewFlipper.showPrevious();
                              }
                              break;
                          }
                  }
                  return false;
     }

}
	
	
		
	

