package com.kripton.gamepad.ui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import com.kripton.COM.COMWorker;
import com.kripton.gamepad.JoystickReciever;
import com.kripton.gamepad.chart.ChartFrame;
import com.kripton.gamepad.chart.ChartParser;

public class Controller implements Runnable {
	
	
	public Controller() {
		
		joystickReceiver = new JoystickReciever();
		initCodes();
		worker = new COMWorker();
		chartParser = new ChartParser();
		tracesNames = new String[0];
		
	}

	
	/* Must be called before starting controller's thread */
	public void setCOMInfoArea(JTextArea _comInfo) {
		comInfo = _comInfo;
	}
	
	
	/* Must be called before starting controller's thread */
	public void setInfoLabels(Map<String, JLabel> lblsWrite, Map<String, JLabel> lblsRead) {
		infoWriteLbls = lblsWrite;
		infoReadLbls = lblsRead;
	}
	
	
	/* Print COM ports to JComboBox for selection */
	public void showCOMPorts(JComboBox<String> ports) {
		worker.showCOMPorts(ports);
	}
	
	
	/* Open serial port for communication */
	public void openCOMPort(String name, int baud) {
		//Runtime.getRuntime().exec("sudo rm /var/lock/LCK*USB0");
		worker.openCOMPort(name, baud);
		this.start();
	}
	
	
	public void startJoystickReceiver() {
		if(joystickReceiver.isGamepadFound())
		{
			joystickReceiver.start();
		}
	}
	
	
	public void start() {
		workingThread = new Thread(this);
		workingThread.start();
	}
	
	
	/* Close serial port */
	public void closePort() {
		worker.closePort();
	}
	
	
	/* 
	 * Create chart frame
	 * Create traces amount of parameter count coming from COM port
	 */
	public void openChartFrame(ChartFrame frame) {
		
		chartFrame = frame;
		
		int paramsCount = chartParser.getParamsCount(worker.getBufferedData());
		tracesID = new int[paramsCount];
		
		String traceName = null;
		for(int i=0; i<paramsCount; i++)
		{
			if(tracesNames.length == paramsCount)
			{
				traceName = tracesNames[i];
			}
			else 
			{
				traceName = String.valueOf(i);
			}
			System.out.println(traceName);
			tracesID[i] = chartFrame.addTrace(traceName);
		}
		
	}
	
	
	
	@Override
	public void run() {
		
		boolean paramsFlagFound = false;
		while(true) 
		{	
			if(joystickReceiver.started)
			{
				getJoystickData();
			}
				
			/* Log read data and put new points in chart */
			if(worker.isRead()) 
			{
				String readData = worker.getReadData();
				comInfo.append("In: "+readData);
				
				if(paramsFlagFound)
				{
					tracesNames = readData.split(",");
					paramsFlagFound = false;
				}
				else if(readData.length() >= 4 && readData.charAt(0) == '0' && readData.charAt(1) == 'x' 
						&& readData.charAt(2) == '0' && readData.charAt(3) == '1')
				{
					comInfo.append("Found params flag"+'\n');
					//comInfo.append(readData);
					paramsFlagFound = true;
				}
				
				/* Should add all parameters */
				if(chartFrame != null && chartFrame.opened) 
				{
					for(int i=0; i<tracesID.length; i++)
					{
						double pointY = chartParser.parse(readData, i);
						chartFrame.addPoint(pointY, tracesID[i]);
					}
				}
				
				/* Find label by it's code */
				String[] codeAndVal = readData.split(":");		
				JLabel infoLbl = infoReadLbls.get(codeAndVal[0]);
			
				if(infoLbl != null) 
				{
					infoLbl.setText(infoLbl.getName()+": "+codeAndVal[1]);
				}
			}
		}
	}
	
		
	/*
	 * Get gamepad data and put it into appropriate label
	 *
	 */
	private void getJoystickData() {
		
		Map<String, Float> polls = joystickReceiver.getPolls();
		Map<String, Float> copyPolls = null;
			
		synchronized(polls) {
			copyPolls = new HashMap<String, Float>(polls);
		}

		int val = 0;
			
		if(!copyPolls.isEmpty()) 
		{
			for(Map.Entry<String, Float> entry : copyPolls.entrySet()) 
			{	
				val = (int)(entry.getValue()*1000);
				int norm_val = (int) map(val, 0, 1000, 700, 2000);
				
				if(entry.getKey().equals("slider")) {
					//worker.write(String.valueOf(norm_val)+'\n');								
					//comInfo.append("OUT: "+String.valueOf(norm_val)+'\n');
				}
				
				/* Get name of component then get its code and write to label */
			    String compName = entry.getKey();
			    String compCode = writeCodes.get(compName);
			    JLabel compLbl = infoWriteLbls.get(compCode);
				if(compLbl != null) 
				{
					compLbl.setText(compName+": "+String.valueOf(entry.getValue()));
				}
			    
			}

			polls.clear();
			copyPolls.clear();
		}
	}
	

	long map(long x, long in_min, long in_max, long out_min, long out_max)
	{
	  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	
	
	/* Call after JoystickReciever initialization because codes are written there */
	private void initCodes() {
	
		try 
		{
			BufferedReader reader = new BufferedReader(new FileReader("codes.txt"));
			String res = null;
			
			while((res = reader.readLine()) != null) {
				String[] codeAndVal = res.split(":");
				writeCodes.put(codeAndVal[0], codeAndVal[1]);
			}
			
			reader.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		readCodes.put("Gyro", "1");		
		readCodes.put("Accel", "2");
		
	}
	
	
	
	private JTextArea comInfo;
	
	private ChartFrame chartFrame;
	private ChartParser chartParser;
	
	private String[] tracesNames;
	private int[] tracesID; 
	
	private JoystickReciever joystickReceiver = null;
	private COMWorker worker = null;
	
	private Thread workingThread = null;
	
	/* First name then code */
	private Map<String, String> writeCodes = new HashMap<String, String>();
	private Map<String, String> readCodes = new HashMap<String, String>();
	
	/* First code then label */
	private Map<String, JLabel> infoWriteLbls = new HashMap<String, JLabel>();
	private Map<String, JLabel> infoReadLbls = new HashMap<String, JLabel>();
	
	private final String PARAMS_FLAG = "0x01"+'\n';
	
	int last_val = 0;
}
