package net.onedaybeard.recursiveten.lsystem;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LSystemTest
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testGetIteration_algae()
	{
		LSystem system = new LSystem("A");
		system.addProduction('A', "AB");
		system.addProduction('B', "A");
		
		String expected7thIteration = "ABAABABAABAABABAABABAABAABABAABAAB";
		
		assertEquals(expected7thIteration, system.getIteration(7));
	}
	
	@Test
	public void testGetIteration_simple_tree()
	{
		LSystem system = new LSystem("0");
		system.addProduction('0', "1[0]0");
		system.addProduction('1', "11");
		
		String expected3thIteration = "1111[11[1[0]0]1[0]0]11[1[0]0]1[0]0";
		
		assertEquals(expected3thIteration, system.getIteration(3));
	}
	
	@Test
	public void testGetIteration_koch_curve()
	{
		LSystem system = new LSystem("F");
		system.addProduction('F', "F+F−F−F+F");
		
		String expected3thIteration = "F+F−F−F+F+F+F−F−F+F−F+F−F−F+F−F+F−F−F+F+F+F−F−F+F+F"
			+ "+F−F−F+F+F+F−F−F+F−F+F−F−F+F−F+F−F−F+F+F+F−F−F+F−F+F−F−F+F+F+F−F−F+F−F+F−F−F+"
			+ "F−F+F−F−F+F+F+F−F−F+F−F+F−F−F+F+F+F−F−F+F−F+F−F−F+F−F+F−F−F+F+F+F−F−F+F+F+F−F"
			+ "−F+F+F+F−F−F+F−F+F−F−F+F−F+F−F−F+F+F+F−F−F+F";
		
		assertEquals(expected3thIteration, system.getIteration(3));
	}
}
