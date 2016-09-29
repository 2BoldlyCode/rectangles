package com.apprenda.rectangles.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.apprenda.rectangles.model.AnalysisResult;

public class RectanglesTextWriter implements RectanglesWriter {
	
	final BufferedWriter writer;
	
	public RectanglesTextWriter(OutputStream out) {
		this.writer = new BufferedWriter(new OutputStreamWriter(out));
	}

	@Override
	public void write(AnalysisResult result) throws IOException {
		writer.write(result.toString() + "\n");
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}
	
}

