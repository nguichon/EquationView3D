package com.gangstercatgames.equationview;

import java.awt.MouseInfo;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

import com.gangstercatgames.equationview.equation.Equation;

/**
 * Composite that wraps all OpenGL functions and renders the functions to its
 * canvas.
 * 
 * @author Nicholas Guichon
 */
public class ThreeDimensionalGraph extends Composite {
	//OpenGL Data
	private GLCanvas mCanvas;
	private GLData mData;
	
	//Mouse Tracking Data
	private boolean mTrackingMouse = false;
	private Point mLastMouseLocation;
	private float[] mAxisRotations = new float[3];
	private int X = 0; private int Y = 1; private int Z = 2;
	private float mZoomValue = 1.0f;
	private float mRatio = 1.0f;
	private final static float ZOOM_SPEED = 0.5f;
	private WindowManager mHost;

	/**
	 * Constructor to create the composite on it's parent
	 * 
	 * @param parent
	 *            Parent composite/shell that houses this composite
	 * @param style
	 *            SWT styles
	 */
	public ThreeDimensionalGraph(Composite parent, int style, WindowManager wm) {
		super(parent, style);

		mHost = wm;
		// Create the OpenGLData
		mData = new GLData();
		mData.doubleBuffer = true;

		// Initialize the canvas
		mCanvas = new GLCanvas(this, SWT.NONE, mData);
		mCanvas.setCurrent();

		// Set LWJGL to use this canvas
		try {
			GLContext.useContext(mCanvas);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		// Setup OpenGL
		InitializeOpenGL();

		// Handle resizes
		this.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				mCanvas.setBounds(getBounds());
			}
		});
		mCanvas.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				Point size = mCanvas.getSize();
				GL11.glViewport(0, 0, size.x, size.y);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				mRatio = (float) size.x / (float) size.y;
				GLU.gluPerspective( 0.0f, (float) size.x / (float) size.y,
						0.1f, 100.0f );

				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
			}
		});
		
		mCanvas.addMouseListener( new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				mTrackingMouse = true;
				mLastMouseLocation = Display.getCurrent().getCursorLocation();
			}

			@Override
			public void mouseUp(MouseEvent arg0) {
				mTrackingMouse = false;
			}
			
		});
		
		mCanvas.addMouseWheelListener( new MouseWheelListener() {

			@Override
			public void mouseScrolled(MouseEvent arg0) {
				if( arg0.count > 0 ) {
					mZoomValue *= 1 + ZOOM_SPEED;
				} else {
					mZoomValue /= 1 + ZOOM_SPEED;
				}
			}
			
		});

	}

	/**
	 * Setups initial OpenGL parameters
	 */
	private void InitializeOpenGL() {
//		ClearBuffer();
//		GL11.glEnable(GL11.GL_DEPTH_TEST);
//		GL11.glDepthFunc(GL11.GL_LEQUAL);
//
//		GL11.glMatrixMode(GL11.GL_PROJECTION);
//		GL11.glLoadIdentity(); // Reset axis and view
//		GL11.glOrtho(0, 800, 0, 600, 1, -1);
//		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glColor3f(1.0f, 0.0f, 0.0f);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		GL11.glClearDepth(1.0);
		GL11.glLineWidth(2);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	/**
	 * Creates and starts a thread relating to rendering
	 */
	public void Start3DThread() {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				mCanvas.setCurrent();
				
				try {
					GLContext.useContext(mCanvas);
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
				
				ClearBuffer();
				TrackMouse();
				SetupCamera();
				DrawAxes();
				RenderMesh();
				
				mCanvas.swapBuffers();
				
				Display.getDefault().asyncExec(this);
			}

		};

		Display.getDefault().asyncExec(r);
	}

	protected void RenderMesh() {
		Equation eq = mHost.GetGraph();
		if( eq.CanGraph() ) {
			int start = eq.GetStart();
			float step = eq.GetStep();
			int count = eq.GetSteps();
			/*GL11.glBegin(GL11.GL_LINES);
			for( int i = 0; i < count; i++ ) {
				for( int j = 0; j < count; j++ ) {
					float z = eq.GetPoint( i, j );
					float y = j * step + start;
					float x = i * step + start;
					
					GL11.glVertex3f( x, y, z );
				}
			}
			GL11.glEnd();*/

			System.out.println(start + ":" + step + ":" + count);
			
			GL11.glBegin(GL11.GL_QUADS);
			for( int i = 0; i < count - 1; i++ ) {
				for( int j = 0; j < count - 1; j++ ) {
					float z = eq.GetPoint( i, j );
					float y = j * step + start;
					float x = i * step + start;
					GL11.glVertex3f( -x, -y, z );
					z = eq.GetPoint( i+1, j+1 );
					y = (j+1) * step + start;
					x = (i+1) * step + start;
					GL11.glVertex3f( -x, -y, z );
					z = eq.GetPoint( i+1, j );
					y = j * step + start;
					x = (i+1) * step + start;
					GL11.glVertex3f( -x, -y, z );
					z = eq.GetPoint( i, j+1 );
					y = (j+1) * step + start;
					x = i * step + start;
					GL11.glVertex3f( -x, -y, z );
				}
			}
			GL11.glEnd();
			
		}
	}

	/**
	 * Reset the screen to a default black state
	 */
	private void ClearBuffer() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor( 0.0f, 0.0f, 0.0f, 0.0f );
		GL11.glLoadIdentity();
	}
	
	private void SetupCamera() {
		GL11.glScaled( mZoomValue / mRatio, mZoomValue, 0.1);
		GL11.glRotatef( mAxisRotations[X], 1.0f, 0.0f, 0.0f);
		GL11.glRotatef( mAxisRotations[Y], 0.0f, 1.0f, 0.0f);
		GL11.glRotatef( mAxisRotations[Z], 0.0f, 0.0f, 1.0f);
	}
	
	private void TrackMouse() {
		if( mTrackingMouse ) {
			Point p = Display.getCurrent().getCursorLocation();
			
			StepRotationAroundY( (float)(mLastMouseLocation.x - p.x) / 5);
			StepRotationAroundX( (float)(mLastMouseLocation.y - p.y) / 5);

			mLastMouseLocation = p;
		}
	}
	
	/**
	 * Draws all the axes in order of X, Y, Z
	 */
	private void DrawAxes() {
		//Draw the X axis
		GL11.glLineWidth( 2.5f ); 
		GL11.glColor3f( 1.0f, 0.0f, 0.0f );
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f( -15.0f, 0.0f, 0.0f );
		GL11.glVertex3f( 15.0f, 0.0f, 0.0f );
		GL11.glEnd();
		
		GL11.glLineWidth( 0.5f );
		for( int i = -15; i <= 15; i++ ) {
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex3f( -15.0f, i, 0.0f );
			GL11.glVertex3f( 15.0f, i, 0.0f );
			GL11.glEnd();
		}

		//Draw the X axis
		GL11.glLineWidth( 2.5f ); 
		GL11.glColor3f( 0.0f, 1.0f, 0.0f );
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f( 0.0f, -15.0f, 0.0f );
		GL11.glVertex3f( 0.0f, 15.0f, 0.0f );
		GL11.glEnd();
		
		GL11.glLineWidth( 0.5f );
		for( int i = -15; i <= 15; i++ ) {
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex3f( i, -15.0f, 0.0f );
			GL11.glVertex3f( i, 15.0f, 0.0f );
			GL11.glEnd();
		}

		//Draw the Z axis
		GL11.glLineWidth( 2.5f ); 
		GL11.glColor3f( 0.0f, 0.0f, 1.0f );
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f( 0.0f, 0.0f, -15.0f );
		GL11.glVertex3f( 0.0f, 0.0f, 15.0f );
		GL11.glEnd();
	}
	
	//Functions to increment rotation by a specified amount.
	private void StepRotationAroundX( float increment ) { mAxisRotations[X] += increment; }
	private void StepRotationAroundY( float increment ) { mAxisRotations[Y] += increment; }
	private void StepRotationAroundZ( float increment ) { mAxisRotations[Z] += increment; }
}
