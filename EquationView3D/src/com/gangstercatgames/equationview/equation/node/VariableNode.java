package com.gangstercatgames.equationview.equation.node;

import java.util.Hashtable;

public class VariableNode extends EquationNode {
	String mVariableIdentifier;
	
	public VariableNode( String identifier ) {
		mVariableIdentifier = identifier;
	}
	
	@Override
	public float Solve( Hashtable<String, Float> variables ) {
		return variables.get( mVariableIdentifier );
	}
}
