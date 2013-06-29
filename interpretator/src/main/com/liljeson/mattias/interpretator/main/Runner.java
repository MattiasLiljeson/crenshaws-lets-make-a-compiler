package com.liljeson.mattias.interpretator.main;

import com.liljeson.mattias.interpretator.cradle.Cradle;

/**
 * Implementation of the stack based VM described here:
 * http://stackoverflow.com/
 * questions/6887471/how-would-i-go-about-writing-an-interpreter-in-c
 * 
 * A program to test all instructions is created by calling Programs.prog1();
 * 
 * @author Mattias Liljeson
 * 
 */

public class Runner {
	/**
	 * @param args
	 */
	public static void main( final String[] args ) {
		System.out.println( "Hello! Let's he'go!" );
		// Backend backend = new Backend();
		// backend.loadProgram( Programs.prog1() );
		// backend.runProgram();

		final Cradle cradle = new Cradle();
		cradle.run( "-3*2/(1+4)-3*1" );

	}
}
