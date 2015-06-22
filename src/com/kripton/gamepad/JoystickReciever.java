package com.kripton.gamepad;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;


public class JoystickReciever implements Runnable {

	
	public JoystickReciever() {
		init();
	}
	
	private void init() {
		
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		
		for(int i=0; i<controllers.length; i++) 
		{
			System.out.println(controllers[i].getName()+ " : " + controllers[i].getType());
			
			if(controllers[i].getType() == Controller.Type.STICK) 
			{	
				/* Get gamepad and its components */
				gamepad = controllers[i];
				comps = gamepad.getComponents();
				
				/* Initialize buffers for keeping number of coming values and values sum. */
				numOfComeVals = new int[comps.length];
				valueBuffer = new float[comps.length];
				lastValueBuffer = new float[comps.length];
				zeroesCome = new int[comps.length];
				
				for (int j=0; j<comps.length; j++) 
				{
					numOfComeVals[j] = 0;
					valueBuffer[j] = 0;
					lastValueBuffer[j] = 0;
					zeroesCome[j] = 0;
				}
				
				
				PrintWriter writer = null;
				try 
				{
					writer = new PrintWriter("codes.txt", "UTF-8");
					for(int j=0; j<comps.length; j++) 
					{	
						String name = comps[j].getName();
						writer.println(name+":"+String.valueOf(j));
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					writer.close();
				}

				
				break;
			}
		}
	}
	
	
	public void start() {
		pollThread = new Thread(this);
		pollThread.start();
		started = true;
	}
	
	
	public boolean isGamepadFound()
	{
		return (gamepad != null) ? true : false; 
	}

	
	public void run() {

		if(comps != null) 
		{
			while(true) 
			{
				gamepad.poll();
				
				synchronized(polls) {
					for(int j=0; j<comps.length; j++) 
					{

						if(comps[j].isAnalog()) 
						{
							
							float poll;
							
							/* Preventing extra zeros that are coming because of fucking joy */
							/*if(((poll = comps[j].getPollData()) == 0.0f)) 
							{		
								if(lastValueBuffer[j] == 0) 
								{
									if(++zeroesCome[j] < 1000) 
									{
										lastValueBuffer[j] = 0;
										continue;
									}
									else {
										zeroesCome[j] = 0;
									}
								}
							}*/
							
							/* Get data from current component and plus it to saved value */
							poll = comps[j].getPollData();
							valueBuffer[j] += poll;
							lastValueBuffer[j] = poll;
							
							/* if there are 500 of coming values from current component */
							/* then get middle value and put it to polls */
							if(numOfComeVals[j] == 500)
							{
								valueBuffer[j] /= numOfComeVals[j];
								polls.put(comps[j].getName(), valueBuffer[j]);
								numOfComeVals[j] = 0;
								valueBuffer[j] = 0;
							}
							
							numOfComeVals[j]++;
						}
						else {
							polls.put(comps[j].getName(), comps[j].getPollData());
						}
						
					}
				}
			}
		}
		
	}
	
	
	public Map<String, Float> getPolls() {
		return polls;
	}
	
	/* Keep gamepad and its components */
	Controller gamepad = null;
	Component[] comps = null;
	
	/* Keep number of values that has already come. Need for find middle value */
	/* Coming values keeps in buffer and then their middle value is put to polls */
	int[] numOfComeVals;
	float[] valueBuffer;
	
	/* For preventing extra zeroes */
	int[] zeroesCome;
	float[] lastValueBuffer;
	
	/* Keep component name and its value */
	Map<String, Float> polls = Collections.synchronizedMap(new HashMap<String, Float>());
	Thread pollThread = null;
	
	public volatile boolean started = false;

}
