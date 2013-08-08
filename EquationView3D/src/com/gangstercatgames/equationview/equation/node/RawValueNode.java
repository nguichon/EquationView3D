package com.gangstercatgames.equationview.equation.node;

import java.util.Hashtable;

public class RawValueNode extends EquationNode {
	private float mValue;
	
	public RawValueNode( float v ) {
		mValue = v;
	}
	
	@Override
	public float Solve( Hashtable<String, Float> variables ) {
		return mValue;
	}

}
