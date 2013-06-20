import static org.junit.Assert.*;

import org.junit.Test;


public class ProgramLineTest {

	@Test
	public void test() {
		ProgramLine line = new ProgramLine( Instructions.JUMP, 42 );
		
		assertEquals("ProgramLineTest: wrong instuction returned",
				Instructions.JUMP, line.getInstr());
		
		assertEquals("ProgramLineTest: wrong value returned",
				42, line.getVal());
	}

}
