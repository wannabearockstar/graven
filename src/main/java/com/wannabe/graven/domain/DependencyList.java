package com.wannabe.graven.domain;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

public class DependencyList {

	@NotNull
	private final Engine engine;
	@NotNull
	private final List<Dependency> dependencies;

	public DependencyList(@NotNull Engine engine, List<Dependency> dependencies) {
		this.engine = requireNonNull(engine);
		this.dependencies = requireNonNull(dependencies);
	}

	@NotNull
	public Engine getEngine() {
		return engine;
	}

	public boolean isEmpty() {
		return dependencies.isEmpty();
	}

	public static DependencyList empty() {
		return new DependencyList(Engine.NONE, emptyList());
	}

	public DependencyList copy(Engine engine) {
		return new DependencyList(engine, new ArrayList<>(dependencies.stream().map(d -> d.copy(engine)).collect(Collectors.toList())));
	}

	public Dependency get(int index) {
		return dependencies.get(index);
	}

	@Override
	public String toString() {
		return dependencies.stream()
			.map(Dependency::toString)
			.collect(joining("\n"));
	}
}
