package com.kripton.COM;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.Enumeration;

import javax.swing.JComboBox;

public class COMWorker {
	
	
	public COMWorker() {
		reader = new COMReader();
	}
	
	
	public void write(String data) {
		synchronized(this)
		{
			if(writer != null) {
				writer.write(data);
			}
		}
	}
	
	
	public boolean isRead() {
		synchronized(this)
		{
			return reader.isRead();
		}
	}
	
	
	public String getReadData() {
		synchronized(this)
		{
			bufferedData = reader.getReadData();
			return bufferedData;
		}
	}
	
	/*
	 * Return last read value
	 */
	public String getBufferedData() {
		synchronized(this)
		{
			if(bufferedData != null)
			{
				return bufferedData;
			}
			
			return new String("");
		}	
	}
	
	
	public void closePort() {
		synchronized(this)
		{
			try {
				if(serial != null)
				{
					writer.close();
					reader.close();
					serial.close();							
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void showCOMPorts(JComboBox ports) {
		synchronized(this)
		{
			portList = CommPortIdentifier.getPortIdentifiers();
	
			while(portList.hasMoreElements()) 
			{
				comm = (CommPortIdentifier) portList.nextElement();
				if(comm.getPortType() == CommPortIdentifier.PORT_SERIAL) 
				{
					String name = comm.getName();
					ports.addItem(name);
				}	
			}
		}
	}
	
	
	public void openCOMPort(String name, int baud) {
		synchronized(this)
		{
			portList = CommPortIdentifier.getPortIdentifiers();
			
			try {
				
				while(portList.hasMoreElements()) 
				{
					comm = (CommPortIdentifier) portList.nextElement();
					System.out.println(comm.getName());
					if((comm.getPortType() == CommPortIdentifier.PORT_SERIAL) && comm.getName().equals(name)) 
					{
						serial = (SerialPort) comm.open("MyApp", 2000); //+
						reader.setPort(serial);
						writer = new COMWriter(serial);
						serial.setSerialPortParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, 
								SerialPort.PARITY_NONE); //++
						opened = true;
						reader.start();
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
	}
	
	
	
	public boolean isOpened() {
		synchronized(this)
		{
			return opened;
		}
	}
	
	
	private COMWriter writer = null;
	private COMReader reader = null;
	@SuppressWarnings("rawtypes")
	private Enumeration portList = null;
	private CommPortIdentifier comm = null;
	private SerialPort serial = null;

	private boolean opened = false;
	private String bufferedData = null;
}
