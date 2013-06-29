package com.liljeson.mattias.interpretator.backendtest;

import com.liljeson.mattias.interpretator.backend.*;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class BackendTest {
	Backend m_stack;
	
	@Before
	public void setUp() throws Exception {
		m_stack = new Backend();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPushAndPop() {
		m_stack.push( 42 ); // Answer of everything
		Integer res = m_stack.pop();
		
		assertEquals( "testPushAndPop: pushed integer not popped",
				new Integer(42), res );
	}
	
	@Test
	public void testTop() {
		m_stack.push( 12 );
		Integer top = m_stack.top();
		assertEquals( "testTop: pushed integer not returned by top",
				new Integer(12), top );
	}
	
	@Test
	public void testAdd() {
		m_stack.push(42);
		m_stack.push(2);
		m_stack.add();
		Integer res = m_stack.pop();
		
		assertEquals( "testAdd: pushed integers not added",
				new Integer(44), res );
	}
	
	@Test
	public void testMult() {
		m_stack.push(42);
		m_stack.push(2);
		m_stack.mult();
		Integer res = m_stack.pop();
		
		assertEquals( "testMult: pushed integers not multiplied",
				new Integer(84), res );
	}
	
	@Test 
	public void testGetPcAndSetPc() {
		Integer pc = m_stack.getPc();
		assertEquals( "testGetPC: m_pc not 0 after ctor",
				new Integer(0), pc );
		
		m_stack.setPc(11);
		Integer newPc = m_stack.getPc();
		assertEquals( "testGetPC: getPc not what setPc was fed with",
				new Integer(11), newPc );
	}
	
	@Test 
	public void testIfeq() {
		m_stack.push( 0 );
		m_stack.ifeq( 42 );
		Integer pc1 = m_stack.getPc();
		assertEquals( "testIfeq: m_pc set after ifeq",
				new Integer(0), pc1 );
		
		m_stack.push( 1 );
		m_stack.ifeq( 42 );
		Integer pc2 = m_stack.getPc();
		assertEquals( "testIfeq: m_pc not set after ifeq",
				new Integer(42), pc2 );	
	}
	
	@Test
	public void testJump() {
		m_stack.jump( 8 );
		Integer pc = m_stack.getPc();
		assertEquals( "testJump: m_pc not set after jump",
				new Integer(8), pc );	
	}
	
	@Test
	public void testPrint() {
		// Untestable?	
	}
	
	@Test
	public void testDup() {
		m_stack.push( 10 );
		m_stack.dup();
		
		Integer first = m_stack.pop();
		Integer second = m_stack.pop();

		assertEquals( "testDup: two pops differ after dup",
				first, second );
	}
	
	@Test
	public void testLoadProgramStepAndRun() {	
		m_stack.loadProgram( Programs.prog1() );
		
		m_stack.stepProgram();
		Integer pcAfter = m_stack.getPc();
		assertEquals( "testLoadProgramAndRun: step not changing m_pc",
				new Integer( 1 ), pcAfter );
		
		m_stack.setPc( 0 );
		m_stack.stepProgram();
		Integer top1 = m_stack.top();
		assertEquals( "testLoadProgramAndRun: step not moving program forward on first step",
				new Integer( 4 ), top1 );
		m_stack.stepProgram();
		Integer top2 = m_stack.top();
		assertEquals( "testLoadProgramAndRun: step not moving program forward  on second step",
				new Integer( 5 ), top2 );
		
		m_stack.setPc( 0 );
		m_stack.runProgram();
		Integer lineCnt = Programs.prog1().size();
		Integer lastPc = m_stack.getPc();
		assertEquals( "testLoadProgramAndRun: m_pc not set to last +1 after running",
				lineCnt, lastPc );
	}
}
