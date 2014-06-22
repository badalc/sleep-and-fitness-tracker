package com.technofreax;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SleepActivity extends Activity{
private Button startService;
private Button stopService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sleep);
		Toast.makeText(getApplicationContext(), "oh yeah", Toast.LENGTH_SHORT).show();
		
		startService=(Button) findViewById(R.id.btnStart);
		stopService=(Button)findViewById(R.id.btnStop);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		startService.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			   Intent i = new Intent(SleepActivity.this,BluetoothService.class);
			   startService(i);
			}
		});
		stopService.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(SleepActivity.this,BluetoothService.class);
			   stopService(i);
			}
		});
	}
}
