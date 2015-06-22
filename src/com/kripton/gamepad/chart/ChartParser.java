package com.kripton.gamepad.chart;

public class ChartParser {
	
	public ChartParser() {
		
	}
	
	
	public double parse(String str, int pos) {
		
		String[] buf = str.split(",");
		
		if(pos < buf.length && pos >= 0)
		{
			return Double.valueOf(buf[pos]);
		}

		return 0;		
	}
	
	
	public int getParamsCount(String data) {
		
		System.out.println("Data: " + data);
		
		return data.split(",").length;
		
	}
	
	
}
