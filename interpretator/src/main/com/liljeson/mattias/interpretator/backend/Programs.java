package com.liljeson.mattias.interpretator.backend;

import java.util.ArrayList;

public class Programs {

	final static int NO_VAL = -1;

	public static ArrayList<IcLine> prog1() {
		final ArrayList<IcLine> program = new ArrayList<IcLine>();
		program.add(new IcLine(Instructions.PUSH, 4));
		program.add(new IcLine(Instructions.PUSH, 5));
		program.add(new IcLine(Instructions.ADD, NO_VAL));
		program.add(new IcLine(Instructions.PUSH, -9));
		program.add(new IcLine(Instructions.ADD, NO_VAL));
		program.add(new IcLine(Instructions.IFEQ, 4));
		program.add(new IcLine(Instructions.PRINT, NO_VAL));
		program.add(new IcLine(Instructions.DUP, NO_VAL));
		program.add(new IcLine(Instructions.POP, NO_VAL));
		program.add(new IcLine(Instructions.PUSH, 42));
		program.add(new IcLine(Instructions.JUMP, 13));
		program.add(new IcLine(Instructions.PUSH, 1337));
		program.add(new IcLine(Instructions.POP, NO_VAL));
		program.add(new IcLine(Instructions.PRINT, NO_VAL));
		return program;
	}
}
