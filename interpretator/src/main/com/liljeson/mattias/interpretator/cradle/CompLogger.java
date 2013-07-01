package com.liljeson.mattias.interpretator.cradle;

import java.util.Stack;

import com.liljeson.mattias.interpretator.cradle.LogLady.LogLevels;

public class CompLogger {

	Stack< String > m_callStack = new Stack<>();
	LogLady m_log = new LogLady( false, LogLevels.ERROR );

	public CompLogger( final LogLady p_lady ) {
		if( p_lady != null ) {
			m_log = p_lady;
		}
	}

	public void push() {
		push( getCallerName( 3 ) );
	}

	public void push( final String p_funcName ) {
		m_callStack.push( p_funcName );
		printTop( "> " );
	}

	public void pop() {
		printTop( "< " );
		m_callStack.pop();
	}

	void printTop( final String p_sym ) {
		final String out = ";" + stackDepth() + p_sym + m_callStack.peek();
		m_log.log( LogLevels.INFO, out );
	}

	static String getCallerName( final int p_depth ) {
		final String raw = Thread.currentThread().getStackTrace()[p_depth]
				.toString();
		final int startOfFunctionName = raw.lastIndexOf( ".Cradle" );
		final String niceified = raw.substring( startOfFunctionName + 7,
				raw.length() );
		return niceified;
	}

	String stackDepth() {
		String depthMarker = "";
		for( int i = 0; i < m_callStack.size(); i++ ) {
			depthMarker += "-";
		}
		return depthMarker;
	}
}
