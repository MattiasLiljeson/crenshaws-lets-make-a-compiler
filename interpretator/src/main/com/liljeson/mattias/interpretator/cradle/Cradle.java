package com.liljeson.mattias.interpretator.cradle;

import com.liljeson.mattias.interpretator.cradle.LogLady.LogLevels;

public class Cradle {

	LogLady m_log = new LogLady(false, LogLevels.ERROR);
	CompLogger m_compLog = new CompLogger(m_log);
	Tokenizer m_tokenizer = new Tokenizer("1+2");
	char m_look = '_';
	boolean m_abort = false;
	int labelCnt = 1;

	public Cradle(final LogLady p_log, final CompLogger p_compLog) {
		if (p_compLog != null) {
			m_compLog = p_compLog;
		}
		if (p_log != null) {
			m_log = p_log;
		}
	}

	public void run(final String p_program) throws Exception {
		m_tokenizer = new Tokenizer(p_program);
		init();
		// assignment();
		// other();
		doProgram();

		// if (m_look != '\n' && m_look != '_') {
		// expected("Newline, not: " + m_look);
		// }
	}

	void abort(final String p_msg) throws Exception {
		m_log.log(LogLevels.SEVERE, p_msg);
		m_abort = true;
		throw new Exception();
	}

	void error(final String p_msg) {
		m_log.log(LogLevels.ERROR, p_msg);
		m_abort = true;
		try {
			throw new Exception();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	void expected(final String p_msg) throws Exception {
		abort(p_msg + " Expected");
	}

	void getChar() {
		m_look = read();
		m_log.log(LogLevels.NONE,
				"; next token read to look: " + Character.toString(m_look));
	}

	void match(final char p_char) throws Exception {
		if (m_look == p_char) {
			getChar();
		} else {
			expected("\"" + p_char + "\"");
		}
	}

	boolean isAddop(final char p_char) {
		return p_char == '+' || p_char == '-';
	}

	boolean isAlpha(final char p_char) {
		return Character.isLetter(p_char);
	}

	boolean isDigit(final char p_char) {
		return Character.isDigit(p_char);
	}

	char getName() throws Exception {
		// ? returned if look isn't a digit
		char name = '?';
		if (!isAlpha(m_look)) {
			expected("Name");
		} else {
			name = Character.toUpperCase(m_look);
			getChar();
		}
		return name;
	}

	char getNum() throws Exception {
		// ¤ returned if look isn't a digit
		char num = '¤';
		if (!isDigit(m_look)) {
			expected("Integer");
		} else {
			num = m_look;
			getChar();
		}
		return num;
	}

	void emit(final String p_msg) {
		m_log.write("\t" + p_msg);
	}

	void emitLn(final char p_msg) {

		emitLn(String.valueOf(p_msg), CompLogger.getCallerName(3));
	}

	void emitLn(final String p_msg) {

		emitLn(p_msg, CompLogger.getCallerName(3));
	}

	void emitLn(final String p_msg, final String p_caller) {
		emit(p_msg + "\t\t; " + p_caller);
		m_log.writeLn();
	}

	void init() {
		getChar();
	}

	char read() {
		return m_tokenizer.getNextToken().m_token;
	}

	void term() throws Exception {
		m_compLog.push();
		factor();
		while (m_look == '*' || m_look == '/') {
			emitLn("MOVE D0, -(SP)");
			switch (m_look) {
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

	void assignment() throws Exception {
		m_compLog.push();
		final char name = getName();
		match('=');
		expression();
		emitLn("LEA  " + name + "(PC),A0");
		emitLn("MOVE D0,(A0)");
		m_compLog.pop();
	}

	void add() throws Exception {
		m_compLog.push();
		match('+');
		term();
		emitLn("ADD  (SP)+, D0");
		m_compLog.pop();
	}

	void subtract() throws Exception {
		m_compLog.push();
		match('-');
		term();
		emitLn("SUB  (SP)+, D0");
		emitLn("NEG  D0");
		m_compLog.pop();
	}

	void factor() throws Exception {
		m_compLog.push();
		if (m_look == '(') {
			match('(');
			expression();
			match(')');
		} else if (isAlpha(m_look)) {
			ident();
		} else {
			emitLn("MOVE #" + getNum() + ", D0");
		}
		m_compLog.pop();
	}

	void ident() throws Exception {
		m_compLog.push();
		final char name = getName();
		if (m_look == '(') {
			match('(');
			match(')');
			emitLn("BSR " + name);
		} else {
			emitLn("MOVE " + name + "(PC), D0");
		}
		m_compLog.pop();
	}

	void multiply() throws Exception {
		m_compLog.push();
		match('*');
		factor();
		emitLn("MULS (SP)+, D0");
		m_compLog.pop();
	}

	void divide() throws Exception {
		m_compLog.push();
		match('/');
		factor();
		emitLn("MOVE (SP)+, D1");
		emitLn("DIVS D1, D0");
		m_compLog.pop();
	}

	void other() throws Exception {
		emitLn(getName());
	}

	void doProgram() throws Exception {
		block("");
		if (m_look != 'e') {
			expected("end");
		}
		emitLn("END");
	}

	void block(final String l) throws Exception {
		m_compLog.push();
		while (contBlock(m_look)) {
			switch (m_look) {
			case 'i':
				doIf(l);
				break;
			case 'w':
				doWhile();
				break;
			case 'p':
				doLoop();
				break;
			case 'r':
				doRepeat();
				break;
			case 'f':
				doFor();
				break;
			case 'b':
				doBreak(l);
				break;
			case 'o':
				other();
				break;
			default:
				other();
			}
		}
		m_compLog.pop();
	}

	// own helper func. Bug in orig impl?
	boolean contBlock(final char c) {
		boolean cont = true;
		if (c == 'e' || c == 'E' || c == 'l' || c == 'L' || c == 'u'
				|| c == 'U') {
			cont = false;
		}
		return cont;
	}

	void condition() {
		m_compLog.push();
		emitLn("<condition>");
		m_compLog.pop();
	}

	String newLabel() {
		return "L" + labelCnt++;
	}

	void postLabel(final String p_label) {
		emitLn(p_label + " :");
	}

	void doIf(final String l) throws Exception {
		m_compLog.push();
		match('i');
		condition();
		final String l1 = newLabel();
		String l2 = l1;
		emitLn("BEQ " + l1);
		block(l);
		if (m_look == 'l') {
			match('l');
			l2 = newLabel();
			emitLn("BRA " + l2);
			postLabel(l1);
			block(l);
		}
		match('e');
		postLabel(l2);
		m_compLog.pop();
	}

	void doWhile() throws Exception {
		m_compLog.push();
		match('w');
		final String l1 = newLabel();
		final String l2 = newLabel();
		postLabel(l1);
		condition();
		emitLn("BEQ " + l2);
		block(l2);
		match('e');
		emitLn("BRA " + l1);
		postLabel(l2);
		m_compLog.pop();
	}

	void doLoop() throws Exception {
		m_compLog.push();
		match('p');
		final String l1 = newLabel();
		final String l2 = newLabel();
		postLabel(l1);
		block(l2);
		match('e');
		emitLn("BRA " + l1);
		postLabel(l2);
		m_compLog.pop();
	}

	void doRepeat() throws Exception {
		m_compLog.push();
		match('r');
		final String l = newLabel();
		final String l2 = newLabel();
		block(l2);
		match('u');
		condition();
		emitLn("BEQ " + l);
		postLabel(l2);
		m_compLog.pop();
	}

	void doFor() throws Exception {
		m_compLog.push();
		match('f');
		final String l1 = newLabel();
		final String l2 = newLabel();
		final char name = getName();
		match('=');
		expression();
		emitLn("SUBQ #1,D0");
		emitLn("LEA " + name + "(PC),A0");
		emitLn("MOVE D0,(A0)");
		expression();
		emitLn("MOVE D0,-(SP)");
		postLabel(l1);
		emitLn("LEA " + name + "(PC),A0");
		emitLn("MOVE (A0),D0");
		emitLn("ADDQ #1,D0");
		emitLn("MOVE D0,(A0)");
		emitLn("CMP (SP),D0");
		emitLn("BGT " + l2);
		block(l2);
		match('e');
		emitLn("BRA " + l1);
		postLabel(l2);
		emitLn("ADDQ #2,SP");
		m_compLog.pop();
	}

	void expression() throws Exception {
		m_compLog.push();
		emitLn("<expr>");
		m_compLog.pop();
	}

	void doDo() throws Exception {
		match('d');
		final String l = newLabel();
		final String l2 = newLabel();
		expression();
		emitLn("SUBQ #1,D0");
		postLabel(l);
		emitLn("MOVE D0,-(SP)");
		block(l2);
		emitLn("MOVE (SP)+,D0");
		emitLn("DBRA D0," + l);
		emitLn("SUBQ #2,SP");
		postLabel(l2);
		emitLn("ADDQ #2,SP");
	}

	void doBreak(final String l) throws Exception {
		match('b');
		if (l != "") {
			emitLn("BRA " + l);
		} else {
			abort("No loop to break from");
		}
	}
}