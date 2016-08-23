package com.wannabe.graven.services;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.util.Pair;
import com.wannabe.graven.domain.DependencyList;
import com.wannabe.graven.domain.Engine;
import com.wannabe.graven.processor.DependencyProcessor;
import com.wannabe.graven.processor.GradleDependencyProcessor;
import com.wannabe.graven.processor.MavenDependencyProcessor;

import java.util.HashSet;
import java.util.Set;

import static com.intellij.openapi.util.Pair.*;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.reducing;

public class GravenService {

	private Set<DependencyProcessor> processors = new HashSet<>();

	public GravenService() {
		processors.add(ServiceManager.getService(MavenDependencyProcessor.class));
		processors.add(ServiceManager.getService(GradleDependencyProcessor.class));
	}

	public static GravenService getInstance() {
		return ServiceManager.getService(GravenService.class);
	}

	public Pair<DependencyList, DependencyList> tryToRewrite(String text, String filename) {
		boolean validFilename = stream(Engine.values()).anyMatch(e -> e.getFileName().equals(filename));
		if (!validFilename) {
			return empty();
		}
		DependencyList dependency = lookupDependency(text);
		if (dependency == null || dependency.getEngine().equals(Engine.ofFilename(filename))) {
			return empty();
		}
		return create(dependency, dependency.copy(Engine.ofFilename(filename)));
	}

	private DependencyList lookupDependency(String text) {
		return processors.stream()
			.map(d -> d.extractDependency(text))
			.filter(d -> d.getEngine() != Engine.NONE)
			.filter(d -> !d.isEmpty())
			.findFirst().orElseGet(() -> null);
	}
}
