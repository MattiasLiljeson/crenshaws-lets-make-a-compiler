package cradle;

import java.util.Date;

public class LogLady {
	public enum LogLevels {
		INFO, WARNING, ERROR, SEVERE
	}

	/**
	 * @param args
	 */
	public static void main( final String[] args ) {
		// TODO Auto-generated method stub
	}

	boolean m_printTimeStamp=true;

	void Log( final LogLevels p_level, final String p_msg ) {
		if( m_printTimeStamp ) {
			System.out.println( new Date().toString() + " "
					+ p_level.toString() + ": " );
		}
		System.out.println( p_msg );
	}

	public void write( final String p_msg ) {
		System.out.print( p_msg );

	}

	public void writeLn() {
		System.out.println( "" );

	}

	public char read() {
		return '_';
	}
}
