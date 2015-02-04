package com.kripton.gamepad;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;


public class JoystickReciever implements Runnable {

	
	public JoystickReciever(Object _mutex) {
		mutex = _mutex;
		init();
	}
	
	private void init() {
		
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		
		for(int i=0; i<controllers.length; i++) 
		{
			System.out.println(controllers[i].getName()+ " : " + controllers[i].getType());
			
			if(controllers[i].getType() == Controller.Type.STICK) 
			{
				System.out.println("Found");
				gamepad = controllers[i];
				comps = gamepad.getComponents();
				break;
			}
		}

		pollThread = new Thread(this);
		pollThread.start();
	}

	
	public void run() {

		StringBuilder builder = new StringBuilder();
		if(comps != null) 
		{
			while(true) 
			{
				gamepad.poll();
				
				synchronized(mutex) {
					for(int j=0; j<comps.length; j++) 
					{
						
						if(comps[j].isAnalog()) 
						{
						
							float poll = comps[j].getPollData();
							/* Get middle value of 1000 */
							if(poll != 0.0f) 
							{
								val += poll;
								if(k == 500) {
									val /= k;
									polls.put(comps[j].getName(), val);
									k = 0;
									val = 0;
								}
								
								k++;
							}
						}
						else if(comps[j].getPollData() == 1.0f) {
							builder.append(comps[j].getName());
							builder.append(" : ");
							builder.append("On");
							builder.append(";");
						}
						
					}
				}
				
				builder.setLength(0);
			}
		}
		
	}
	
	
	public Map<String, Float> getPolls() {
		return polls;
	}
	
	Controller gamepad = null;
	Component[] comps = null;
	Thread pollThread = null;
	Map<String, Float> polls = Collections.synchronizedMap(new HashMap<String, Float>());
	Object mutex = null;
	
	int k = 0;
	float val = 0;
}
