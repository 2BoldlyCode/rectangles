package com.apprenda.rectangles.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.apprenda.rectangles.OverflowException;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Axis-aligned rectangle
 * 
 * @author Cody McCain
 *
 */
@Getter
@EqualsAndHashCode
@Slf4j
public class Rectangle {
	final Map<Side, LineSegment> sides;

	final long width;
	final long height;
	final long area;

	/**
	 * Create a new rectangle from any two non-collinear points
	 * 
	 * @param p1
	 *            point
	 * @param p2
	 *            point
	 * 
	 * @exception NullPointerException
	 *                if either point is null
	 * @exception IllegalArgumentException
	 *                if the points are collinear
	 */
	public Rectangle(@NonNull Point p1, @NonNull Point p2) {
		if (p1.equals(p2)) {
			throw new IllegalArgumentException("points p1 and p2 must not be identical");
		}

		if (p1.isCollinearTo(p2)) {
			throw new IllegalArgumentException("points cannot be collinear");
		}

		this.sides = buildSides(p1, p2);

		this.width = bottom().length;
		this.height = left().length;
		this.area = calcArea(width, height);
	}

	/**
	 * Convenience method to access top side
	 * 
	 * @return top side line segment
	 */
	public LineSegment top() {
		return sides.get(Side.TOP);
	}

	/**
	 * Convenience method to access bottom side
	 * 
	 * @return bottom side line segment
	 */
	public LineSegment bottom() {
		return sides.get(Side.BOTTOM);
	}

	/**
	 * Convenience method to access left side
	 * 
	 * @return left side line segment
	 */
	public LineSegment left() {
		return sides.get(Side.LEFT);
	}

	/**
	 * Convenience method to access right side
	 * 
	 * @return right side line segment
	 */
	public LineSegment right() {
		return sides.get(Side.RIGHT);
	}

	/**
	 * Get side line segment
	 * 
	 * @param side
	 *            side to return
	 * @return line segment
	 */
	public LineSegment getSide(@NonNull Side side) {
		return sides.get(side);
	}

	private Map<Side, LineSegment> buildSides(Point p1, Point p2) {

		Point bottomLeft = findBottomLeft(p1, p2);
		Point topRight = findTopRight(p1, p2);
		Point bottomRight = new Point(topRight.x, bottomLeft.y);
		Point topLeft = new Point(bottomLeft.x, topRight.y);

		Map<Side, LineSegment> sides = new HashMap<>(4);

		sides.put(Side.BOTTOM, new LineSegment(bottomLeft, bottomRight, Side.BOTTOM));
		sides.put(Side.RIGHT, new LineSegment(bottomRight, topRight, Side.RIGHT));
		sides.put(Side.TOP, new LineSegment(topLeft, topRight, Side.TOP));
		sides.put(Side.LEFT, new LineSegment(bottomLeft, topLeft, Side.LEFT));

		return Collections.unmodifiableMap(sides);
	}

