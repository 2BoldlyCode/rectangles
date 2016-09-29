package com.apprenda.rectangles.rest;

import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.apprenda.rectangles.RectanglesAnalyzer;
import com.apprenda.rectangles.model.AnalysisResult;
import com.apprenda.rectangles.model.Point;
import com.apprenda.rectangles.model.Rectangle;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RectanglesController {
	
	private final RectanglesAnalyzer analyzer = new RectanglesAnalyzer();

	@RequestMapping(value = "/analyze/{r1Coords}/{r2Coords}", method = RequestMethod.GET)
	public ResponseEntity<Resource<AnalysisResult>> analyze(@PathVariable String r1Coords, @PathVariable String r2Coords) {
		log.info("rectangles: {} {}", r1Coords, r2Coords);
		
		Rectangle r1 = toRectangle(r1Coords);
		Rectangle r2 = toRectangle(r2Coords);
		
		AnalysisResult result = analyzer.analyze(r1, r2);
		
		return ResponseEntity.ok(new Resource<AnalysisResult>(result));
	}
	
	private Rectangle toRectangle(String coordString) {
		String[] coords = coordString.split(",");
		
		long x1 = Long.parseLong(coords[0]);
		long y1 = Long.parseLong(coords[1]);
		long x2 = Long.parseLong(coords[2]);
		long y2 = Long.parseLong(coords[3]);
		
		return new Rectangle(new Point(x1,y1), new Point(x2,y2));
	}
}
