package com.kripton.gamepad.ui;

import java.util.HashMap;
import java.util.Map;

import com.kripton.COM.COMWorker;
import com.kripton.gamepad.JoystickReciever;

public class Controller implements Runnable {
	
	
	public Controller() {
		
		reciever = new JoystickReciever(mutex);
		worker = new COMWorker();
		initCodes();
		workingThread = new Thread(this);
		
	}

	
	public void start() {
		workingThread.start();
	}
	
	
	public void stop() {
		worker.stop();
	}
	
	
	
	@Override
	public void run() {
	
		while(true) 
		{	
			
			Map<String, Float> polls = reciever.getPolls();
			Map<String, Float> copyPolls = null;
			
			//if(first_time || worker.isRead()) {
				
				synchronized(mutex) {
					copyPolls = new HashMap<String, Float>(polls);
				}
	
				int val = 0;
				if(!copyPolls.isEmpty()) 
				{
					for(Map.Entry<String, Float> entry : copyPolls.entrySet()) {
						
						val = (int)(entry.getValue()*1000);
						
						//if(Math.abs(last_val-val) > 10) {
						int norm_val = (int) map(val, 0, 1000, 1000, 2000);
						worker.dropReadingState();
						worker.write(String.valueOf(norm_val)+";");
						System.out.println(norm_val);
						last_val = val;
						
						//if(first_time) first_time = false;
						//}
					}
	
					polls.clear();
					copyPolls.clear();
				//}
				//else {
					//worker.dropReadingState();
					//worker.write(String.valueOf(last_val));
				//}

			}
		}
	}
	
	
	
	
	
	
	long map(long x, long in_min, long in_max, long out_min, long out_max)
	{
	  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	
	
	private void initCodes() {
		codes.put("Slider", 0x01);
	}
	
	
	
	
	
	private JoystickReciever reciever = null;
	private COMWorker worker = null;
	
	private Thread workingThread = null;
	private Object mutex = new Object();
	private final Map<String, Integer> codes = new HashMap<String, Integer>();
	
	private boolean first_time = true;
	int last_val = 0;
}
