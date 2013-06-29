package com.liljeson.mattias.interpretator.cradle;

import com.liljeson.mattias.interpretator.cradle.LogLady.LogLevels;

public class Cradle {
	/**
	 * @param args
	 */
	// public static void main( final String[] args ) {
	// // TODO Auto-generated method stub
	// final Cradle cradle=new Cradle();
	// cradle.init();
	// cradle.expression();
	// }

	LogLady m_log = new LogLady( false );
	CompLogger m_compLog = new CompLogger();
	Tokenizer m_tokenizer = new Tokenizer( "1+2" );
	char m_look = '_';
	boolean m_abort = false;

	public void run( final String p_program ) {
		m_tokenizer = new Tokenizer( p_program );
		init();
		expression();
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
		m_log.log( LogLevels.NONE, "; read: " + Character.toString( m_look ) );
	}

	void match( final char p_char ) {
		if( m_look == p_char ) {
			getChar();
		} else {
			expected( "\"" + p_char + "\"" );
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
		// ? returned if look isn't a digit
		char name = '?';
		if( !isAlpha( m_look ) ) {
			expected( "Name" );
		} else {
			name = Character.toUpperCase( m_look );
			getChar();
		}
		return name;
	}

	char getNum() {
		// ¤ returned if look isn't a digit
		char num = '¤';
		if( !isDigit( m_look ) ) {
			expected( "Integer" );
		} else {
			num = m_look;
			getChar();
		}
		return num;
	}

	void emit( final String p_msg ) {
		m_log.write( "\t" + p_msg );
	}

	void emitLn( final String p_msg, final String p_caller ) {
		emit( p_msg + "\t\t; " + p_caller );
		m_log.writeLn();
	}

	void init() {
		getChar();
	}

	char read() {
		return m_tokenizer.getNextToken().m_token;
	}

	void term() {
		m_compLog.push( "term()" );
		factor();
		while( m_look == '*' || m_look == '/' ) {
			emitLn( "MOVE D0, -(SP)", "term()" );
			switch( m_look ) {
			case '*':
				multiply();
				break;
			case '/':
				divide();
				break;
			default:
				expected( "Mulop" );
			}
		}
		m_compLog.pop();
	}

	void expression() {
		m_compLog.push( "expression()" );
		if( isAddop( m_look ) ) {
			emitLn( "CLR  D0", "expression()" );
		} else {
			term();
		}
		while( isAddop( m_look ) ) {
			emitLn( "MOVE D0, -(SP)", "expression()" );
			switch( m_look ) {
			case '+':
				add();
				break;
			case '-':
				subtract();
				break;
			default:
				expected( "Addop" );
			}
		}
		m_compLog.pop();
	}

	void add() {
		m_compLog.push( "add()" );
		match( '+' );
		term();
		emitLn( "ADD  (SP)+, D0", "add()" );
		m_compLog.pop();
	}

	void subtract() {
		m_compLog.push( "subtract()" );
		match( '-' );
		term();
		emitLn( "SUB  (SP)+, D0", "subtract()" );
		emitLn( "NEG  D0", "subtract()" );
		m_compLog.pop();
	}

	void factor() {
		m_compLog.push( "factor()" );
		if( m_look == '(' ) {
			match( '(' );
			expression();
			match( ')' );
		} else {
			emitLn( "MOVE #" + getNum() + ", D0", "factor()" );
		}
		m_compLog.pop();
	}

	void multiply() {
		m_compLog.push( "multiply()" );
		match( '*' );
		factor();
		emitLn( "MULS (SP)+, D0", "multiply()" );
		m_compLog.pop();
	}

	void divide() {
		m_compLog.push( "divide()" );
		match( '/' );
		factor();
		emitLn( "MOVE (SP)+, D1", "divide()" );
		emitLn( "DIVS D1, D0", "divide()" );
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