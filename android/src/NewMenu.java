/*

 */

package com.technofreax;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.technofreax.R;



//import com.blueserial.R;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class NewMenu extends Activity {

	private Button mBtnSleep;
	private Button mBtnFitness;
	private ListView mLstDevices;
	private ViewFlipper viewFlipper;
    private float lastX;
	private BluetoothAdapter mBTAdapter;
	int myHeight=170;
	int myWeight=70;
	String msex ="Male"; 
	private static final int BT_ENABLE_REQUEST = 10; // This is the code we use
														// for BT Enable
	private static final int SETTINGS = 20;

	private UUID mDeviceUUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB"); // 
	private int mBufferSize = 50000; // Default
	public static final String DEVICE_EXTRA = "com.technofreax.SOCKET";
	public static final String DEVICE_UUID = "com.technofreax.uuid";
	private static final String DEVICE_LIST = "com.technofreax.devicelist";
	private static final String DEVICE_LIST_SELECTED = "com.technofreax.devicelistselected";
	public static final String BUFFER_SIZE = "com.texhnofreax.buffersize";
	private static final String TAG = "BlueTest5-Homescreen";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.newmenu);
		ActivityHelper.initialize(this); // This is to ensure that the rotation
											// persists across activities and
											// not just this one
		Log.d(TAG, "Created");

		mBtnSleep = (Button) findViewById(R.id.newMenuBtn1);
		mBtnFitness = (Button) findViewById(R.id.newMenuBtn2);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
				/*
		
		 */
		

		
		mBtnFitness.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				Intent intent = new Intent(getApplicationContext(),
						FitnessMenu.class);
				
				startActivity(intent);
			}
		});
		mBtnSleep.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				Intent intent = new Intent(getApplicationContext(),
						Sleep.class);
			
			startActivity(intent);
			}
		});
	}
	public boolean onTouchEvent(MotionEvent touchevent)
    {
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

	/**
	 * Called when the screen rotates. If this isn't handled, data already
	 * generated is no longer available
	 */
	
	/**
	 * Quick way to call the Toast
	 * 
	 * @param str
	 */
	
	/**
	 * Initialize the List adapter
	 * 
	 * @param objects
	 */


	/**
	 * Searches for paired devices. Doesn't do a scan! Only devices which are
	 * paired through Settings->Bluetooth will show up with this. I didn't see
	 * any need to re-build the wheel over here
	 * 
	 * @author ryder
	 * 
	 */
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.homescreen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(NewMenu.this, PreferencesActivity.class);
			startActivityForResult(intent, SETTINGS);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
