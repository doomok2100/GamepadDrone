package com.kripton.COM;

import java.io.IOException;
import java.io.InputStream;
import java.util.TooManyListenersException;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;


public class COMReader implements Runnable, SerialPortEventListener {

	
	public COMReader(SerialPort _serial) {
		
		serial = _serial;
		
		try {
			inStr = serial.getInputStream();
			serial.addEventListener(this);
			serial.notifyOnDataAvailable(true);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
		
		readThread = new Thread(this);
	}
	
	
	public void start() {
		readThread.start();
	}

	@Override
	public void run() {
		
		synchronized(mutex) {
			
			while(!run) {
				
				try 
				{
					mutex.wait();	
				
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
			}
			
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
	        	
	        	byte[] readBuffer = new byte[20];
	            try {
	                while (inStr.available() > 0) {
	                    inStr.read(readBuffer);
	                }
	                System.out.print(new String(readBuffer));
	            } catch (IOException e) {
	            	System.out.println(e);
	            }
	            break;
        }
    }
	
	
	private SerialPort serial = null;
	private InputStream inStr = null;
	private Thread readThread = null;
	private Object mutex = new Object();
	private boolean run = false;
	
}
