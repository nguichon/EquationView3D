package com.gangstercatgames.equationview.equation.node;

public class RawValueNode extends EquationNode {
	private float mValue;
	
	public RawValueNode( float v ) {
		mValue = v;
	}
	
	@Override
	public float Solve() {
		return mValue;
	}

}
