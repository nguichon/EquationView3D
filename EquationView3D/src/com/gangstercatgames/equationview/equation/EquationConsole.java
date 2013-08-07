package com.gangstercatgames.equationview.equation;

import java.util.Scanner;

import com.gangstercatgames.equationview.equation.node.EquationNode;

public class EquationConsole {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner( System.in );
		String input = "q";
		
		do{
			System.out.print('>');
			input = scanner.nextLine();
			
			if( !input.equals("q") ) {
				try{
					System.out.println("Solution: " + EquationNode.ParseEquationString( input ).Solve() );
				} catch( Exception e ) {
					System.out.println( "Error occured: " + e.getMessage() );
					e.printStackTrace();
				}
			}
		} while ( !input.equals("q") );
		
	}

}
