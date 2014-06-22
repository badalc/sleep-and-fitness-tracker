/* */

package com.technofreax;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.UUID;

import org.achartengine.GraphicalView;

import com.technofreax.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivitypushup extends Activity {

	private String VALUES = "/sdcard/values.txt";
	private static final String TAG = "BlueTest5-MainActivitypushup";
	private int mMaxChars = 50000;// Default
	private UUID mDeviceUUID;
	private BluetoothSocket mBTSocket;
	private ReadInput mReadThread = null;
	int myHeight = 170;
	int myWeight = 70;
	int stepsInt = 0;
	int eqStepsInt = 0;
	double calories = 0;
	String msex = "Male";
	String caloriesString;
	private static GraphicalView mchartview;
	private LineGraph line = new LineGraph();
	private static Thread thread;

	private static String steps;
	private static int x = 0;
	private static int y = 0;

	private boolean mIsUserInitiatedDisconnect = false;

	// All controls here
	// private TextView mTxtReceive;
	// private Button mBtnDisconnect;
	// private Button mBtnGraph;
	private Button mBtnStart;
	private Button mBtnStop;
	// private ScrollView scrollView;
	private TextView etPushup, etCalorie;

	// private CheckBox chkScroll;
	// private CheckBox chkReceiveText;
	private String toArduino;// values which will go to arduino
	private int fromArduino = 0;

	private boolean mIsBluetoothConnected = false;

	private BluetoothDevice mDevice;

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainpushup);
		ActivityHelper.initialize(this);

		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		mDevice = b.getParcelable(Steps.DEVICE_EXTRA);
		mDeviceUUID = UUID.fromString(b.getString(Steps.DEVICE_UUID));
		mMaxChars = b.getInt(Steps.BUFFER_SIZE);
		myHeight = b.getInt("height");
		myWeight = b.getInt("weight");
		msex = b.getString("sex");
		Log.d(TAG, "Ready");
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// mBtnDisconnect = (Button) findViewById(R.id.btnDisconnect);
		// mBtnGraph = (Button) findViewById(R.id.btnGraph);
		mBtnStart = (Button) findViewById(R.id.btnStart);
		mBtnStop = (Button) findViewById(R.id.btnStop);
		etCalorie = (TextView) findViewById(R.id.editCalorie);
		etPushup = (TextView) findViewById(R.id.editPushup);
		
		final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
		final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
		final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
		// mTxtReceive = (TextView) findViewById(R.id.txtReceive);
		// scrollView = (ScrollView) findViewById(R.id.viewScroll);
		// chkScroll = (CheckBox) findViewById(R.id.chkScroll);
		// chkReceiveText = (CheckBox) findViewById(R.id.chkReceiveText);
		// mBtnClearInput = (Button) findViewById(R.id.btnClearInput);
		mchartview = line.getView(this);

		// mTxtReceive.setMovementMethod(new ScrollingMovementMethod());

		// mBtnDisconnect.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// mIsUserInitiatedDisconnect = true;
		// new DisConnectBT().execute();
		// }
		// });

		mBtnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				arg0.startAnimation(animAlpha);
				toArduino = "1";
				fromArduino = 1;
				try {
					mBTSocket.getOutputStream().write(toArduino.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		mBtnStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				arg0.startAnimation(animAlpha);
				toArduino = "4";
				fromArduino = 2;
				try {
					mBTSocket.getOutputStream().write(toArduino.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		// mBtnGraph.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
		// layout.addView(mchartview, new LayoutParams(
		// LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		//
		// thread = new Thread() {
		// public void run() {
		//
		// for (int i = 0;; i++) {
		// try {
		// Thread.sleep(400);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// line.addNewPoints(x, y);
		// mchartview.repaint();
		// int k = toInteger(steps, y);
		// x = x + 5;
		// y = k;
		// }
		// }
		// };
		// thread.start();
		// }
		// });

		// mBtnClearInput.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// mTxtReceive.setText("");
		// }
		// });

	}

	private class ReadInput implements Runnable {

		private boolean bStop = false;
		private Thread t;
		File file = new File("/storage/sdcard0/stats.txt");

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

			try {
				inputStream = mBTSocket.getInputStream();
				while (!bStop) {
					byte[] buffer = new byte[256];
					if (inputStream.available() > 0) {
						inputStream.read(buffer);
						int i = 0;
						for (i = 0; i < buffer.length && buffer[i] != 0; i++) {
						}
						final String strInput = new String(buffer, 0, i);
						if (true) {
							etPushup.post(new Runnable() {
								@Override
								public void run() {
									steps = strInput;
									stepsInt=toInteger(steps,stepsInt);
									eqStepsInt=(int)(stepsInt*3636.0/1500);
									etPushup.setText(Integer.toString(stepsInt));
									calories=calorieconvert(eqStepsInt);
									etCalorie.setText(Double.toString(calories));
									try {
										BufferedWriter buf = new BufferedWriter(
												new FileWriter(file, true));
										buf.append(steps);
										buf.close();
									} catch (Exception e) {
										Log.e("ERRR", "Could not create file",
												e);
									}
									// chkReceiveText.isChecked()Uncomment below
									// for testing
									// mTxtReceive.append("\n");
									// mTxtReceive.append("Chars: " +
									// strInput.length() + " Lines: " +
									// mTxtReceive.getLineCount() + "\n");

									/*
									 * int txtLength =
									 * mTxtReceive.getEditableText().length();
									 * if(txtLength > mMaxChars){
									 * mTxtReceive.getEditableText().delete(0,
									 * txtLength - mMaxChars); }
									 */

									// if (false) { // Scroll
									// onlychkScroll.isChecked()
									// // if this
									// // is
									// // checked
									// scrollView.post(new Runnable() { //
									// 
									// @Override
									// public void run() {
									// scrollView
									// .fullScroll(View.FOCUS_DOWN);
									// }
									// });
									// }
								}

								private double calorieconvert(int eqStepsInt) {
									// TODO Auto-generated method stub
									if(myWeight<54 && myWeight >=45)
										return eqStepsInt*420.0/10000;
									else if(myWeight>=54 && myWeight <63)
										return eqStepsInt*440.0/10000;
									else if(myWeight<72 && myWeight >=63)
										return eqStepsInt*460.0/10000;
									else if(myWeight<81 && myWeight >=72)
										return eqStepsInt*480.0/10000;
									else if(myWeight<90 && myWeight >=81)
										return eqStepsInt*500.0/10000;
									else if(myWeight<99 && myWeight >=90)
										return eqStepsInt*520.0/10000;
									else if(myWeight<108 && myWeight >=99)
										return eqStepsInt*540.0/10000;
									else if(myWeight<117 && myWeight >=108)
										return eqStepsInt*560.0/10000;
									else if(myWeight<126 && myWeight >=117)
										return eqStepsInt*580.0/10000;
									else if(myWeight<135 && myWeight >=126)
										return eqStepsInt*600.0/10000;
									else
										return eqStepsInt*620.0/10000;
								}

							});
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
			progressDialog = ProgressDialog.show(MainActivitypushup.this,
					"Hold on", "Connecting");// 
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
				mReadThread = new ReadInput(); // Kick off input reader
			}

			progressDialog.dismiss();
		}

	}
	
	

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		try {
			mBTSocket.getOutputStream().write("4".getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int toInteger(String s, int y) {
		int j = 0, flag = 1;

		for (int k = 0; k < s.length(); k++) {
			if (s.charAt(k) > 47 && s.charAt(k) < 58) {
				j = j * 10 + ((int) s.charAt(k) - 48);
			} else {
				if (j != 0) {
					return j;
				}
			}
		}

		return y;

	}

	

}
