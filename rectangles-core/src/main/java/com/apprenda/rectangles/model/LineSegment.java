package com.apprenda.rectangles.model;

import com.apprenda.rectangles.OverflowException;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Axis-aligned line segment.
 * 
 * @author Cody McCain
 *
 */
@Getter
@EqualsAndHashCode
@Slf4j
public class LineSegment {
	final Point lowerBound;
	final Point upperBound;
	final long length;
	final Axis axis;
	final Side side;

	public LineSegment(@NonNull Point p1, @NonNull Point p2) {
		this(p1, p2, null);
	}

	/**
	 * Create a new line from two axis-aligned points. The points can be passed
	 * in any order; however, they will be converted to lower and upper bound
	 * according to the axis aligned coordinate magnitude.
	 * 
	 * @param p1
	 *            point1
	 * @param p2
	 *            point2
	 * 
	 * @exception IllegalArgumentException
	 *                if p1==p2
	 * @exception IllegalArgumentException
	 *                if p1 and p2 are not axis aligned
	 */
	public LineSegment(@NonNull Point p1, @NonNull Point p2, Side side) {

		if (p1.equals(p2)) {
			throw new IllegalArgumentException("line cannot be composed of two identical points");
		}

		if (p1.x != p2.x && p1.y != p2.y) {
			throw new IllegalArgumentException("only axis aligned lines are supported");
		}

		axis = (p1.y == p2.y) ? Axis.X : Axis.Y;

		if (p1.compareTo(p2) == -1) {
			lowerBound = p1;
			upperBound = p2;
		} else {
			lowerBound = p2;
			upperBound = p1;
		}

		this.length = calcLength();

		if (side != null && side.getAxis() != axis) {
			throw new IllegalArgumentException("side axis alignment does not match axis alignment of segment");
		}
		this.side = side;
	}

	private long calcLength() {
		long length = 0;

		switch (axis) {
		case X:
			length = upperBound.x - lowerBound.x;
			break;
		case Y:
			length = upperBound.y - lowerBound.y;
			break;
		default:
			throw new UnknownError("unknown axis");
		}

		if (length < 0) {
			throw new OverflowException("width is greater than Long.MAX_VALUE");
		}

		return length;
	}

	/**
	 * Returns true if this line segment intersects with the provided line
	 * segment.
	 * 
	 * @see {@link #intersectsAtPointWith(LineSegment)}
	 * 
	 * @param segment
	 *            segment to check for intersection
	 * @return true if the line segments intersect
	 */
	public boolean intersectsWith(@NonNull LineSegment segment) {
		return (intersectsAtPointWith(segment) != null);
	}

	/**
	 * Find the point at which two orthogonal line segments intersect. Two line
	 * segments intersect if they share a common point. We use this definition
	 * since we are dealing with discrete values defined in inclusive ranges.
	 * 
	 * @param line
	 *            line to find intersection point with
	 * @return point of intersection or null if none
	 */
	public Point intersectsAtPointWith(@NonNull LineSegment line) {
		if (axis == line.axis) {
			log.trace("lines are parallel or part of same line segment");
			return null;
		}

		LineSegment xLine;
		LineSegment yLine;

		log.trace(axis.toString());

		switch (axis) {
		case X:
			xLine = this;
			yLine = line;
			break;
		case Y:
			xLine = line;
			yLine = this;
			break;
		default:
			throw new UnknownError("unknown axis type");
		}

		if (xLine.lowerBound.x <= yLine.lowerBound.x && xLine.upperBound.x >= yLine.lowerBound.x
				&& yLine.lowerBound.y <= xLine.lowerBound.y && yLine.upperBound.y >= xLine.lowerBound.y) {
			Point intersection = new Point(yLine.lowerBound.x, xLine.lowerBound.y);
			log.trace("intersection: {}", intersection);
			return intersection;
		} else {
			log.trace("no intersection");
			return null;
		}
	}

	/**
	 * Returns true if the line overlaps
	 * 
	 * @see {@link #overlapLengthWith(LineSegment)}
	 * 
	 * @param segment
	 *            line segment to check for overlap
	 * @return true if the lines overlap, false otherwise
	 */
	public boolean overlapsWith(@NonNull LineSegment segment) {
		return (overlapLengthWith(segment) >= 0);
	}

	/**
	 * Calculate length of overlapping line segment with provided line segment.
	 * If the line segments do not overlap, return -1. A length of zero
	 * signifies line segments share only a point.
	 * 
	 * @param segment
	 *            line to find overlap length with
	 * @return length of overlapping line segment, -1 if lines do not overlap
	 */
	public long overlapLengthWith(@NonNull LineSegment segment) {
		long length;

		if (isOrthogonalTo(segment)) {
			log.trace("line segments are orthogonal");
			length = -1;
		} else if (isParallelTo(segment)) {
			log.trace("segments are parallel");
			length = -1;
		} else {
			switch (axis) {
			case X:
				length = Math.min(upperBound.x, segment.upperBound.x) - Math.max(lowerBound.x, segment.lowerBound.x);
				break;
			case Y:
				length = Math.min(upperBound.y, segment.upperBound.y) - Math.max(lowerBound.y, segment.lowerBound.y);
				break;
			default:
				throw new UnknownError("unknown axis type");
			}
		}

		log.trace("overlap length: {}, segment1: {}, segment2: {}", length, this, segment);
		return length;
	}

	/**
	 * Returns true if the line segments are orthogonal to one another.
	 * 
	 * @param lineSegment
	 *            line segment to compare with
	 * @return true if orthogonal, false otherwise
	 */
	public boolean isOrthogonalTo(@NonNull LineSegment lineSegment) {
		return axis.isOrthogonalTo(lineSegment.axis);
	}

	/**
	 * Returns true is both segments are aligned to the same axis but are
	 * parallel and not part of the same line.
	 * 
	 * @param lineSegment
	 *            line segment to compare with
	 * @return true if parallel, false otherwise.
	 */
	public boolean isParallelTo(@NonNull LineSegment lineSegment) {
		if (isOrthogonalTo(lineSegment)) {
			return false;
		}

		switch (axis) {
		case X:
			return (lowerBound.y != lineSegment.lowerBound.y);
		case Y:
			return (lowerBound.x != lineSegment.lowerBound.x);
		default:
			throw new UnknownError("unknown axis type");
		}
	}

	/**
	 * Returns true if this line segment contains the provided line segment
	 * 
	 * @param segment
	 *            segment to check
	 * @return true if this line segment contains the provided line segment
	 */
	public boolean contains(@NonNull LineSegment segment) {
		return (contains(segment.lowerBound) && contains(segment.upperBound));
	}

	/**
	 * Returns true if the point is contained within this line segment
	 * 
	 * @param point
	 *            point to check
	 * @return true if the point is contained within this line segment
	 */
	public boolean contains(@NonNull Point point) {
		switch (axis) {
		case X:
			return (point.y == lowerBound.y && point.x >= lowerBound.x && point.x <= upperBound.x);
		case Y:
			return (point.x == lowerBound.x && point.y >= lowerBound.y && point.y <= upperBound.y);
		default:
			throw new UnknownError("unknown axis");
		}
	}

	@Override
	public String toString() {
		return String.format("(%d %d, %d %d)", lowerBound.x, lowerBound.y, upperBound.x, upperBound.y);
	}

}
