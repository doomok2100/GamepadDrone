package com.kripton.COM;

import gnu.io.SerialPort;


import java.io.IOException;

import java.io.OutputStream;


public class COMWriter {
	
	
	public COMWriter(SerialPort _serial) {
		serial = _serial;
		init();
	}
	
	
	public void close() throws IOException {
		outputStr.close();
	}
	
	
	private void init() {

		try {
			outputStr = serial.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public void write(String data) {
		try {
			outputStr.write(data.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	private OutputStream outputStr = null;
	private SerialPort serial;

}
