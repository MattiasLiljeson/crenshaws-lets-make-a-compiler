import java.util.ArrayList;


public class Programs {

	public static ArrayList<ProgramLine> prog1() {
		ArrayList<ProgramLine> program = new ArrayList<ProgramLine>();
		program.add( new ProgramLine( Instructions.PUSH, 4 ) );
		program.add( new ProgramLine( Instructions.PUSH, 5 ) );
		program.add( new ProgramLine( Instructions.ADD, -1 ) );
		program.add( new ProgramLine( Instructions.PUSH, -9 ) );
		program.add( new ProgramLine( Instructions.ADD, -1 ) );
		program.add( new ProgramLine( Instructions.IFEQ, 4 ) );
		program.add( new ProgramLine( Instructions.PRINT, -1 ) );
		program.add( new ProgramLine( Instructions.DUP, -1 ) );
		program.add( new ProgramLine( Instructions.POP, -1 ) );
		program.add( new ProgramLine( Instructions.PUSH, 42 ) );
		program.add( new ProgramLine( Instructions.JUMP, 13 ) );
		program.add( new ProgramLine( Instructions.PUSH, 1337 ) );
		program.add( new ProgramLine( Instructions.POP, -1 ) );
		program.add( new ProgramLine( Instructions.PRINT, -1 ) );
		return program;
	}
}
