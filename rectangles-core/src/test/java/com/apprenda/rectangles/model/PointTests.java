package com.apprenda.rectangles.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class PointTests {

	@Test
	public void equality() {
		int x = 0;
		int y = 0;
		
		Point p1 = new Point(x,y);
		Point p2 = new Point(x,y);
		
		assertEquals(p1, p2);
	}
	
	@Test
	public void nonEquality() {
		Point p1 = new Point(0,0);
		Point p2 = new Point(1,1);
		
		assertNotEquals(p1, p2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void rejectCompareNonCollinearPoints() {
		Point p1 = new Point(0,0);
		Point p2 = new Point(1,1);
		
		p1.compareTo(p2);
	}
	
	@Test
	public void compareXAlignedPoints() {
		Point p1 = new Point(0,0);
		Point p2 = new Point(1,0);
		
		assertEquals(-1, p1.compareTo(p2));
		assertEquals(1, p2.compareTo(p1));
	}
	
	@Test
	public void compareYAlignedPoints() {
		Point p1 = new Point(0,0);
		Point p2 = new Point(0,1);
		
		assertEquals(-1, p1.compareTo(p2));
		assertEquals(1, p2.compareTo(p1));
	}
}
