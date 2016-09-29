package com.apprenda.rectangles.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Point composed of x and y coordinates
 * 
 * @author Cody McCain
 *
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Point implements Comparable<Point> {
	long x;
	long y;

	/**
	 * Return true if the provided point shares either an x coordinate or y
	 * coordinate value
	 * 
	 * @param point
	 *            point to compare
	 * @return true if the points are collinear
	 */
	boolean isCollinearTo(Point point) {
		return (x == point.x || y == point.y);
	}

	/**
	 * Only works with collinear points
	 * 
	 * @param point
	 *            point to compare
	 * 
	 * @exception IllegalArgumentException
	 *                if the provided point is not collinear
	 */
	@Override
	public int compareTo(Point point) {
		if (!isCollinearTo(point)) {
			throw new IllegalArgumentException("compare operation only valid on collinear points");
		}

		final Axis axis = (y == point.y) ? Axis.X : Axis.Y;

		switch (axis) {
		case X:
			return Long.compare(x, point.x);
		case Y:
			return Long.compare(y, point.y);
		default:
			throw new UnknownError("unknown axis");
		}
	}
	
	@Override
	public String toString() {
		return String.format("(%d %d)", x, y);
	}
}
