package com.kripton.gamepad.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IRangePolicy;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.util.Range;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
				if(tracesStates.get(ID))
				{
					traces.get(ID).addPoint(x++, y);
					
					if(x % CHART_BUFFER_SIZE == 0)
					{
						int low_border = (int)x;
						int high_border = low_border + CHART_BUFFER_SIZE;
						IRangePolicy rangePolicyX = new RangePolicyFixedViewport(new Range(low_border, high_border));
						chart.getAxisX().setRangePolicy(rangePolicyX);
					}
				}
				else 
				{
					//System.out.println("Trace is disabled");
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
		
		setSize(1200, 700);
		setLayout(new BorderLayout());
		
		chartPanel = new JPanel();
		chartPanel.setBounds(0, 0, 870, 370);
		chartPanel.setLayout(new BorderLayout());
		
		checkBoxPanel = new JPanel();
		checkBoxPanel.setBounds(870, 0, 1200-870, 700-370);
		checkBoxPanel.setLayout(new GridLayout(25, 1));
		
		add(chartPanel);
		add(checkBoxPanel, BorderLayout.EAST);
		
		setOnCloseEvent();
		x = 0;
		
		chart = new Chart2D();
		chart.setBounds(20, 20, 850, 350);
		
		IRangePolicy rangePolicyX = new RangePolicyFixedViewport(new Range(0, CHART_BUFFER_SIZE));
		//IRangePolicy rangePolicyY = new RangePolicyFixedViewport(new Range(-180, 180));
		
		chart.getAxisX().setRangePolicy(rangePolicyX);
		//chart.getAxisY().setRangePolicy(rangePolicyY);
		
		chartPanel.add(chart);
		
		traces = new ArrayList<ITrace2D>();
		tracesStates = new ArrayList<Boolean>();
		
		colors = new Stack<Color>();
		colors.push(Color.BLACK);
		colors.push(Color.BLUE);
		colors.push(Color.RED);
		colors.push(Color.GREEN);
		colors.push(Color.YELLOW);
		colors.push(Color.PINK);
		colors.push(Color.CYAN);
		colors.push(Color.MAGENTA);
	}
	
	
	/*
	 *  Add new 2DLtd trace
	 * traces list contains exactly traces
	 * traces states contains states of all traces
	 * if state is true then trace is drawing 
	 */
	public int addTrace(String name) {
		synchronized(this)
		{
			Trace2DLtd trace = new Trace2DLtd(CHART_BUFFER_SIZE);	
			traces.add(trace);
			tracesStates.add(false);
			
			final int id = traces.size()-1;
			trace.setName(name);
			
			JCheckBox chBox = new JCheckBox();	
			chBox.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					
					switchTraceState(id);
				}
				
			});
			
			lastCheckBoxY += 30;
			chBox.setText(name);
			chBox.setBounds(chBoxX,lastCheckBoxY , 50, 20);
			
			checkBoxPanel.add(chBox);
		
			return id;
		}
	}
	
	
	private void switchTraceState(int ID) {
		
		if(tracesStates.get(ID))
		{
			disableTrace(ID);
			tracesStates.set(ID, false);
		}
		else 
		{
			enableTrace(ID);
			tracesStates.set(ID, true);
		}
		
	}
	
	
	private void disableTrace(int ID) {
		chart.removeTrace(traces.get(ID));
		colors.push(traces.get(ID).getColor());
	}
	
	private void enableTrace(int ID) {
		traces.get(ID).setColor(colors.pop());
		chart.addTrace(traces.get(ID));
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
	private List<Boolean> tracesStates;
	private long x;
	
	private final int chBoxX = 900;
	private int lastCheckBoxY = 50;
	
	public volatile boolean opened = false;
	
	final int CHART_BUFFER_SIZE = 3000;
	JPanel chartPanel;
	JPanel checkBoxPanel;
	
	Stack<Color> colors;
}
