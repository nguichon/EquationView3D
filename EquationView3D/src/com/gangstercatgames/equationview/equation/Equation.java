package com.gangstercatgames.equationview.equation;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

import com.gangstercatgames.equationview.equation.node.EquationNode;
import com.gangstercatgames.equationview.equation.node.RawValueNode;

public class Equation {
	private EquationNode mHead;
	private float[][] mMesh;
	private float mStep;
	private int mSteps;
	private int mStart;
	private boolean mCanGraph = false;
	
	public Equation( String equation ) {
		try{
			mHead = EquationNode.ParseEquationString( equation );
		} catch( Exception e ) {
			mHead = new RawValueNode( 0f );
		}
	}
	
	public float SolveEquationUsing(Hashtable<String, Float> variables) {
		return mHead.Solve( variables );
	}
	
	public void CalculateMesh( float step, int steps, int start ) {
		mCanGraph = false;
		mMesh = new float[steps][steps];
		mStep = step;
		mSteps = steps;
		mStart = start;
		Hashtable<String, Float> vars = new Hashtable<String, Float>();
		for( int i = 0; i < steps; i++ ) {
			vars.put("x", (float)i*step+start);
			for( int j = 0; j < steps; j++ ) {
				vars.put("y", (float)j*step+start);
				mMesh[i][j] = SolveEquationUsing( vars );
				System.out.println(vars.get("x")+","+vars.get("y")+","+mMesh[i][j] );
			}
		}
		mCanGraph = true;
	}
	
	public float GetStep() {
		return mStep;
	}
	public int GetSteps() {
		return mSteps;
	}
	public int GetStart() {
		return mStart;
	}
	public boolean CanGraph() {
		return mCanGraph;
	}
	
	public float GetPoint( int x, int y ) {
		return mMesh[x][y];
	}
}
