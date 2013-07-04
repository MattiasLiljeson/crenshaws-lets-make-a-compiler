package com.liljeson.mattias.interpretator.cradle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class LogLady {
	public enum LogLevels {
		NONE, INFO, WARNING, ERROR, SEVERE
	}

	LogLevels m_printLevel = LogLevels.ERROR;
	boolean m_printTimeStamp = true;

	public LogLady() {
		m_printTimeStamp = true;
	}

	public LogLady( final boolean p_printTimeStamp, final LogLevels p_printLevel ) {
		m_printTimeStamp = p_printTimeStamp;
		m_printLevel = p_printLevel;
	}

	void log( final LogLevels p_level, final String p_msg ) {

		if( p_level.compareTo( m_printLevel ) >= 0 ) {

			if( m_printTimeStamp ) {
				System.out.println( new Date().toString() + ", "
						+ p_level.toString() + ": " );
			}
			System.out.println( p_msg );
		}
	}

	public void write( final String p_msg ) {
		System.out.print( p_msg );

	}

	public void writeLn() {
		System.out.println( "" );
	}

	public void WriteLn( final int p_i ) {
		System.out.println( Integer.toString( p_i ) );

	}

	public int read() {
		int input = -1;
		try {
			final BufferedReader bufferRead = new BufferedReader(
					new InputStreamReader( System.in ) );
			input = Integer
					.parseInt( String.valueOf( (char) bufferRead.read() ) );
		} catch( final IOException e ) {
			e.printStackTrace();
		}
		return input;
	}

	// public char read() {
	// return '_';
	// }
}
