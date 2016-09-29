package com.apprenda.rectangles.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RectangleIntersectionTests {

	void affirmativeIntersection(Rectangle r1, Rectangle r2, Rectangle intersection) {
		assertEquals(intersection, r1.overlappingAreaOf(r2));
		assertEquals(intersection, r2.overlappingAreaOf(r1));

		assertTrue(r1.intersectsWith(r2));
		assertTrue(r2.intersectsWith(r1));
	}

	void negativeIntersection(Rectangle r1, Rectangle r2) {
		assertNull(r1.overlappingAreaOf(r2));
		assertNull(r2.overlappingAreaOf(r1));

		assertFalse(r1.intersectsWith(r2));
		assertFalse(r2.intersectsWith(r1));
	}

	void noAreaAffirmativeIntersection(Rectangle r1, Rectangle r2) {
		assertNull(r1.overlappingAreaOf(r2));
		assertNull(r2.overlappingAreaOf(r1));

		assertTrue(r1.intersectsWith(r2));
		assertTrue(r2.intersectsWith(r1));
	}

	@Test
	public void identity() {
		Rectangle r = new Rectangle(new Point(0, 0), new Point(5, 5));

		assertEquals(r, r.overlappingAreaOf(r));
		assertTrue(r.intersectsWith(r));
	}

	@Test
	public void containment() {
		Rectangle r1 = new Rectangle(new Point(0, 0), new Point(5, 5));
		Rectangle r2 = new Rectangle(new Point(1, 1), new Point(4, 4));

		affirmativeIntersection(r1, r2, r2);
	}

	@Test
	public void overlappingCorners() {
		Rectangle r1 = new Rectangle(new Point(0, 0), new Point(5, 5));
		Rectangle r2 = new Rectangle(new Point(1, 1), new Point(6, 6));
		Rectangle intersection = new Rectangle(new Point(1, 1), new Point(5, 5));

		affirmativeIntersection(r1, r2, intersection);
	}

	@Test
	public void overlappingSide() {
		Rectangle r1 = new Rectangle(new Point(0, 0), new Point(5, 5));
		Rectangle r2 = new Rectangle(new Point(1, 1), new Point(6, 4));
		Rectangle intersection = new Rectangle(new Point(1, 1), new Point(5, 4));

		affirmativeIntersection(r1, r2, intersection);
	}

	@Test
	public void overlappingSideInverse() {
		Rectangle r1 = new Rectangle(new Point(0, 0), new Point(5, 5));
		Rectangle r2 = new Rectangle(new Point(1, 1), new Point(4, 6));
		Rectangle intersection = new Rectangle(new Point(1, 1), new Point(4, 5));

		affirmativeIntersection(r1, r2, intersection);
	}

	@Test
	public void adjacentSharedEdge() {
		Rectangle r1 = new Rectangle(new Point(0, 0), new Point(5, 5));
		Rectangle r2 = new Rectangle(new Point(5, 0), new Point(10, 5));

		noAreaAffirmativeIntersection(r1, r2);
	}

	@Test
	public void sharedCorner() {
		Rectangle r1 = new Rectangle(new Point(0, 0), new Point(1, 1));
		Rectangle r2 = new Rectangle(new Point(1, 1), new Point(2, 2));

		noAreaAffirmativeIntersection(r1, r2);
	}

	@Test
	public void separated() {
		Rectangle r1 = new Rectangle(new Point(0, 0), new Point(1, 1));
		Rectangle r2 = new Rectangle(new Point(2, 2), new Point(3, 3));

		negativeIntersection(r1, r2);
	}
}
