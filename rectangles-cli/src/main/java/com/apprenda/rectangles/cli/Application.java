package com.apprenda.rectangles.cli;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.apprenda.rectangles.RectanglesAnalyzer;
import com.apprenda.rectangles.Version;
import com.apprenda.rectangles.model.AnalysisResult;
import com.apprenda.rectangles.model.Rectangle;
import com.apprenda.rectangles.output.RectanglesTextWriter;
import com.apprenda.rectangles.parse.WellKnownTextParser;

@SpringBootApplication
@ComponentScan(basePackageClasses = Version.class)
public class Application implements CommandLineRunner {
	
	@Autowired
	private Version version;
	
	public static void main(String[] args) throws Exception {
		SpringApplication app = new SpringApplication(Application.class);
		app.setHeadless(true);
		app.setLogStartupInfo(false);
		app.setShowBanner(true);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(version.toString());
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
