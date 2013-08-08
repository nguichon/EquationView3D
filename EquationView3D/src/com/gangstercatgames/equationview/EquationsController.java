package com.gangstercatgames.equationview;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.gangstercatgames.equationview.equation.Equation;

/**
 * Handles the widgets associated with the equation editor.
 * 
 * @author Nicholas Guichon
 * 
 */
public class EquationsController extends Composite {
	private Button mBtnDrawerControl;
	private Button mBtnGraph;
	private Button mBtnRevertChanges;
	private Text mTxtEquation;
	private Equation mEquation = new Equation( "0" );
	private boolean mOpen = true;
	private String mCurrentEquation = "";

	/**
	 * Constructor to create the composite on it's parent
	 * 
	 * @param parent
	 *            Parent composite/shell that houses this composite
	 * @param style
	 *            SWT styles
	 */
	public EquationsController(Composite parent, int style) {
		super(parent, style);

		// Create the grid layout
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		this.setLayout(gridLayout);

		// Recycle the reference to GridData.
		GridData gridData;

		// Entry field for the equation text.
		mTxtEquation = new Text(this, SWT.MULTI | SWT.WRAP | SWT.BORDER
				| SWT.V_SCROLL);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		mTxtEquation.setLayoutData(gridData);
		mTxtEquation.setSize(0, mTxtEquation.getLineHeight() * 3);

		// Button to cause the equation to be graphed.
		mBtnGraph = new Button(this, SWT.PUSH);
		mBtnGraph.setText("Graph Function");
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		mBtnGraph.setLayoutData(gridData);
		mBtnGraph.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				GraphEquation();
			}

		});

		// Button to revert equation to the last equation graphed
		mBtnRevertChanges = new Button(this, SWT.PUSH);
		mBtnRevertChanges.setText("Revert");
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		mBtnRevertChanges.setLayoutData(gridData);
		mBtnRevertChanges.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				RevertEquation();
			}

		});

		// Button to open or close the equation drawer
		mBtnDrawerControl = new Button(this, SWT.PUSH);
		mBtnDrawerControl.setText("Open/Close");
		gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = GridData.FILL;
		mBtnDrawerControl.setLayoutData(gridData);
		mBtnDrawerControl.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ToggleOpen();
			}

		});

		this.setSize(250, 200);
		ToggleOpen();
	}

	/**
	 * Sets the location of the equation editor horizontally only.
	 * 
	 * @param x
	 *            x of the point to set the CENTER of this composite to
	 */
	public void SetLocation(int x) {
		this.setLocation(x - (this.getSize().x / 2), this.getLocation().y);
	}

	/**
	 * Opens or closes this composite.
	 */
	public void ToggleOpen() {
		if (mOpen) {
			this.setLocation(this.getLocation().x,
					-1 * mBtnDrawerControl.getLocation().y);
		} else {
			this.setLocation(this.getLocation().x, 0);
		}
		mOpen = !mOpen;
	}
	
	public Equation GetGraph() {
		return mEquation;
	}

	private void RevertEquation() {
		mTxtEquation.setText(mCurrentEquation);
	}

	private void GraphEquation() {
		mCurrentEquation = mTxtEquation.getText();
		mEquation = new Equation( mCurrentEquation );
		mEquation.CalculateMesh( 0.25f, 80, -10 );
	}
}
