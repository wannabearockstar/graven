package com.wannabe.graven.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.Arrays.stream;

public enum Engine {
	MAVEN(dependency -> {

		StringBuilder sb = new StringBuilder();

		String pattern = "<dependency>\n" +
			"\t<groupId>%s</groupId>\n" +
			"\t<artifactId>%s</artifactId>";
		sb.append(String.format(pattern, dependency.getGroup(), dependency.getArtifactId()));
		if (dependency.getVersion() != null) {
			sb.append(String.format("\n\t<version>%s</version>", dependency.getVersion()));
		}
		sb.append(String.format("\n\t<scope>%s</scope>", dependency.getScope()));

		if (dependency.getType() != null) {
			sb.append(String.format("\n\t<type>%s</type>", dependency.getType()));
		}
		sb.append("\n</dependency>");
		return sb.toString();

	}, "pom.xml"),

	GRADLE(dependency -> {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s '%s:%s", dependency.getScope(), dependency.getGroup(), dependency.getArtifactId()));
		if (dependency.getVersion() != null) {
			sb.append(String.format(":%s", dependency.getVersion()));
		}
		sb.append("'");
		return sb.toString();
	}, "build.gradle"),

	NONE(dependency -> "", "");

	private final Function<Dependency, String> writer;
	private final String fileName;
	private static final Map<String, Engine> filenames;

	static {
		filenames = new HashMap<>();
		stream(values()).forEach(e -> filenames.put(e.fileName, e));
	}

	Engine(Function<Dependency, String> writer, String fileName) {
		this.writer = writer;
		this.fileName = fileName;
	}

	public static Engine ofFilename(String fileName) {
		return filenames.getOrDefault(fileName, NONE);
	}


	public String toString(Dependency dependency) {
		return writer.apply(dependency);
	}

	public String getFileName() {
		return fileName;
	}
}
