package com.liljeson.mattias.interpretator.backendtest;
import static org.junit.Assert.*;

import com.liljeson.mattias.interpretator.backend.*;

import org.junit.Test;


public class ProgramLineTest {

	@Test
	public void test() {
		IcLine line = new IcLine( Instructions.JUMP, 42 );
		
		assertEquals("ProgramLineTest: wrong instuction returned",
				Instructions.JUMP, line.getInstr());
		
		assertEquals("ProgramLineTest: wrong value returned",
				42, line.getVal());
	}

}
