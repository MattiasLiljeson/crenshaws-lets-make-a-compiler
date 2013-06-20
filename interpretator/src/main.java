/**
 * Implementation of the stack based VM described here:
 * http://stackoverflow.com/questions/6887471/how-would-i-go-about-writing-an-interpreter-in-c
 * 
 * A program to test all instructions is created by calling Programs.prog1();
 * @author Mattias
 *
 */

public class main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello! Let's he'go!");
		Assembler stack = new Assembler();
		
		stack.loadProgram( Programs.prog1() );
		stack.runProgram();
		
	}
}
