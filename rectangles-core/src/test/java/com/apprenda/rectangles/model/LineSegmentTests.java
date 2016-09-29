package com.apprenda.rectangles.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LineSegmentTests {

	@Test(expected = NullPointerException.class)
	public void rejectNullPoint1() {
		new LineSegment(null, new Point(0,0));
	}
	
	@Test(expected = NullPointerException.class)
	public void rejectNullPoint2() {
		new LineSegment(new Point(0,0), null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void rejectIdenticalPoint() {
		Point p = new Point(0,0);
		new LineSegment(p, p);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void axisAligned() {
		new LineSegment(new Point(0,0), new Point(1,1));
	}
	
	@Test
	public void upperLowerBoundX() {
		Point p1 = new Point(0,0);
		Point p2 = new Point(1,0);
		
		LineSegment line = new LineSegment(p1, p2);
		
		assertEquals(p1, line.getLowerBound());
		assertEquals(p2, line.getUpperBound());
	}
	
	@Test
	public void upperLowerBoundY() {
		Point p1 = new Point(0,0);
		Point p2 = new Point(0,1);
		
		LineSegment line = new LineSegment(p1, p2);
		
		assertEquals(p1, line.getLowerBound());
		assertEquals(p2, line.getUpperBound());
	}
	
	@Test
	public void intersectsWith() {
		LineSegment line1 = new LineSegment(new Point(0,0), new Point(5,0));
		
		// orthogonal to line 1 and crosses
		LineSegment line2 = new LineSegment(new Point(2,2), new Point(2,-2));
		
		Point intersection = new Point(2,0);
		
		assertEquals(intersection, line1.intersectsAtPointWith(line2));
		assertEquals(intersection, line2.intersectsAtPointWith(line1));
		assertTrue(line1.intersectsWith(line2));
		assertTrue(line2.intersectsWith(line1));
		
		// parallel to line 1
		LineSegment line3 = new LineSegment(new Point(0,1), new Point(5,1));
		
		assertFalse(line1.intersectsWith(line3));
		assertFalse(line3.intersectsWith(line1));
		
		// touches line 1, but does not cross
		LineSegment line4 = new LineSegment(new Point(3,0), new Point(3,5));
		
		assertTrue(line1.intersectsWith(line4));
		assertTrue(line4.intersectsWith(line1));
		
		// overlaps line 1, parallel
		LineSegment line5 = new LineSegment(new Point(3,0), new Point(7,0));
		
		assertFalse(line1.intersectsWith(line5));
		assertFalse(line5.intersectsWith(line1));
	}
	
	@Test
	public void overlapsWith() {
		LineSegment line1 = new LineSegment(new Point(0,0), new Point(5,0));
		
		// orthogonal to line 1 and crosses
		LineSegment line2 = new LineSegment(new Point(2,2), new Point(2,-2));
		
		assertFalse(line1.overlapsWith(line2));
		assertFalse(line2.overlapsWith(line1));
		
		// lines touch end-to-end
		LineSegment line3 = new LineSegment(new Point(5,0), new Point(10,0));
		
		assertTrue(line1.overlapsWith(line3));
		assertTrue(line3.overlapsWith(line1));
		
		// overlaps line 1 (encompassed by line 1)
		LineSegment line4 = new LineSegment(new Point(2,0), new Point(3,0));
		
		assertTrue(line1.overlapsWith(line4));
		assertTrue(line4.overlapsWith(line1));
		assertEquals(1, line1.overlapLengthWith(line4));
		assertEquals(1, line4.overlapLengthWith(line1));
		
		// overlaps line 1
		LineSegment line5 = new LineSegment(new Point(2,0), new Point(7,0));
		
		assertTrue(line1.overlapsWith(line5));
		assertTrue(line5.overlapsWith(line1));
		assertEquals(3, line1.overlapLengthWith(line5));
		assertEquals(3, line5.overlapLengthWith(line1));
	}
	
	
}
