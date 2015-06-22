package com.kripton.COM;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;


public class COMReader implements Runnable, SerialPortEventListener {

	
	public COMReader() {
		
	}
	
	
	public void setPort(SerialPort _serial) {
		
		serial = _serial;
		
		try {
			inStr = serial.getInputStream();
			opened = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public boolean isRead() {
		return wasRead;
	}
	
	
	public void close() throws IOException {
		
		if(opened) 
		{
			inStr.close();
			opened = false;
		}
	}
	
	
	public void start() {
		readerThread = new Thread(this);
		readerThread.start();
	}
	
	
	public String getReadData() {
		if(wasRead) 
		{
			byte[] buf = new byte[pos];
			
			for(int i=0; i<BUFFER_SIZE; i++) 
			{
				if(i < pos) 
				{
					buf[i] = readBuffer[i];
				}
				readBuffer[i] = '\0';
			}			
			pos = 0;
		
			String res = "";
			synchronized(readData) 
			{
				ListIterator<String> iter = readData.listIterator();
			
				while(iter.hasNext())
				{
					res += iter.next();
				}
				readData.clear();
			}
			res += new String(buf);
			
			wasRead = false;
			
			return (res);
		}
		else {
			return null;
		}
	}
	
	
	@Override
	public void run() {
		
		try {
			while(true) 
			{	
				if(!wasRead) 
				{
					while(inStr.available() > 0) 
					{	
						if(pos == BUFFER_SIZE-1) 
						{
							synchronized(readData) 
							{
								String data = getReadData();
								readData.add(data);
							}
						}
						
	                    inStr.read(readBuffer, pos++, 1);
	                    
	                    if((readBuffer[pos-1] == '\n') && (readBuffer[0] != '\n')) 
	                    {
	                    	wasRead = true;
	                    	break;
	                    }
	                    else if(readBuffer[0] == '\n') 
	                    {
	                    	readBuffer[0] = '\0';
	                    	pos = 0;
	                    }
					}
				}
			}
		} catch (IOException e) {
        	System.out.println(e);
        }
		
	}
	
	
	public void serialEvent(SerialPortEvent event) {
       
		switch(event.getEventType()) {
	        
			case SerialPortEvent.BI:
	        case SerialPortEvent.OE:
	        case SerialPortEvent.FE:
	        case SerialPortEvent.PE:
	        case SerialPortEvent.CD:
	        case SerialPortEvent.CTS:
	        case SerialPortEvent.DSR:
	        case SerialPortEvent.RI:
	        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
	            break;
	        
	        case SerialPortEvent.DATA_AVAILABLE:
	        	
	            break;
        }
    }
	
	
	
	private SerialPort serial = null;
	private InputStream inStr = null;
	
	private final int BUFFER_SIZE = 1024;

	private Thread readerThread;
	private int pos = 0;
	private volatile boolean wasRead = false;
	byte[] readBuffer = new byte[BUFFER_SIZE];
	
	private boolean opened = false;
	List<String> readData = new ArrayList<String>();
	
}
