package com.liljeson.mattias.interpretator.backend;

/**
 * Intermidiate code line. A set of an Ic instruction and values. 
 * @author Mattias Liljeson
 *
 */

public class IcLine {
	public Instructions m_instr;
	public int m_val;
	
	public IcLine( Instructions p_instr, int p_val ) {
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