	private Point findBottomLeft(Point p1, Point p2) {
		return new Point(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y));
	}

	private Point findTopRight(Point p1, Point p2) {
		return new Point(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y));
	}

	private long calcArea(long width, long height) {
		boolean overflowed = false;
		long area = 0;

		if (width == Long.MAX_VALUE && height == Long.MAX_VALUE) {
			overflowed = true;
		} else {
			area = width * height;
			if (area < 0) {
				overflowed = true;
			}
		}

		if (overflowed) {
			throw new OverflowException("area is greater than Long.MAX_VALUE");
		}

		return area;
	}

	/**
	 * Returns true if the provided rectangle is adjacent.
	 * 
	 * @param rectangle
	 *            rectangle to analyze
	 * @return true if provided rectangle is adjacent, false otherwise
	 */
	public boolean adjacentTo(@NonNull Rectangle rectangle) {
		return Arrays.stream(Side.values()).map(side -> {
			return getSide(side).overlapsWith(rectangle.getSide(side.parallel()));
		}).reduce((result, overlaps) -> result || overlaps).get();
	}

	/**
	 * Return the rectangle that represents the overlapping region between this
	 * rectangle and the provided rectangle.
	 * 
	 * @param rectangle
	 *            rectangle to analyze for overlap
	 * @return rectangle representing overlapping region, null if the rectangles
	 *         do not overlap
	 */
	public Rectangle overlappingAreaOf(@NonNull Rectangle rectangle) {
		if (adjacentTo(rectangle)) {
			return null;
		}

		final Set<Point> intersectionPoints = intersectionPointsWith(rectangle);
		return buildOverlapRectangle(rectangle, intersectionPoints);
	}

	private Rectangle buildOverlapRectangle(@NonNull Rectangle rectangle, @NonNull Set<Point> intersectionPoints) {
		if (intersectionPoints.size() < 2) {

			if (contains(rectangle)) {
				return rectangle;
			} else if (rectangle.contains(this)) {
				return this;
			} else {
				log.trace("separated rectangles");
				return null;
			}

		}

		final Point[] intersections = new Point[intersectionPoints.size()];
		intersectionPoints.toArray(intersections);

		if (intersections.length >= 3) {
			return buildOverlapFrom3PlusPoints(intersections);
		} else {
			return buildOverlapFrom2Points(rectangle, intersections);
		}

	}

	private Rectangle buildOverlapFrom2Points(@NonNull Rectangle rectangle, Point[] intersections) {
		final Point p1 = intersections[0];
		final Point p2 = intersections[1];

		if (!p1.isCollinearTo(p2)) {
			return new Rectangle(p1, p2);
		}

		LineSegment intersectionEdge = new LineSegment(p1, p2);
		Rectangle outerRectangle;
		Rectangle innerRectangle;

		if (onEdge(intersectionEdge)) {
			innerRectangle = rectangle;
			outerRectangle = this;
		} else {
			innerRectangle = this;
			outerRectangle = rectangle;
		}

		LineSegment orthogonalEdge = Side.sidesAlignedTo(intersectionEdge.axis.orthogonal()).stream().map(side -> {
			return innerRectangle.getSide(side);
		}).filter(ls -> ls.contains(p2)).findFirst().get();

		Point p3;
		if (outerRectangle.contains(orthogonalEdge.lowerBound)) {
			p3 = orthogonalEdge.lowerBound;
		} else {
			p3 = orthogonalEdge.upperBound;
		}

		return new Rectangle(p1, p3);
	}

	private Rectangle buildOverlapFrom3PlusPoints(@NonNull Point[] intersections) {
		Point p1 = intersections[0];
		Point p2 = null;

		for (int i = 1; i < intersections.length; i++) {
			p2 = intersections[i];
			if (p2.isCollinearTo(p1)) {
				p2 = null;
				continue;
			} else {
				break;
			}
		}

		if (p2 == null) {
			throw new RuntimeException("unable to find overlap rectangle");
		}

		return new Rectangle(p1, p2);
	}

	/**
	 * Find all intersection points with the provided rectangle
	 * 
	 * @param rectangle
	 *            rectangle to intersect with
	 * @return set of intersection points, empty list if no intersections
	 */
	public Set<Point> intersectionPointsWith(@NonNull Rectangle rectangle) {
		return Arrays.stream(Side.values()).flatMap(side -> {
			return side.orthogonal().stream().map(orthogonalSide -> {
				return getSide(side).intersectsAtPointWith(rectangle.getSide(orthogonalSide));
			});
		}).filter(p -> (p != null)).collect(Collectors.toSet());
	}

	/**
	 * Returns true if this rectangle intersects with the provided rectangle.
	 * 
	 * @param rectangle
	 *            rectangle to check for intersection
	 * @return true if rectangle intersects, false otherwise
	 */
	public boolean intersectsWith(@NonNull Rectangle rectangle) {
		Set<Point> intersectionPoints = intersectionPointsWith(rectangle);

		return (!intersectionPoints.isEmpty() || contains(rectangle) || rectangle.contains(this));
	}

	/**
	 * Returns true if the this rectangle contains the provided rectangle
	 * 
	 * @param rectangle
	 *            rectangle to check for containment
	 * @return true if provided rectangle is contained by this rectangle, false
	 *         otherwise
	 */
	public boolean contains(@NonNull Rectangle rectangle) {
		return Arrays.stream(Side.values()).map(side -> {
			LineSegment container = getSide(side);
			LineSegment containee = rectangle.getSide(side);

			switch (side.getAxis()) {
			case X:
				return (containee.lowerBound.x >= container.lowerBound.x
						&& containee.upperBound.x <= container.upperBound.x);
			case Y:
				return (containee.lowerBound.y >= container.lowerBound.y
						&& containee.upperBound.y <= container.upperBound.y);
			default:
				throw new UnknownError("unknown axis");
			}

		}).reduce((result, contains) -> {
			return result && contains;
		}).get();
	}

	/**
	 * Returns true if the edge of this rectangle contains the provided line
	 * segment
	 * 
	 * @param segment
	 *            line segment to check
	 * @return true if the edge of this rectangle contains the provided line
	 *         segment
	 */
	public boolean onEdge(@NonNull LineSegment segment) {
		return Arrays.stream(Side.values()).map(side -> {
			return getSide(side).contains(segment);
		}).reduce((result, contains) -> {
			return result || contains;
		}).get();
	}

	/**
	 * Returns true if this rectangle contains the provided point
	 * 
	 * @param point
	 *            point to evaluate
	 * @return true if the point is within the boundaries of this rectangle
	 */
	public boolean contains(@NonNull Point point) {
		return (point.x >= top().lowerBound.x && point.x <= top().upperBound.x && point.y >= left().lowerBound.y
				&& point.y <= left().upperBound.y);
	}

	@Override
	public String toString() {
		return String.format("(%d %d, %d %d)", bottom().lowerBound.x, bottom().lowerBound.y, top().upperBound.x,
				top().upperBound.y);
	}
}
