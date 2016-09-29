package com.apprenda.rectangles.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class RectangleAdjacencyTests {
	
	private void affirmative(Rectangle r1, Rectangle r2) {
		assertTrue(r1.adjacentTo(r2));
		assertTrue(r2.adjacentTo(r1));
	}
	
	private void negative(Rectangle r1, Rectangle r2) {
		assertFalse(r1.adjacentTo(r2));
		assertFalse(r2.adjacentTo(r1));
	}
	
	@Test
	public void sharedCornerPoint() {
		Rectangle r1 = new Rectangle(new Point(0,0), new Point(1,1));
		Rectangle r2 = new Rectangle(new Point(1,1), new Point(2,2));
		
		affirmative(r1, r2);
	}
	
	@Test
	public void adjacentFullSide() {
		Rectangle r1 = new Rectangle(new Point(0,0), new Point(5,5));
		Rectangle r2 = new Rectangle(new Point(5,0), new Point(10,5));
		
		affirmative(r1, r2);
	}
	
	@Test
	public void adjacentFullSideInverse() {
		Rectangle r1 = new Rectangle(new Point(0,0), new Point(5,5));
		Rectangle r2 = new Rectangle(new Point(0,5), new Point(5,10));
		
		affirmative(r1, r2);
	}
	
	@Test
	public void adjacentPartialSide() {
		Rectangle r1 = new Rectangle(new Point(0,1), new Point(5,4));
		Rectangle r2 = new Rectangle(new Point(5,0), new Point(10,5));
		
		affirmative(r1, r2);
	}
	
	@Test
	public void adjacentPartialSideInverse() {
		Rectangle r1 = new Rectangle(new Point(1,0), new Point(4,5));
		Rectangle r2 = new Rectangle(new Point(0,5), new Point(5,10));
		
		affirmative(r1, r2);
	}

	@Test
	public void overlappingSide() {
		Rectangle r1 = new Rectangle(new Point(0,0), new Point(5,5));
		Rectangle r2 = new Rectangle(new Point(0,1), new Point(5,4));
		
		negative(r1, r2);
	}
	
	@Test
	public void overlappingSharedEdge() {
		Rectangle r1 = new Rectangle(new Point(0,0), new Point(5,5));
		Rectangle r2 = new Rectangle(new Point(1,1), new Point(5,4));
		
		negative(r1, r2);
	}
	
	@Test
	public void separated() {
		Rectangle r1 = new Rectangle(new Point(0,0), new Point(5,5));
		Rectangle r2 = new Rectangle(new Point(6,0), new Point(11,5));
		
		negative(r1, r2);
	}
	
	@Test
	public void separatedInverse() {
		Rectangle r1 = new Rectangle(new Point(0,0), new Point(5,5));
		Rectangle r2 = new Rectangle(new Point(0,6), new Point(5,11));
		
		negative(r1, r2);
	}
	
	
}
