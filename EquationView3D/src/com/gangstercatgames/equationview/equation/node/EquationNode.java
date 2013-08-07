package com.gangstercatgames.equationview.equation.node;

import java.util.LinkedList;
import java.util.Queue;

import com.gangstercatgames.equationview.equation.node.LinearOperationChainNode.OperationType;

/**
 * 
 * @author Nicholas Guichon
 */
public abstract class EquationNode {
	/**
	 * Takes a string and parses it. Most of this could be handle with another library
	 * or using a scripting language such as JavaScript. But I wanted to do it this way
	 * because I can. So I did. Deal with it.
	 * 
	 * @param equationString
	 *            equation to parse
	 * @return EquationNode that can be solved and janx
	 * @throws MathErrorException
	 *             if any error occurs while parsing, this throws an error
	 */
	public static EquationNode ParseEquationString(String equationString)
			throws MathErrorException {
		// Remove all white space from the equation, because... screw that
		// noise.
		equationString = equationString.replaceAll("\\s", "");
		equationString = equationString.replaceAll(
				"(?<=[^\\*\\/\\+\\-\\(\\^])\\(", "*(");
		equationString = equationString.replaceAll(
				"\\)(?=[^\\*\\/\\+\\-\\)\\^])", ")*");
		System.out.println("Parsing the value \"" + equationString + "\".");

		// First step to is to find and handle parenthesiseseseseseseseseses
		Queue<String> parenthesesValues = new LinkedList<String>();
		while (equationString.indexOf('(') != -1) {
			int leftParenthesis = equationString.indexOf('(');
			int rightParenthesis = -1;
			int parenthesisLevel = 1;

			int i = leftParenthesis + 1;
			while (rightParenthesis == -1 && i < equationString.length()) {
				switch (equationString.charAt(i)) {
				case '(':
					parenthesisLevel++;
					break;
				case ')':
					parenthesisLevel--;
					if (parenthesisLevel == 0) {
						rightParenthesis = i;
					}
					break;
				default:
					break;
				}

				i++; // Increment
			}

			if (rightParenthesis == -1)
				throw new MathErrorException("Unclosed parenthesis detected.");

			String parenthesisString = equationString.substring(
					leftParenthesis + 1, rightParenthesis);
			parenthesesValues.add(parenthesisString);
			equationString = equationString.substring(0, leftParenthesis) + "$"
					+ equationString.substring(rightParenthesis + 1);
		}
		if (equationString.equals("$")) {
			return ParseEquationString(parenthesesValues.remove());
		}
		if (equationString.equals("-$")) {
			LinearOperationChainNode toReturn = new LinearOperationChainNode(
					new RawValueNode(-1));
			toReturn.AddOperation(OperationType.MULTIPLY,
					ParseEquationString(parenthesesValues.remove()));
			return toReturn;
		}

		// This is where things get wierd. Since the tree will be solved bottom
		// up, addition/subtraction should be at the top of the tree in order to
		// done last.
		// Yeah.
		if (equationString.indexOf('+') != -1
				|| equationString.lastIndexOf('-') > 0) {
			String[] additionParts = equationString
					.split("(?<=[^\\+\\-\\*\\/])[\\+\\-]");
			String[] operators = equationString.split("[^\\+\\-]+");

			for (int i = 0; i < additionParts.length; i++) {
				while (additionParts[i].indexOf('$') > -1) {
					additionParts[i] = additionParts[i].replaceFirst("\\$",
							parenthesesValues.remove());
				}
			}

			LinearOperationChainNode toReturn = new LinearOperationChainNode(
					ParseEquationString(additionParts[0]));
			for (int i = 1; i < additionParts.length; i++) {
				EquationNode en = ParseEquationString(additionParts[i]);
				if (operators[i].charAt(0) == '+')
					toReturn.AddOperation(OperationType.ADD, en);
				if (operators[i].charAt(0) == '-')
					toReturn.AddOperation(OperationType.SUBTRACT, en);
			}

			return toReturn;
		}

		// Same as addition and subtraction BUT for multiplication and division.
		// Probably should use a common method or something. Y'know.
		if (equationString.indexOf('*') != -1
				|| equationString.indexOf('/') > -1) {
			String[] additionParts = equationString
					.split("(?<=[^\\+\\-\\*\\/])[\\*\\/]");
			String[] operators = equationString.split("[^\\*\\/]+");

			for (int i = 0; i < additionParts.length; i++) {
				while (additionParts[i].indexOf('$') > -1) {
					additionParts[i] = additionParts[i].replaceFirst("\\$",
							parenthesesValues.remove());
				}
			}

			LinearOperationChainNode toReturn = new LinearOperationChainNode(
					ParseEquationString(additionParts[0]));
			for (int i = 1; i < additionParts.length; i++) {
				EquationNode en = ParseEquationString(additionParts[i]);
				if (operators[i].charAt(0) == '*')
					toReturn.AddOperation(OperationType.MULTIPLY, en);
				if (operators[i].charAt(0) == '/')
					toReturn.AddOperation(OperationType.DIVIDE, en);
			}

			return toReturn;
		}

		// Check for exponents!
		if (equationString.indexOf('^') != -1) {
			String left = equationString.substring(0,
					equationString.indexOf('^'));
			String right = equationString
					.substring(equationString.indexOf('^') + 1);
			while (left.indexOf('$') > -1) {
				left = left.replaceFirst("\\$", parenthesesValues.remove());
			}
			while (right.indexOf('$') > -1) {
				right = right.replaceFirst("\\$", parenthesesValues.remove());
			}
			LinearOperationChainNode toReturn = new LinearOperationChainNode(
					ParseEquationString(left));
			toReturn.AddOperation(OperationType.POWEROF,
					ParseEquationString(right));
			return toReturn;
		}

		return new RawValueNode(Float.parseFloat(equationString));
	}

	/**
	 * You done fucked up.
	 * 
	 * @author Grogian
	 */
	public static class MathErrorException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6706670100887374730L;

		public MathErrorException(String message) {
			super(message);
		}
	}

	abstract public float Solve();
}
