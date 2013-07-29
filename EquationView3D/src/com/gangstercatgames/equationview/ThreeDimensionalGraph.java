package com.gangstercatgames.equationview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Composite that wraps all OpenGL functions and renders the functions to its
 * canvas.
 * 
 * @author Nicholas Guichon
 */
public class ThreeDimensionalGraph extends Composite {
	private GLCanvas mCanvas;
	private GLData mData;

	/**
	 * Constructor to create the composite on it's parent
	 * 
	 * @param parent
	 *            Parent composite/shell that houses this composite
	 * @param style
	 *            SWT styles
	 */
	public ThreeDimensionalGraph(Composite parent, int style) {
		super(parent, style);

		// Create the OpenGLData
		mData = new GLData();
		mData.doubleBuffer = true;

		// Initialize the canvas
		mCanvas = new GLCanvas(this, SWT.NONE, mData);

		// Handle resizes
		this.addListener(SWT.RESIZE, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				mCanvas.setBounds(getBounds());
			}

		});
	}
}
