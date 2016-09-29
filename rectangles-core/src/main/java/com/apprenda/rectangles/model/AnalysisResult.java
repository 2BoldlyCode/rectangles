package com.apprenda.rectangles.model;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

/**
 * Represents the results of analyzing two rectangles.
 * 
 * @author Cody McCain
 *
 */
@Data
public class AnalysisResult {
	final Rectangle r1;
	final Rectangle r2;

	boolean intersects;
	boolean adjacent;
	Rectangle container;
	Rectangle containee;
	Rectangle overlap;

	public AnalysisResult(Rectangle r1, Rectangle r2) {
		this.r1 = r1;
		this.r2 = r2;
	}

	@Override
	public String toString() {
		List<String> results = new LinkedList<>();

		results.add(r1.toString());
		results.add(r2.toString());
		results.add("\t--");

		if (intersects) {
			results.add("INTERSECTS");
		} else {
			results.add("SEPARATED");
		}

		if (adjacent) {
			results.add("ADJACENT");
		}

		if (container != null) {
			results.add(String.format("%s CONTAINS %s", container, containee));
		}

		if (overlap != null) {
			results.add(String.format("OVERLAP %s", overlap));
		}

		return results.stream().collect(Collectors.joining(" "));
	}
}
