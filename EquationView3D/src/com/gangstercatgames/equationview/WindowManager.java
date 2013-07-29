package com.gangstercatgames.equationview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * Manager for the viewer's window. Only one viewer may be used at any given
 * time.
 * 
 * @author Nicholas Guichon
 */
public class WindowManager {
	// Singleton instance
	private static WindowManager mInstance;

	// Vars
	private Shell mWindow;
	private Display mDisplayReference;
	private EquationsController mEquationControl;
	private ThreeDimensionalGraph mGraph;

	/**
	 * Default constructor. Creates a window and adds a resize listener, as well
	 * as the Equation Controller and 3DGraph
	 */
	private WindowManager() {
		// Create and initialize the window
		CreateWindow();

		// Create the two parts
		mEquationControl = new EquationsController(mWindow, SWT.BORDER);
		mGraph = new ThreeDimensionalGraph(mWindow, SWT.NONE);

		// Add the resize listener
		mWindow.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				mEquationControl.SetLocation(mWindow.getClientArea().width / 2);
				mGraph.setBounds(mWindow.getClientArea());
			}

		});

		// Open the window
		mWindow.open();
	}

	/**
	 * Creates the actual window shell.
	 */
	private void CreateWindow() {
		mDisplayReference = Display.getDefault();

		mWindow = new Shell(mDisplayReference);

		mWindow.setText("Equation View 3D - " + Main.VERSION_STRING);
		mWindow.setBounds(100, 100, 800, 600);
	}

	/**
	 * Singleton get method. Returns the stored copy of this WindowManager OR
	 * creates a new one and returns it.
	 * 
	 * @return Singleton instance of WindowManager
	 */
	public static WindowManager get() {
		if (mInstance == null)
			mInstance = new WindowManager();
		return mInstance;
	}

	/**
	 * Reads and handles all input calls for the window.
	 */
	public void Update() {
		if (!mDisplayReference.readAndDispatch()) {
			mDisplayReference.sleep();
		}
	}

	/**
	 * Checks if the window is closed or open.
	 * 
	 * @return TRUE if the window has been closed, FALSE if open
	 */
	public boolean isClosed() {
		return mWindow.isDisposed();
	}
}
