package com.kripton.gamepad.visual;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

public class Renderer implements GLEventListener {

	
	
	@Override
	public void display(GLAutoDrawable drawable) {

		GL2 gl = drawable.getGL().getGL2();	
		GLUT glut = new GLUT();
		
		//GLU glu = new GLU();
		//glu.gluLookAt(4f, 2f, 2f, arg3, arg4, arg5, arg6, arg7, arg8);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glTranslatef(0f, 0f, -3f);
		//glut.glutSolidTeapot(1);
		
		gl.glBegin(GL2.GL_QUADS);
		for(int i=0; i<8; i++)
		{	
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			gl.glVertex3f(placeRaw[i][0], placeRaw[i][1], placeRaw[i][2]);
		}
		gl.glEnd();
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
		glu.gluPerspective(45f, 4f/3f, 0.1f, 30f);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[]{1f, 1f, 1f, 1f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[]{0f, 0f, 1f, 0f}, 0);
        
        //gl.glTranslatef(0f, 0f, -5f);
        
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub
		
	}
	
	private float[][] placeRaw = new float[][] 	{
													{0f, 0f,   0f, 1f},
													{0f, 0f,   0f, 1f},
													{2f, 0f,   -1f, 1f},
													{2f, 0f,   -1f, 1f},
													{0f, 0.5f, 0f, 1f},
													{0f, 0.5f, 0f, 1f},
													{2f, 0.5f, -1f, 1f},
													{2f, 0.5f, -1f, 1f}
												};

}
