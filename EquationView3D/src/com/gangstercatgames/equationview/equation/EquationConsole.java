package com.gangstercatgames.equationview.equation;

import java.util.Hashtable;
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
					Equation e =  new Equation( input );
					Hashtable<String, Float> numbers = new Hashtable<String, Float>();
					for( int i = 0; i < 5; i++ ) {
						numbers.put("x",(float) i);
						for( int j = 0; j < 5; j++ ) {
							numbers.put("y",(float) j);
							System.out.println( "(" + i + "," + j + "," + e.SolveEquationUsing( numbers ) + ")" );
						}
					}
				} catch( Exception e ) {
					System.out.println( "Error occured: " + e.getMessage() );
					e.printStackTrace();
				}
			}
		} while ( !input.equals("q") );
		
	}

}
