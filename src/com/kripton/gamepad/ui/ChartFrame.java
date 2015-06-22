package com.kripton.gamepad.ui;

import java.util.ArrayList;
import java.util.List;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IRangePolicy;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.util.Range;

import javax.swing.JFrame;

public class ChartFrame extends JFrame {
	
	
	/**
	 *  ChartFrame is extended JFrame with default Chart2D in it
	 *  It contains Trace2DLtd which has maximum size and replace oldest point with newest if buffer is full.
	 *  
	 */
	private static final long serialVersionUID = 1L;

	public ChartFrame() {
		super();
		init();
	}
	
	
	public ChartFrame(String title) {
		super(title);
		init();
	}
	
	
	/* Add point to a trace with given ID */
	public void addPoint(double y, int ID) {
		synchronized(this)
		{	
			if(ID < traces.size()) 
			{
				traces.get(ID).addPoint(x++, y);
				
				if(x == 3000)
				{
					x = 0;
				}
			}
			else
			{
				System.out.println("There is no trace with given ID");
			}
		}
	}
	
	
	/*public void show() {
		
		opened = true;
		setVisible(true);
		
	}*/
	
	
	private void init() {
		
		this.setSize(900, 700);
		setOnCloseEvent();
		x = 0;
		
		chart = new Chart2D();
		chart.setBounds(20, 20, 800, 600);
		
		IRangePolicy rangePolicyX = new RangePolicyFixedViewport(new Range(0, 3000));
		IRangePolicy rangePolicyY = new RangePolicyFixedViewport(new Range(-360, 360));
		
		chart.getAxisX().setRangePolicy(rangePolicyX);
		chart.getAxisY().setRangePolicy(rangePolicyY);
		
		getContentPane().add(chart);
		
		traces = new ArrayList<ITrace2D>();
	}
	
	
	/* Add new 2DLtd trace */
	public int addTrace() {
		synchronized(this)
		{
			Trace2DLtd trace = new Trace2DLtd(CHART_BUFFER_SIZE);	
			traces.add(trace);
			chart.addTrace(trace);
		
			return traces.size()-1;
		}
	}
	
	
	private void setOnCloseEvent() {
		
		addWindowListener(new java.awt.event.WindowAdapter() {
			
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				
				opened = false;
				
			}			
		});
		
	}
	
	
	private Chart2D chart;
	private List<ITrace2D> traces;
	private long x;
	
	public volatile boolean opened = false;
	
	final int CHART_BUFFER_SIZE = 3000;
}
