package com.apprenda.rectangles;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:git.properties")
@Component
public class Version {

	@Value("${git.commit.id.describe-short}")
	String gitDescribeShort;
	
	@Value("${git.build.version}")
	String buildVersion;
	
	@Override
	public String toString() {
		return String.format("Version %s %s", buildVersion, gitDescribeShort);
	}
}
