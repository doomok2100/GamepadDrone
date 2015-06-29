package com.kripton.gamepad.visual;



import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

public class GLFrame extends Frame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5108751698360936321L;
	
	GLCanvas canvas;
	Animator anim;
	
	public GLFrame() {
		
		canvas = new GLCanvas();
		
		canvas.setSize(800, 640);
		canvas.setIgnoreRepaint(true);
		canvas.addGLEventListener(new Renderer());
		
		setTitle("Gyro realtime visualising");
		setLayout(new BorderLayout());
		
		add(canvas, BorderLayout.CENTER);
		setSize(getPreferredSize());
		
		anim = new Animator(canvas);
		anim.setRunAsFastAsPossible(true);
		
		anim.start();
			
		addWindowListener(new java.awt.event.WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				anim.stop();
				dispose();
			}
			
		});
		
	}

}
