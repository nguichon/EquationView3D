package com.gangstercatgames.equationview.equation.node;

import java.util.LinkedList;

public class LinearOperationChainNode extends EquationNode {
	private EquationNode mBase;
	private LinkedList<Operation> mOperations = new LinkedList<Operation>();

	public LinearOperationChainNode(EquationNode base) {
		mBase = base;
	}

	public void AddOperation(OperationType typeOfOperation,
			EquationNode valueOfOperation) {
		mOperations.add(new Operation(typeOfOperation, valueOfOperation));
	}

	@Override
	public float Solve() {
		float f = mBase.Solve();
		
		for( Operation o : mOperations ) {
			f = o.Operate( f );
		}
		
		return f;
	}

	public enum OperationType {
		ADD {

			@Override
			float OperateBetweenTwo(float left, float right) {
				return left + right;
			}

		},
		SUBTRACT {

			@Override
			float OperateBetweenTwo(float left, float right) {
				return left - right;
			}

		},
		MULTIPLY {

			@Override
			float OperateBetweenTwo(float left, float right) {
				return left * right;
			}

		},
		DIVIDE {

			@Override
			float OperateBetweenTwo(float left, float right) {
				return left / right;
			}

		},
		POWEROF {

			@Override
			float OperateBetweenTwo(float left, float right) {
				return (float) Math.pow( left, right );
			}
		};
		abstract float OperateBetweenTwo(float left, float right);
	}

	private class Operation {
		private OperationType mType;
		private EquationNode mValue;

		public Operation(OperationType typeOfOperation,
				EquationNode valueOfOperation) {
			mType = typeOfOperation;
			mValue = valueOfOperation;
		}
		
		public float Operate( float left ) {
			return mType.OperateBetweenTwo( left, mValue.Solve() );
		}
	}
}
