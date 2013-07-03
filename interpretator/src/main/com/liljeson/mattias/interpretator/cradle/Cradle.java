package com.liljeson.mattias.interpretator.cradle;

import com.liljeson.mattias.interpretator.cradle.LogLady.LogLevels;

public class Cradle {

	LogLady m_log = new LogLady( false, LogLevels.ERROR );
	CompLogger m_compLog = new CompLogger( m_log );
	Tokenizer m_tokenizer = new Tokenizer( "1+2" );
	char m_look = '_';
	boolean m_abort = false;

	public Cradle( final LogLady p_log, final CompLogger p_compLog ) {
		if( p_compLog != null ) {
			m_compLog = p_compLog;
		}
		if( p_log != null ) {
			m_log = p_log;
		}
	}

	public void run( final String p_program ) {
		m_tokenizer = new Tokenizer( p_program );
		init();
		assignment();
		if( m_look != '\n' && m_look != '_' ) {
			expected( "Newline, not: " + m_look );
		}
	}

	void abort( final String p_msg ) {
		m_log.log( LogLevels.SEVERE, p_msg );
		m_abort = true;
		try {
			throw new Exception();
		} catch( final Exception e ) {
			e.printStackTrace();
		}
	}

	void error( final String p_msg ) {
		m_log.log( LogLevels.ERROR, p_msg );
		m_abort = true;
		try {
			throw new Exception();
		} catch( final Exception e ) {
			e.printStackTrace();
		}
	}

	void expected( final String p_msg ) {
		abort( p_msg + " Expected" );
	}

	void getChar() {
		m_look = read();
		m_log.log( LogLevels.NONE,
				"; next token read to look: " + Character.toString( m_look ) );
	}

	void match( final char p_char ) {
		if( m_look != p_char ) {
			expected( "\"" + p_char + "\" not: \"" + m_look + "\"" );
		} else {
			getChar();
			skipWhite();
		}
	}

	boolean isAddop( final char p_char ) {
		return p_char == '+' || p_char == '-';
	}

	boolean isAlpha( final char p_char ) {
		return Character.isLetter( p_char );
	}

	boolean isDigit( final char p_char ) {
		return Character.isDigit( p_char );
	}

	boolean isAlNum( final char p_char ) {
		return isAlpha( p_char ) || isDigit( p_char );
	}

	boolean isWhite( final char p_char ) {
		return p_char == ' ' || p_char == '\t';
	}

	void skipWhite() {
		while( isWhite( m_look ) ) {
			getChar();
		}
	}

	String getName() {
		// ? returned if look isn't a digit
		String token = "";
		if( !isAlpha( m_look ) ) {
			expected( "Name, not: " + m_look );
		}
		while( isAlNum( m_look ) ) {
			token += upCase( m_look );
			getChar();
		}
		skipWhite();
		return token;
	}

	char upCase( final char p_char ) {
		return Character.toUpperCase( p_char );
	}

	String getNum() {
		// ¤ returned if look isn't a digit
		String value = "";
		if( !isDigit( m_look ) ) {
			expected( "Integer, not: " + m_look );
		}
		while( isDigit( m_look ) ) {
			value += m_look;
			getChar();
		}
		skipWhite();
		return value;
	}

	void emit( final String p_msg ) {
		m_log.write( "\t" + p_msg );
	}

	void emitLn( final String p_msg ) {

		emitLn( p_msg, CompLogger.getCallerName( 3 ) );
	}

	void emitLn( final String p_msg, final String p_caller ) {
		emit( p_msg + "\t\t; " + p_caller );
		m_log.writeLn();
	}

	void init() {
		getChar();
		skipWhite();
	}

	char read() {
		return m_tokenizer.getNextToken().m_token;
	}

	void term() {
		m_compLog.push();
		factor();
		while( m_look == '*' || m_look == '/' ) {
			emitLn( "MOVE D0, -(SP)" );
			switch( m_look ) {
			case '*':
				multiply();
				break;
			case '/':
				divide();
				break;
			}
		}
		m_compLog.pop();
	}

	void expression() {
		m_compLog.push();
		if( isAddop( m_look ) ) {
			emitLn( "CLR  D0" );
		} else {
			term();
		}
		while( isAddop( m_look ) ) {
			emitLn( "MOVE D0, -(SP)" );
			switch( m_look ) {
			case '+':
				add();
				break;
			case '-':
				subtract();
				break;
			}
		}
		m_compLog.pop();
	}

	void assignment() {
		m_compLog.push();
		final String name = getName();
		match( '=' );
		expression();
		emitLn( "LEA  " + name + "(PC),A0" );
		emitLn( "MOVE D0,(A0)" );
		m_compLog.pop();
	}

	void add() {
		m_compLog.push();
		match( '+' );
		term();
		emitLn( "ADD  (SP)+, D0" );
		m_compLog.pop();
	}

	void subtract() {
		m_compLog.push();
		match( '-' );
		term();
		emitLn( "SUB  (SP)+, D0" );
		emitLn( "NEG  D0" );
		m_compLog.pop();
	}

	void factor() {
		m_compLog.push();
		if( m_look == '(' ) {
			match( '(' );
			expression();
			match( ')' );
		} else if( isAlpha( m_look ) ) {
			ident();
		} else {
			emitLn( "MOVE #" + getNum() + ", D0" );
		}
		m_compLog.pop();
	}

	void ident() {
		m_compLog.push();
		final String name = getName();
		if( m_look == '(' ) {
			match( '(' );
			match( ')' );
			emitLn( "BSR " + name );
		} else {
			emitLn( "MOVE " + name + "(PC), D0" );
		}
		m_compLog.pop();
	}

	void multiply() {
		m_compLog.push();
		match( '*' );
		factor();
		emitLn( "MULS (SP)+, D0" );
		m_compLog.pop();
	}

	void divide() {
		m_compLog.push();
		match( '/' );
		factor();
		emitLn( "MOVE (SP)+, D1" );
		emitLn( "DIVS D1, D0" );
		m_compLog.pop();
	}
	// {--------------------------------------------------------------}
	// { Main Program }
	//
	// begin
	// Init;
	// end.
	// {--------------------------------------------------------------}
}