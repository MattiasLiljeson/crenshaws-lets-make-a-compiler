package com.liljeson.mattias.interpretator.main;

import com.liljeson.mattias.interpretator.cradle.CompLogger;
import com.liljeson.mattias.interpretator.cradle.Cradle;
import com.liljeson.mattias.interpretator.cradle.LogLady;
import com.liljeson.mattias.interpretator.cradle.LogLady.LogLevels;

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
	 * @throws Exception
	 */
	public static void main(final String[] args) {
		System.out.println("Hello! Let's he'go!");
		// Backend backend = new Backend();
		// backend.loadProgram( Programs.prog1() );
		// backend.runProgram();
		final LogLady logLady = new LogLady(false, LogLevels.WARNING);
		final CompLogger compLady = new CompLogger(logLady);
		final Cradle cradle = new Cradle(logLady, compLady);

		try {
			cradle.run("afl=kibecdege");
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
