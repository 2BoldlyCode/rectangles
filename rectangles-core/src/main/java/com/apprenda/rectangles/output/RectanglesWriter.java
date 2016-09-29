package com.apprenda.rectangles.output;

import java.io.Closeable;
import java.io.IOException;

import com.apprenda.rectangles.model.AnalysisResult;

public interface RectanglesWriter extends Closeable {
	void write(AnalysisResult result) throws IOException;
}
