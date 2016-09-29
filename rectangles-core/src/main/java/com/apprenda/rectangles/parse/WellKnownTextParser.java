package com.apprenda.rectangles.parse;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.apprenda.rectangles.model.Point;
import com.apprenda.rectangles.model.Rectangle;

import lombok.extern.slf4j.Slf4j;

/**
 * Parses a modified form of the well-known text format.
 * 
 * Rectangle pairs are represented as:
 * 
 * <pre>
 *   (x1 y1, x2 y2) (x3 y3, x4 y4)
 * </pre>
 * 
 * @author Cody McCain
 *
 */
@Slf4j
public class WellKnownTextParser implements Closeable {

	static final Pattern RECTANGLE_PATTERN = Pattern
			.compile("\\((?<x1>\\d+)\\s+(?<y1>\\d+),\\s+(?<x2>\\d+)\\s+(?<y2>\\d+)\\)");

	final LineNumberReader reader;

	public WellKnownTextParser(InputStream stream) {
		this.reader = new LineNumberReader(new InputStreamReader(stream));
	}

	Set<Rectangle> readline() throws IOException {
		Set<Rectangle> rectangles = new LinkedHashSet<>();

		Matcher matcher = RECTANGLE_PATTERN.matcher(reader.readLine());

		while (matcher.find()) {
			Rectangle rectangle = new Rectangle(parsePoint(matcher, 1), parsePoint(matcher, 2));
			log.debug("parsed rectangle: {}", rectangle);
			rectangles.add(rectangle);
		}

		return rectangles;
	}

	Point parsePoint(Matcher matcher, int group) {
		return new Point(Long.parseLong(matcher.group("x" + group)), Long.parseLong(matcher.group("y" + group)));
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}
}
