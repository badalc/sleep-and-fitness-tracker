package com.technofreax;

//

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class FitnessMenu extends Activity {

	//String classes[] = { "Jogging","Walking", "Skipping", "Sleep", "Alarm", "Pushup", "example6" };
	Button mBtnJog,mBtnWalk,mBtnPushup,mBtnSkip;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fitnessmenu);
		final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
		final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
		final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
		mBtnJog = (Button) findViewById(R.id.menubtnjog);
		mBtnWalk = (Button) findViewById(R.id.menubtnwalk);
		mBtnPushup = (Button) findViewById(R.id.menubtnpushup);
		mBtnSkip = (Button) findViewById(R.id.menubtnskip);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mBtnJog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				 arg0.startAnimation(animScale);
						
				Intent intent = new Intent(getApplicationContext(),
						Jogging.class);
				
				startActivity(intent);
			}
		});
		mBtnWalk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				 arg0.startAnimation(animScale);
						
				Intent intent = new Intent(getApplicationContext(),
						Walking.class);
				
				startActivity(intent);
			}
		});
		mBtnPushup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				 arg0.startAnimation(animScale);		
				Intent intent = new Intent(getApplicationContext(),
						Pushup.class);
				
				startActivity(intent);
			}
		});
		mBtnSkip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				 arg0.startAnimation(animScale);
						
				Intent intent = new Intent(getApplicationContext(),
						Skipping.class);
				
				startActivity(intent);
			}
		});
		
	}
	
	
	
	
		
	}

