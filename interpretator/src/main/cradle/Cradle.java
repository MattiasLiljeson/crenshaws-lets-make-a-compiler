package cradle;

import cradle.LogLady.LogLevels;

public class Cradle {
	/**
	 * @param args
	 */
	public static void main( final String[] args ) {
		// TODO Auto-generated method stub
		final Cradle cradle=new Cradle();
		cradle.init();
	}

	LogLady m_log=new LogLady();

	char m_look;

	void abort( final String p_msg ) {
		m_log.Log( LogLevels.SEVERE, p_msg );
		// Todo: quit program
	}

	void error( final String p_msg ) {
		m_log.Log( LogLevels.ERROR, p_msg );
	}

	void expected( final String p_msg ) {
		abort( p_msg + " Expected" );
	}

	void getChar() {
		m_look=m_log.read();
	}

	boolean isAlpha( final char p_char ) {
		return Character.isLetter( p_char );
	}

	void match( final char p_char ) {
		if( m_look == p_char ) {
			getChar();
		} else {
			expected( "\"" + p_char + "\"" );
		}
	}

	boolean isDigit( final char p_char ) {
		return Character.isDigit( p_char );
	}

	char getName() {
		char name='_';
		if( !isAlpha( m_look ) ) {
			expected( "Name" );
		} else {
			name=Character.toUpperCase( m_look );
			getChar();
		}
		return name;
	}

	char getNum() {
		char name='_';
		if( !isDigit( m_look ) ) {
			expected( "Integer" );
		} else {
			name=m_look;
			getChar();
		}
		return name;
	}

	void emit( final String p_msg ) {
		m_log.write( "\t" + p_msg );
	}

	void emitLn( final String p_msg ) {
		emit( p_msg );
		m_log.writeLn();
	}

	void init() {
		getChar();
	}

	// {--------------------------------------------------------------}
	// { Main Program }
	//
	// begin
	// Init;
	// end.
	// {--------------------------------------------------------------}
}