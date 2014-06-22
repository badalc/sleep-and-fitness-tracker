package com.technofreax;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.achartengine.GraphicalView;
import org.achartengine.model.XYSeries;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Analysis extends Activity{
	ArrayList<Integer> list = new ArrayList<Integer>();
	private LineGraphTime line = new LineGraphTime();
	private static GraphicalView mchartview;
	private int deepsleep=0;
	private int lightsleep=0;
	private int awake=0,awake1=0,flag=0;
	private int value;
	private TextView txtDsleep;
	private TextView txtSsleep;
	private TextView txtAwake;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.analysis);
		mchartview = line.getView(this);
		txtDsleep = (TextView)findViewById(R.id.deepSleepValue);
		txtSsleep = (TextView)findViewById(R.id.lightSleepValue);
		txtAwake = (TextView)findViewById(R.id.noSleepValue);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"values.txt");
		FileInputStream fIn;
		try {
			fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String values = " ";
			while((values = myReader.readLine())!=null){
				value = toInteger(values);
				list.add(value);
				if(value<85){
					flag=0;
					deepsleep++;
				} else if(value<1000){
					lightsleep++;
					flag=0;
				}else{
					flag=1;
					if(flag==1)
					awake++;
					else
						awake=0;
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long x = Alarm.time;
		for(int i : list){
			line.addNewPoints(x, i);
			x+=10000;
		}
		LinearLayout layout = (LinearLayout) findViewById(R.id.chartview);
		layout.addView(mchartview, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mchartview.repaint();
		int total = deepsleep+lightsleep+awake;
		deepsleep=(int)((deepsleep/total)*100);
		lightsleep=(int)((lightsleep/total)*100);
		if(awake>20){
			awake1+=20;
			awake=0;
			
		}
		awake1=(int)((awake1/total)*100);
		txtDsleep.setText(Integer.toString(deepsleep));
		txtSsleep.setText(Integer.toString(lightsleep));
		txtAwake.setText(Integer.toString(awake1));
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
