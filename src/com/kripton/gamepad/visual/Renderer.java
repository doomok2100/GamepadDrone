package com.kripton.gamepad.visual;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.math.Quaternion;
import com.jogamp.opengl.util.gl2.GLUT;

public class Renderer implements GLEventListener {

	
	
	@Override
	public void display(GLAutoDrawable drawable) {

		GL2 gl = drawable.getGL().getGL2();	
		GLU glu = new GLU();		
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		glu.gluLookAt(eyePosition[0], eyePosition[1], eyePosition[2], worldCenter[0], worldCenter[1], worldCenter[2], 
				upVector[0], upVector[1], upVector[2]);
		gl.glRotatef(rot, 0.0f, 1.0f, 0.0f);
		
		gl.glBegin(GL2.GL_QUADS);	
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		for(int i=0; i<24; i++)
		{				
			gl.glVertex3f(placeRaw[i][0], placeRaw[i][1], placeRaw[i][2]);
		}
		gl.glEnd();
		
		rot = rot%360 + 0.5f;
	}
	

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		
		GL2 gl = drawable.getGL().getGL2();
		GLU glu = new GLU();
		
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glClearDepth(1f);
		gl.glDepthFunc(GL.GL_LESS);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45f, 4f/3f, 0.1f, 100f);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		
		rotation.setFromEuler(angles);   
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub
		
	}
	
	
	private void rotate3DVector(float[] vec, float[] angles) {
		
	}
	
	
	private float rot = 0.0f;
	private float[] worldCenter = new float[] {0f, 0f, 0f};
	private float[] eyePosition = new float[] {0f, -3f, -5f};
	private float[] upVector = new float[] {0f, -2f, 1.2f};
	
	private Quaternion rotation = new Quaternion();
	private float[] angles = new float[] {0.0f, 0.0f, 0.0f};
	
	private float[][] placeRaw = new float[][] 	{
													/* Bottom */
													{ 1f, -0.25f, -0.5f},
													{-1f, -0.25f, -0.5f},
													{-1f, -0.25f,  0.5f},	
													{ 1f, -0.25f,  0.5f},
													
													/* Top */
													{ 1f, 0.25f, -0.5f},
													{-1f, 0.25f, -0.5f},
													{-1f, 0.25f,  0.5f},
													{ 1f, 0.25f,  0.5f},
													
													/* Front */
													{ 1f,  0.25f,  0.5f},
													{-1f,  0.25f,  0.5f},
													{-1f, -0.25f,  0.5f},
													{ 1f, -0.25f,  0.5f},							
													
													/* Back side */
													{ 1f,  0.25f, -0.5f},
													{-1f,  0.25f, -0.5f},
													{-1f, -0.25f, -0.5f},
													{ 1f, -0.25f, -0.5f},
													
													/* Left */
													{-1f,  0.25f,  0.5f},
													{-1f,  0.25f, -0.5f},
													{-1f, -0.25f, -0.5f},
													{-1f, -0.25f,  0.5f},
													
													/* Right */
													{1f,  0.25f, -0.5f},
													{1f,  0.25f,  0.5f},
													{1f, -0.25f,  0.5f},
													{1f, -0.25f, -0.5f}
													
												};

}
