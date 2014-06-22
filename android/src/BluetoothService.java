package com.technofreax;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass.Device;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class BluetoothService extends Service {
	private static final String TAG = "BluetoothService";
	private BluetoothAdapter mBluetoothAdapter;
	public static final String BT_DEVICE = "RNBT-3A31";
	public static final String Address = "00:06:66:3A:31";
	String deviceName = "RNBT-3A31";
	private UUID mDeviceUUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private ReadInput mreadthread = null;
	String steps;
	public static final int STATE_NONE = 0; // 
	public static final int STATE_LISTEN = 1; // 
	public static final int STATE_CONNECTING = 2; // 
	public static final int STATE_CONNECTED = 3; // 
	// device

	public static int mState = STATE_NONE;
	BluetoothDevice device = null;
	BluetoothSocket btSocket;
 
	@Override
	public void onCreate() {
		Log.d(TAG, "Service started");
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {

		return mBinder;
	}

	public class LocalBinder extends Binder {
		BluetoothService getService() {
			return BluetoothService.this;
		}
	}

	private final IBinder mBinder = new LocalBinder();

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "Onstart Command");
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (mBluetoothAdapter == null) {
			Toast.makeText(getApplicationContext(), "Bluetooth not found",
					Toast.LENGTH_SHORT).show();
		} else if (!mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
		} else {
			BluetoothDevice btdevice = mBluetoothAdapter
					.getRemoteDevice(Address);
			if (btdevice != null) {
				try {
					btSocket = btdevice
							.createInsecureRfcommSocketToServiceRecord(mDeviceUUID);
					btSocket.connect();
					mreadthread = new ReadInput();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	public int getInteger(){
		int j = 0;

		for (int k = 0; k < steps.length(); k++) {
			if (steps.charAt(k) > 47 && steps.charAt(k) < 58) {
				j = j * 10 + ((int) steps.charAt(k) - 48);
			} 
		}
		return j;
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
				inputStream = btSocket.getInputStream();
				while (!bStop) {
					byte[] buffer = new byte[256];
					if (inputStream.available() > 0) {
						inputStream.read(buffer);
						int i = 0;
						/*
						 
						 */
						for (i = 0; i < buffer.length && buffer[i] != 0; i++) {
						}
						final String strInput = new String(buffer, 0, i);
						steps = strInput;
						try {
							BufferedWriter buf = new BufferedWriter(
									new FileWriter(file, true));
							buf.append(strInput);
							buf.close();
						} catch (Exception e) {
							Log.e("ERRR", "Could not create file",
									e);
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
}
