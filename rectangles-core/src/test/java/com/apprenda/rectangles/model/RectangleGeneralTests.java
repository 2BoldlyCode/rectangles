package com.apprenda.rectangles.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.apprenda.rectangles.OverflowException;

public class RectangleGeneralTests {

	@Test(expected = NullPointerException.class)
	public void rejectsNullPoint1() {
		Point p = new Point(0, 0);
		new Rectangle(null, p);
	}

	@Test(expected = NullPointerException.class)
	public void rejectsNullPoint2() {
		Point p = new Point(0, 0);
		new Rectangle(p, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsIdenticalPoints() {
		Point p1 = new Point(0, 0);
		new Rectangle(p1, p1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void rejectcollinearPointsX() {
		Point p1 = new Point(0, 0);
		Point p2 = new Point(0, 5);
		
		new Rectangle(p1, p2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void rejectcollinearPointsY() {
		Point p1 = new Point(0, 0);
		Point p2 = new Point(5, 0);
		
		new Rectangle(p1, p2);
	}

	@Test
	public void normalizesPoints() {
		Point topLeft = new Point(0, 1);
		Point bottomRight = new Point(1, 0);
		
		Point bottomLeft = new Point(0, 0);
		Point topRight = new Point(1, 1);
		
		Rectangle rect = new Rectangle(bottomLeft, topRight);
		assertEquals(bottomLeft, rect.bottom().getLowerBound());
		assertEquals(topRight, rect.top().getUpperBound());
		
		rect = new Rectangle(topRight, bottomLeft);
		assertEquals(bottomLeft, rect.bottom().getLowerBound());
		assertEquals(topRight, rect.top().getUpperBound());

		rect = new Rectangle(topLeft, bottomRight);
		assertEquals(bottomLeft, rect.bottom().getLowerBound());
		assertEquals(topRight, rect.top().getUpperBound());
		
		rect = new Rectangle(bottomRight, topLeft);
		assertEquals(bottomLeft, rect.bottom().getLowerBound());
		assertEquals(topRight, rect.top().getUpperBound());
	}
	
	@Test
	public void width() {
		Point bottomLeft = new Point(0, 0);
		Point topRight = new Point(2, 3);
		
		Rectangle rect = new Rectangle(bottomLeft, topRight);
		assertEquals(2L, rect.getWidth());
	}
	
	@Test(expected = OverflowException.class)
	public void widthOverflow() {
		Point bottomLeft = new Point(Long.MIN_VALUE,0);
		Point topRight = new Point(Long.MAX_VALUE, 1);
		
		new Rectangle(bottomLeft, topRight);
	}
	
	@Test
	public void height() {
		Point bottomLeft = new Point(0, 0);
		Point topRight = new Point(2, 3);
		
		Rectangle rect = new Rectangle(bottomLeft, topRight);
		assertEquals(3L, rect.getHeight());
	}
	
	@Test(expected = OverflowException.class)
	public void heightOverflow() {
		Point bottomLeft = new Point(0, Long.MIN_VALUE);
		Point topRight = new Point(1, Long.MAX_VALUE);
		
		new Rectangle(bottomLeft, topRight);
	}
	
	@Test
	public void area() {
		Point bottomLeft = new Point(0, 0);
		Point topRight = new Point(2, 3);
		
		Rectangle rect = new Rectangle(bottomLeft, topRight);
		assertEquals(6L, rect.getArea());
	}
	
	@Test(expected = OverflowException.class)
	public void areaOverflow() {
		Point bottomLeft = new Point(0, 0);
		Point topRight = new Point(Long.MAX_VALUE, Long.MAX_VALUE);
		
		new Rectangle(bottomLeft, topRight);
	}
}
