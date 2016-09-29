package com.apprenda.rectangles.model;

/**
 * Axis of 2D plane
 * 
 * @author Cody McCain
 *
 */
public enum Axis {
	X, Y;

	/**
	 * Returns true if the axis are orthogonal
	 * 
	 * @param axis
	 *            axis to compare
	 * @return true if orthogonal, false otherwise
	 */
	public boolean isOrthogonalTo(Axis axis) {
		return (this != axis);
	}
	
	public Axis orthogonal() {
		if (this == Y) {
			return X;
		} else {
			return Y;
		}
	}
}
