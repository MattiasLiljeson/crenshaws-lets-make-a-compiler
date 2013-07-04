package com.liljeson.mattias.interpretator.cradle;

import com.liljeson.mattias.interpretator.cradle.LogLady.LogLevels;

public class Cradle {

	LogLady m_log = new LogLady( false, LogLevels.ERROR );
	CompLogger m_compLog = new CompLogger( m_log );
	Tokenizer m_tokenizer = new Tokenizer( "1+2" );
	char m_look = '_';
	int[] table = new int['z'];
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

		do {
			// m_log.write( new Integer( expression() ).toString() );
			switch( m_look ) {
			case '?':
				input();
				break;
			case '!':
				output();
				break;
			default:
				assignment();
				break;
			}
			newLine();
		} while( m_look != '.' );
	}

	void init() {
		getChar();
		initTable();
	}

	void initTable() {
		for( int i = 0; i < table.length; i++ ) {
			table[i] = 0;
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

	void input() {
		m_compLog.push();
		match( '?' );
		table[getName()] = m_log.read();
		m_compLog.pop();
	}

	void output() {
		m_compLog.push();
		match( '!' );
		m_log.WriteLn( table[getName()] );
		m_compLog.pop();
	}

	void match( final char p_char ) {
		if( m_look == p_char ) {
			getChar();
		} else {
			expected( "\"" + p_char + "\"" );
		}
	}

	void newLine() {
		if( m_look == '\n' ) {
			getChar();
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

	char getName() {
		// % returned if look isn't a digit
		char name = '%';
		if( !isAlpha( m_look ) ) {
			expected( "Name" );
		} else {
			name = Character.toUpperCase( m_look );
			getChar();
		}
		return name;
	}

	int getNum() {
		int num = 0;
		if( !isDigit( m_look ) ) {
			expected( "Integer" );
		}
		while( isDigit( m_look ) ) {
			num = num * 10 + Integer.parseInt( ( String.valueOf( m_look ) ) );
			getChar();
		}
		return num;
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

	char read() {
		return m_tokenizer.getNextToken().m_token;
	}

	int term() {
		m_compLog.push();
		int value = factor();
		while( m_look == '*' || m_look == '/' ) {
			switch( m_look ) {
			case '*':
				match( '*' );
				value *= factor();
				break;
			case '/':
				match( '/' );
				value /= factor();
				break;
			}
		}
		m_compLog.pop();
		return value;
	}

	int expression() {
		m_compLog.push();
		int value;
		if( isAddop( m_look ) ) {
			value = 0;
		} else {
			value = term();
		}
		while( isAddop( m_look ) ) {
			switch( m_look ) {
			case '+':
				match( '+' );
				value += term();
				break;
			case '-':
				match( '-' );
				value -= term();
				break;
			}
		}
		m_compLog.pop();
		return value;
	}

	void assignment() {
		m_compLog.push();
		final char name = getName();
		match( '=' );
		table[name] = expression();
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

	int factor() {
		int value;
		m_compLog.push();
		if( m_look == '(' ) {
			match( '(' );
			value = expression();
			match( ')' );
		} else if( isAlpha( m_look ) ) {
			value = table[getName()]; // ident();
		} else {
			value = getNum();
		}
		m_compLog.pop();
		return value;
	}

	void ident() {
		m_compLog.push();
		final char name = getName();
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