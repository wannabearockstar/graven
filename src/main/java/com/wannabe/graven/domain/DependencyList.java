package com.wannabe.graven.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

	public static class Dependency {

		@NotNull
		private final String group;
		@NotNull
		private final String artifactId;
		@Nullable
		private final String version;
		@Nullable
		private final String type;
		@NotNull
		private final String scope;
		@NotNull
		private final Engine engine;

		Dependency(@NotNull String group,
							 @NotNull String artifactId,
							 @Nullable String version,
							 @Nullable String scope,
							 @Nullable String type,
							 @NotNull Engine engine) {
			this.group = requireNonNull(group);
			this.artifactId = requireNonNull(artifactId);
			this.version = version;
			this.scope = scope == null ? "compile" : scope;
			this.type = type;
			this.engine = requireNonNull(engine);
		}

		Dependency(@NotNull String group,
							 @NotNull String artifactId,
							 @NotNull Engine engine) {
			this(group, artifactId, null, null, null, engine);
		}

		@NotNull
		public String getGroup() {
			return group;
		}

		@NotNull
		public String getArtifactId() {
			return artifactId;
		}

		@Nullable
		public String getVersion() {
			return version;
		}

		@NotNull
		public String getScope() {
			return scope;
		}

		@Nullable
		public String getType() {
			return type;
		}

		@NotNull
		public Engine getEngine() {
			return engine;
		}


		@Override
		public String toString() {
			return engine.toString(this);
		}

		public Dependency copy(Engine newEngine) {
			return new Dependency(group, artifactId, version, scope, type, newEngine);
		}

		public static Dependency gradle(@NotNull String group,
																		@NotNull String artifactId,
																		@Nullable String version,
																		@Nullable String scope,
																		@Nullable String type) {
			return new Dependency(group, artifactId, version, scope, type, Engine.GRADLE);
		}

		public static Dependency maven(@NotNull String group,
																	 @NotNull String artifactId,
																	 @Nullable String version,
																	 @Nullable String scope,
																	 @Nullable String type) {
			return new Dependency(group, artifactId, version, scope, type, Engine.MAVEN);
		}

		public static Dependency none() {
			return new Dependency("", "", "", "", "", Engine.NONE);
		}
	}
}
