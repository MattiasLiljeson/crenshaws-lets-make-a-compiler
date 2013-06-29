package com.liljeson.mattias.interpretator.cradle;

import java.util.Date;

public class LogLady {
	public enum LogLevels {
		NONE, INFO, WARNING, ERROR, SEVERE
	}

	boolean m_printTimeStamp = true;

	public LogLady() {
		m_printTimeStamp = true;
		;
	}

	public LogLady( final boolean p_printTimeStamp ) {
		m_printTimeStamp = p_printTimeStamp;
	}

	void log( final LogLevels p_level, final String p_msg ) {
		if( m_printTimeStamp ) {
			System.out.println( new Date().toString() + ", "
					+ p_level.toString() + ": " );
		}
		// System.out.println( p_msg );
	}

	public void write( final String p_msg ) {
		System.out.print( p_msg );

	}

	public void writeLn() {
		System.out.println( "" );

	}

	// public char read() {
	// return '_';
	// }
}
