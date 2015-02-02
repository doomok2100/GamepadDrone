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
	
	
	
	@Override
	public void run() {
		
		while(true) 
		{	
			Map<String, Float> polls = reciever.getPolls();
			if(!polls.isEmpty()) 
			{
				synchronized(mutex) 
				{
					for(Map.Entry<String, Float> entry : polls.entrySet()) {
						worker.write(codes.get(entry.getKey())+":"+entry.getValue());
					}
				}
			}
			polls.clear();
		}
	}
	
	
	private void initCodes() {
		codes.put("Slider", 0x01);
	}
	
	
	private JoystickReciever reciever = null;
	private COMWorker worker = null;
	
	private Thread workingThread = null;
	private Object mutex = new Object();
	private final Map<String, Integer> codes = new HashMap<String, Integer>();
}
