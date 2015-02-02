package com.kripton.gamepad;
import com.kripton.COM.COMWorker;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;


public class JoystickReciever {

	
	public static void main(String args[]) {
		
		Controller gamepad = null;
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		Component[] comps = null;
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
		
		COMWorker worker = new COMWorker();
		worker.write("Hello world!!!");
		StringBuilder builder = new StringBuilder();
		
		if(comps != null) 
		{
			while(true) 
			{
				gamepad.poll();
				for(int j=0; j<comps.length; j++) 
				{
					
					if(comps[j].isAnalog()) 
					{
						float poll = comps[j].getPollData();
						if(poll != 0.0f) 
						{
							builder.append(comps[j].getName());
							builder.append(" : ");
							builder.append(poll);
							builder.append(";");
						}
					}
					else if(comps[j].getPollData() == 1.0f) {
						builder.append(comps[j].getName());
						builder.append(" : ");
						builder.append("On");
						builder.append(";");
					}
					
				}					
				System.out.println(builder.toString());
				builder.setLength(0);
				
			}
		}
		
	}
	
}
