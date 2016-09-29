package com.apprenda.rectangles.model;

import static com.apprenda.rectangles.model.Axis.X;
import static com.apprenda.rectangles.model.Axis.Y;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public enum Side {
	TOP(X), LEFT(Y), BOTTOM(X), RIGHT(Y);

	private final Axis axis;
	private final static Map<Axis, Set<Side>> axisToSides;

	static {
		HashMap<Axis, Set<Side>> map = new HashMap<>(2);
		Arrays.stream(Axis.values()).forEach(axis -> {
			map.put(axis, Arrays.stream(values()).filter(s -> s.axis == axis).collect(Collectors.toSet()));
		});
		axisToSides = Collections.unmodifiableMap(map);
	}

	private Side(Axis alignment) {
		this.axis = alignment;
	}

	public static Set<Side> sidesAlignedTo(Axis axis) {
		return axisToSides.get(axis);
	}

	/**
	 * Return the side parallel to this side
	 * 
	 * @return parallel side
	 */
	public Side parallel() {
		return axisToSides.get(axis).stream().filter(s -> s != this).findFirst().get();
	}

	/**
	 * Return sides orthogonal (aligned to other axis) to this side
	 *
	 * @return orthogonal sides
	 */
	public Set<Side> orthogonal() {
		return axisToSides.get(axis.orthogonal());
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", name(), axis.name());
	}
}
