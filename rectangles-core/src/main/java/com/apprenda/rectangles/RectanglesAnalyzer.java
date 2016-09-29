package com.apprenda.rectangles;

import java.util.List;

import com.apprenda.rectangles.model.AnalysisResult;
import com.apprenda.rectangles.model.Rectangle;

import lombok.NonNull;

/**
 * Performs analysis of two rectangles
 * 
 * @author Cody McCain
 *
 */
public class RectanglesAnalyzer {

	public AnalysisResult analyze(@NonNull List<Rectangle> rectangles) {
		if (rectangles.size() != 2) {
			throw new IllegalArgumentException("this version only supports analyzing two rectangles at once");
		}

		return analyze(rectangles.get(0), rectangles.get(1));
	}

	public AnalysisResult analyze(Rectangle r1, Rectangle r2) {
		final AnalysisResult result = new AnalysisResult(r1, r2);

		result.setIntersects(r1.intersectsWith(r2));
		result.setAdjacent(r1.adjacentTo(r2));

		if (r1.contains(r2)) {
			result.setContainer(r1);
			result.setContainee(r2);
		} else if (r2.contains(r1)) {
			result.setContainer(r2);
			result.setContainee(r1);
		}
		
		result.setOverlap(r1.overlappingAreaOf(r2));

		return result;
	}

}
