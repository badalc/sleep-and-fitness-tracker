package com.technofreax;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.UUID;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Alarm extends Activity {
	private static final String TAG = "BlueTest5-MainActivity";
	private int mMaxChars = 50000;// Default
	private UUID mDeviceUUID;
	private BluetoothSocket mBTSocket;
	private ReadInput mReadThread = null;
	public static long time=0;
	long millis;
	long alarmMillis;
	public static String deepsleep = " ";
	public static int lightsleep = 0;
	public static int awake = 0;

	private static String steps;
	String st = "2";
	String sp = "5";
	private static final String threshold = "100";
	private boolean mIsUserInitiatedDisconnect = false;
	
	File file = new File(Environment.getExternalStorageDirectory()
			.getAbsolutePath() + File.separator + "values.txt");

	// All controls here
	private TextView mTxtReceive;
	private boolean mIsBluetoothConnected = false;

	private BluetoothDevice mDevice;

	private ProgressDialog progressDialog;
	private boolean flag = false;
	private PowerManager.WakeLock mWakeLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm);
		ActivityHelper.initialize(this);

		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		mDevice = b.getParcelable(Steps.DEVICE_EXTRA);
		mDeviceUUID = UUID.fromString(b.getString(Steps.DEVICE_UUID));
		mMaxChars = b.getInt(Steps.BUFFER_SIZE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Log.d(TAG, "Ready");
		
		 PowerManager pm = (PowerManager)
		 getSystemService(Context.POWER_SERVICE); mWakeLock =
		 pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag"); mWakeLock.acquire();
		 
		 try {
				BufferedWriter buf = new BufferedWriter(
						new FileWriter(file));
				buf.write(" ");
				buf.close();
			} catch (Exception e) {
				Log.e("ERRR", "Could not create file", e);
			}

		mTxtReceive = (TextView) findViewById(R.id.txtRecieved);
		final TimePicker tpTime = (TimePicker) findViewById(R.id.tp_time);
		tpTime.setIs24HourView(true);
		Button setAlarm = (Button) findViewById(R.id.btn_settime);
		setAlarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				/**
				 * Getting a reference to TimePicker object available in the
				 * MainActivity
				 */
				int hour = tpTime.getCurrentHour();
				int minute = tpTime.getCurrentMinute();
				// 
				Calendar cal = Calendar.getInstance();
				time= cal.getTimeInMillis();
				cal.add(Calendar.HOUR, hour);
				cal.add(Calendar.MINUTE, minute);
				millis = cal.getTimeInMillis();
				if ((millis - 1800000) > Calendar.getInstance()
						.getTimeInMillis()) {
					alarmMillis = millis - 1800000;
				} else {
					alarmMillis = Calendar.getInstance().getTimeInMillis() + 5000;
				}
				Toast.makeText(getApplicationContext(), "Alarm set",
						Toast.LENGTH_SHORT).show();
				flag = true;
			}
		});
		Button cancel = (Button) findViewById(R.id.btn_cancelalarm);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
				flag = false;
			}
		});

	}

	private class ReadInput implements Runnable {

		private boolean bStop = false;
		private Thread t;
		

		public ReadInput() {
			t = new Thread(this, "Input Thread");
			t.start();
		}

		public boolean isRunning() {
			return t.isAlive();
		}

		@Override
		public void run() {
			InputStream inputStream;
			// 
			try {
				inputStream = mBTSocket.getInputStream();
				while (!bStop) {
					byte[] buffer = new byte[256];
					if (inputStream.available() > 0) {
						inputStream.read(buffer);
						int i = 0;
						/*
						 * 
						 */
						for (i = 0; i < buffer.length && buffer[i] != 0; i++) {
						}
						final String strInput = new String(buffer, 0, i);

						/*
						 
						 */

						mTxtReceive.post(new Runnable() {
							@Override
							public void run() {
								steps = strInput;
								mTxtReceive.setText(steps);
								// 
								try {
									BufferedWriter buf = new BufferedWriter(
											new FileWriter(file, true));
									buf.append(steps);
									buf.close();
								} catch (Exception e) {
									Log.e("ERRR", "Could not create file", e);
								}

							}
						});
						if (flag
								&& (Calendar.getInstance().getTimeInMillis() > alarmMillis)) {
							if (toInteger(steps) > toInteger(threshold)) {
								mBTSocket.getOutputStream()
										.write(sp.getBytes());
								InputStream inStream = mBTSocket.getInputStream();
								byte[] buff = new byte[256];
								int j = 0;
								while (j < 2) {
									if (inStream.available() > 0) {
										inStream.read(buff);
										int in = 0;
										/*
										 *
										 */
										for (in = 0; in < buff.length
												&& buff[in] != 0; in++) {
										}
										final String Input = new String(buffer,
												0, in);
										if (j == 0) {
											deepsleep = Input;
										} else if (j == 1) {
											lightsleep = toInteger(Input);
										}
									}
									j++;
								}
								Intent intent = new Intent(Alarm.this,
										AfterSleepActivity.class);
								startActivity(intent);
							} else if (Calendar.getInstance().getTimeInMillis() > millis) {
								mBTSocket.getOutputStream()
										.write(sp.getBytes());
								byte[] buff = new byte[256];
								int j = 0;
								while (j < 2) {
									if (inputStream.available() > 0) {
										inputStream.read(buff);
										int in = 0;
										/*
										 
										 */
										for (in = 0; in < buff.length
												&& buff[in] != 0; in++) {
										}
										final String Input = new String(buffer,
												0, in);
										if (j == 0) {
											deepsleep = Input;
										} else if (j == 1) {
											lightsleep = toInteger(Input);
										}
									}
									j++;
								}
								Intent intent = new Intent(Alarm.this,
										AfterSleepActivity.class);
								startActivity(intent);
							}

						}
					}
					Thread.sleep(400);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public void stop() {
			bStop = true;
		}

	}

	private class DisConnectBT extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Void... params) {

			if (mReadThread != null) {
				mReadThread.stop();
				while (mReadThread.isRunning())
					; // Wait until it stops
				mReadThread = null;

			}

			try {
				mBTSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mIsBluetoothConnected = false;
			if (mIsUserInitiatedDisconnect) {
				finish();
			}
		}

	}

	private void msg(String s) {
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPause() {
		if (mBTSocket != null && mIsBluetoothConnected) {
			new DisConnectBT().execute();
		}
		Log.d(TAG, "Paused");
		// 
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mBTSocket == null || !mIsBluetoothConnected) {
			new ConnectBT().execute();
		}
		Log.d(TAG, "Resumed");
		super.onResume();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "Stopped");
		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	private class ConnectBT extends AsyncTask<Void, Void, Void> {
		private boolean mConnectSuccessful = true;

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(Alarm.this, "Hold on",
					"Connecting");// 
		}

		@Override
		protected Void doInBackground(Void... devices) {

			try {
				if (mBTSocket == null || !mIsBluetoothConnected) {
					mBTSocket = mDevice
							.createInsecureRfcommSocketToServiceRecord(mDeviceUUID);
					BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
					mBTSocket.connect();
				}
			} catch (IOException e) {
				// Unable to connect to device
				e.printStackTrace();
				mConnectSuccessful = false;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (!mConnectSuccessful) {
				Toast.makeText(
						getApplicationContext(),
						"Could not connect to device. Is it a Serial device? Also check if the UUID is correct in the settings",
						Toast.LENGTH_LONG).show();
				finish();
			} else {
				msg("Connected to device");
				mIsBluetoothConnected = true;
				try {
					mBTSocket.getOutputStream().write(st.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mReadThread = new ReadInput(); // Kick off input reader
			}

			progressDialog.dismiss();
		}

	}

	public int toInteger(String s) {
		int j = 0;

		for (int k = 0; k < s.length(); k++) {
			if (s.charAt(k) > 47 && s.charAt(k) < 58) {
				j = j * 10 + ((int) s.charAt(k) - 48);
			}
		}
		return j;

	}
}
