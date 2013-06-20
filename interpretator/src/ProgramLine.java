enum Instructions {
	PUSH,
	POP,
	ADD,
	IFEQ,
	JUMP,
	PRINT,
	DUP,
}

public class ProgramLine {
	public Instructions m_instr;
	public int m_val;
	
	ProgramLine( Instructions p_instr, int p_val ) {
		m_instr = p_instr;
		m_val = p_val;
	}
	
	public int getVal() {
		return m_val;
	}
	
	public Instructions getInstr() {
		return m_instr;
	}
}