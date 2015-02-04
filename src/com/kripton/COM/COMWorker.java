package com.kripton.COM;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.Enumeration;

public class COMWorker {
	
	
	public COMWorker() {
		init();
	}
	
	
	public void write(String data) {
		if(writer != null) {
			writer.write(data);
		}
	}
	
	
	public boolean isRead() {
		return reader.isRead();
	}
	
	/* */
	public void dropReadingState() {
		reader.dropState();
	}
	
	public void stop() {
		try {
			writer.close();
			reader.close();
			serial.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void init() {
		
		portList = CommPortIdentifier.getPortIdentifiers();
		
		try {
		
			while(portList.hasMoreElements()) 
			{
				comm = (CommPortIdentifier) portList.nextElement();
				if(comm.getPortType() == CommPortIdentifier.PORT_SERIAL) 
				{
					serial = (SerialPort) comm.open("SimpleWriteApp", 2000);
					reader = new COMReader(serial);
					writer = new COMWriter(serial);
					serial.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, 
							SerialPort.PARITY_NONE);
					break;
				}
				
			}
		} catch (UnsupportedCommOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PortInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private COMWriter writer = null;
	private COMReader reader = null;
	@SuppressWarnings("rawtypes")
	private Enumeration portList = null;
	private CommPortIdentifier comm = null;
	private SerialPort serial = null;


}
