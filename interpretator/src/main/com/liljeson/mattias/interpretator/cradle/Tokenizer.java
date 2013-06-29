package com.liljeson.mattias.interpretator.cradle;

import java.util.List;
import java.util.ArrayList;

public class Tokenizer {
	List<Token> m_tokens;
	int m_pc = 0;
	
	public Tokenizer(String p_program){
		m_tokens = new ArrayList<Token>();
		for (int i = 0; i < p_program.length(); i++) {
			m_tokens.add(new Token(p_program.charAt(i)));
		}
	}
	
	public Token getNextToken(){
		if(m_pc < m_tokens.size()){
			return m_tokens.get(m_pc++);			
		} else {
			return new Token('_');
		}
	}
}
