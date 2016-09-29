package com.apprenda.rectangles.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class RectangleContainmentTests {

	private void affirmative(Rectangle container, Rectangle containee) {
		assertTrue(container.contains(containee));
		assertFalse(containee.contains(container));
	}
	
	private void negative(Rectangle r1, Rectangle r2) {
		assertFalse(r1.contains(r2));
		assertFalse(r2.contains(r1));
	}
	
	@Test
	public void containsPoint() {
		Rectangle r1 = new Rectangle(new Point(0,0), new Point(5,5));
		Point leftEdge = new Point(0,3);
		Point rightEdge = new Point(5,3);
		Point bottomEdge = new Point(3,0);
		Point topEdge = new Point(3,5);
		Point inside = new Point(3,3);
		Point corner = new Point(0,0);
		Point outside = new Point(10,10);
		
		assertTrue(r1.contains(leftEdge));
		assertTrue(r1.contains(rightEdge));
		assertTrue(r1.contains(topEdge));
		assertTrue(r1.contains(bottomEdge));
		assertTrue(r1.contains(inside));
		assertTrue(r1.contains(corner));
		assertFalse(r1.contains(outside));
	}
	
	@Test
	public void identity() {
		Rectangle r1 = new Rectangle(new Point(0,0), new Point(5,5));
		
		assertTrue(r1.contains(r1));
	}
	
	@Test
	public void containmentNoSharedEdges() {
		Rectangle r1 = new Rectangle(new Point(0,0), new Point(5,5));
		Rectangle r2 = new Rectangle(new Point(1,1), new Point(4,4));
		
		affirmative(r1, r2);
	}
	
	@Test
	public void overlappingSideArea() {
		Rectangle r1 = new Rectangle(new Point(0,0), new Point(5,5));
		Rectangle r2 = new Rectangle(new Point(1,1), new Point(6,4));
		
		negative(r1, r2);
	}
	
	@Test
	public void overlappingSideAreaInverse() {
		Rectangle r1 = new Rectangle(new Point(0,0), new Point(5,5));
		Rectangle r2 = new Rectangle(new Point(1,1), new Point(4,6));
		
		negative(r1, r2);
	}
	
	@Test
	public void adjacent() {
		Rectangle r1 = new Rectangle(new Point(0,0), new Point(5,5));
		Rectangle r2 = new Rectangle(new Point(5,0), new Point(10,5));
		
		negative(r1, r2);
	}
	
	@Test
	public void adjacentInverse() {
		Rectangle r1 = new Rectangle(new Point(0,0), new Point(5,5));
		Rectangle r2 = new Rectangle(new Point(0,5), new Point(5,10));
		
		negative(r1, r2);
	}
	
	@Test
	public void separated() {
		Rectangle r1 = new Rectangle(new Point(0,0), new Point(2,2));
		Rectangle r2 = new Rectangle(new Point(3,3), new Point(5,5));
		
		negative(r1, r2);
	}

}
