package com.technofreax;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;

public class LineGraph {
	private TimeSeries dataset = new TimeSeries("TechnoFreax");
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	private XYSeriesRenderer renderer = new XYSeriesRenderer();
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

	public LineGraph() {
		mDataset.addSeries(dataset);
		
		renderer.setColor(Color.GREEN);
		renderer.setPointStyle(PointStyle.POINT);
		renderer.setFillPoints(true);
		
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setXTitle("Counter");
		mRenderer.setYTitle("Steps");
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.BLACK);
		mRenderer.addSeriesRenderer(renderer);
		mRenderer.setShowGrid(true);
	}
	public GraphicalView getView(Context context){
		return ChartFactory.getLineChartView(context, mDataset, mRenderer);
	}
	
	public void addNewPoints(int x,int y){
		dataset.add(x, y);
	}
	
}
