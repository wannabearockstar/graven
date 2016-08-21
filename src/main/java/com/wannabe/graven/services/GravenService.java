package com.wannabe.graven.services;

import com.intellij.openapi.components.ServiceManager;
import com.wannabe.graven.domain.DependencyList;
import com.wannabe.graven.domain.Engine;
import com.wannabe.graven.processor.DependencyProcessor;
import com.wannabe.graven.processor.GradleDependencyProcessor;
import com.wannabe.graven.processor.MavenDependencyProcessor;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class GravenService {

	private Set<DependencyProcessor> processors = new HashSet<>();

	public GravenService() {
		processors.add(ServiceManager.getService(MavenDependencyProcessor.class));
		processors.add(ServiceManager.getService(GradleDependencyProcessor.class));
	}

	public static GravenService getInstance() {
		return ServiceManager.getService(GravenService.class);
	}

	public String tryToRewrite(String text, String filename) {
		boolean validFilename = stream(Engine.values()).anyMatch(e -> e.getFileName().equals(filename));
		if (!validFilename) {
			return text;
		}
		DependencyList dependency = lookupDependency(text);
		if (dependency == null || dependency.getEngine().equals(Engine.ofFilename(filename))) {
			return text;
		}
		return dependency.copy(Engine.ofFilename(filename)).toString();
	}

	private DependencyList lookupDependency(String text) {
		return processors.stream()
			.map(d -> d.extractDependency(text))
			.filter(d -> d.getEngine() != Engine.NONE)
			.filter(d -> !d.isEmpty())
			.findFirst().orElseGet(() -> null);
	}
}
