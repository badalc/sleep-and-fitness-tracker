package com.technofreax;

import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;

public class LineGraphTime {
	private TimeSeries dataset = new TimeSeries("TechnoFreax");
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	private XYSeriesRenderer renderer = new XYSeriesRenderer();
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

	public LineGraphTime() {
		mDataset.addSeries(dataset);
		
		renderer.setColor(Color.GREEN);
		renderer.setPointStyle(PointStyle.POINT);
		renderer.setFillPoints(true);
		
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setXTitle("Time");
		mRenderer.setYTitle("Activity");
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.BLACK);
		mRenderer.addSeriesRenderer(renderer);
		mRenderer.setShowGrid(true);
	}
	public GraphicalView getView(Context context){
		return ChartFactory.getTimeChartView(context, mDataset, mRenderer,"H:mm");
	}
	
	public void addNewPoints(long x,int y){
		dataset.add(new Date(x), y);
	}
	
}
