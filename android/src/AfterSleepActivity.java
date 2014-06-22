package com.technofreax;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class AfterSleepActivity extends Activity {

	MediaPlayer mp = null;
	Button showGraph;
	int i = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.afteralarm);
		Button stopAlarm = (Button) findViewById(R.id.stopAlarm);
		stopAlarm.getBackground().setColorFilter(Color.RED,
				PorterDuff.Mode.MULTIPLY);
		showGraph = (Button) findViewById(R.id.showGraph);
		mp = MediaPlayer.create(getBaseContext(), R.raw.song);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		stopAlarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
					mp.stop();
					

			}
		});

		playSound(this, getAlarmUri());

		showGraph.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent start = new Intent(AfterSleepActivity.this,
						Analysis.class);
				startActivity(start);
			}
		});
	}

	private void playSound(final Context context, Uri alert) {
		
		Thread background = new Thread(new Runnable() {
			public void run() {
				try {
					mp.setLooping(true);
					mp.start();

				} catch (Throwable t) {
					Log.i("Animation", "Thread  exception " + t);
				}
			}
		});
		background.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mp.stop();
	} // Get an alarm sound. Try for an alarm. If none set, try notification,

	// Otherwise, ringtone.
	private Uri getAlarmUri() {

		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		if (alert == null) {
			alert = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			if (alert == null) {
				alert = RingtoneManager
						.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			}
		}
		return alert;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.finish();
	}
	

}
