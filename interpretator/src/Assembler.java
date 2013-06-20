import java.util.ArrayList;


public class Assembler {
	
	public Assembler() {
		m_stack = new ArrayList<Integer>();
		m_program = new ArrayList<ProgramLine>();
		m_pc = 0;
		
	}
	
	public void push( int p_variable ) {
		m_stack.add( p_variable );
	}
	
	public Integer pop() {	
		Integer tmp = new Integer( m_stack.get( m_stack.size()-1 ) );
		m_stack.remove( m_stack.size()-1 );
		return tmp;
	}
	
	public Integer top() {
		Integer tmp = new Integer( m_stack.get( m_stack.size()-1 ) );
		return tmp;
	}
	
	public void mult() {
		Integer product = pop();
		product *= pop();
		push( product );
	}
	
	public void add() {
		Integer sum = pop();
		sum += pop();
		push( sum );
	}
	
	public void ifeq( int p_adress ) {
		if( top() != 0 ) {
			setPc( p_adress );
		}
	}
	
	public void jump( int p_adress ) {
		setPc( p_adress );
	}
	
	public void print() {
		System.out.println( top() );
	}
	
	public void dup() {
		push( top() );
	}
	
	public int getPc() {
		return m_pc;
	}
	
	public void setPc( int p_pc ) {
		m_pc = p_pc;
	}
	
	public void loadProgram( ArrayList<ProgramLine> p_program ) {
		m_program = p_program;
	}
	
	public void runProgram() {
		while(  m_pc < m_program.size() ) {
			stepProgram();
		}
	}
	
	public Integer stepProgram() {
		ProgramLine line = m_program.get( m_pc );
		Instructions instr = line.getInstr();
		Integer val = line.getVal();
		Integer result = new Integer(-1);
		
		switch( instr ) {
		case PUSH:
			push( val );
			m_pc++;
			break;
		case POP:
			result = pop();
			m_pc++;
			break;
		case ADD:
			add();
			m_pc++;
			break;
		case IFEQ:
			ifeq( val );
			m_pc++;
			break;
		case JUMP:
			jump( val );
			break;
		case PRINT:
			print();
			m_pc++;
			break;	
		case DUP:
			dup();
			m_pc++;
			break;
		default:
		}
		
		return result;
	}
	
	// public for ease of testing
	public ArrayList<Integer> m_stack;
	ArrayList<ProgramLine> m_program;
	int m_pc;
}
