package com.liljeson.mattias.interpretator.backend;
import java.util.ArrayList;


public class Programs {

	public static ArrayList<IcLine> prog1() {
		ArrayList<IcLine> program = new ArrayList<IcLine>();
		program.add( new IcLine( Instructions.PUSH, 4 ) );
		program.add( new IcLine( Instructions.PUSH, 5 ) );
		program.add( new IcLine( Instructions.ADD, -1 ) );
		program.add( new IcLine( Instructions.PUSH, -9 ) );
		program.add( new IcLine( Instructions.ADD, -1 ) );
		program.add( new IcLine( Instructions.IFEQ, 4 ) );
		program.add( new IcLine( Instructions.PRINT, -1 ) );
		program.add( new IcLine( Instructions.DUP, -1 ) );
		program.add( new IcLine( Instructions.POP, -1 ) );
		program.add( new IcLine( Instructions.PUSH, 42 ) );
		program.add( new IcLine( Instructions.JUMP, 13 ) );
		program.add( new IcLine( Instructions.PUSH, 1337 ) );
		program.add( new IcLine( Instructions.POP, -1 ) );
		program.add( new IcLine( Instructions.PRINT, -1 ) );
		return program;
	}
}
