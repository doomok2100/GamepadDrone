package com.kripton.gamepad.ui;


public class MainWindow {
	
	
	public static void main(String args[]) {
			
		con.start();
		//Finalizer fin = new Finalizer();
	}
	
	
	
	
	static class Finalizer {	
		protected void finalize() throws Throwable {
			con.stop();
			System.out.println("finalized");
		}
	}
	
	static Controller con = new Controller();

}
