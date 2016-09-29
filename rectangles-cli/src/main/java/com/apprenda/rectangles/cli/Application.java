package com.apprenda.rectangles.cli;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.apprenda.rectangles.RectanglesAnalyzer;
import com.apprenda.rectangles.model.AnalysisResult;
import com.apprenda.rectangles.model.Rectangle;
import com.apprenda.rectangles.output.RectanglesTextWriter;
import com.apprenda.rectangles.parse.WellKnownTextParser;

@SpringBootApplication
public class Application implements CommandLineRunner {

	public static void main(String[] args) throws Exception {
		SpringApplication app = new SpringApplication(Application.class);
		app.setHeadless(true);
		app.setLogStartupInfo(false);
		app.setShowBanner(true);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		final WellKnownTextParser parser = new WellKnownTextParser(System.in);
		final RectanglesTextWriter writer = new RectanglesTextWriter(System.out);

		final RectanglesAnalyzer analyzer = new RectanglesAnalyzer();

		try {
			List<Rectangle> rectangles;
			while ((rectangles = parser.readline()) != null) {
				AnalysisResult result = analyzer.analyze(rectangles);
				writer.write(result);
			}
		} finally {
			if (parser != null) {
				parser.close();
			}

			if (writer != null) {
				writer.close();
			}
		}
	}

}
