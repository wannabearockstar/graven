package com.wannabe.graven.processor;

import com.wannabe.graven.domain.Dependency;
import com.wannabe.graven.domain.DependencyList;
import com.wannabe.graven.domain.Engine;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.wannabe.graven.domain.Dependency.gradle;
import static com.wannabe.graven.domain.Dependency.none;
import static java.util.Arrays.stream;
import static java.util.Collections.singletonList;

public class GradleDependencyProcessor implements DependencyProcessor {

	private final Pattern fullPattern = Pattern.compile("(\\S+)'(\\S+):(\\S+):(\\S+)'");
	private final Pattern partPattern = Pattern.compile("(\\S+)'(\\S+):(\\S+)'");

	@Override
	public DependencyList extractDependency(String text) {
		List<Dependency> dependencies = stream(text.split("\n"))
			.map(this::getDependency)
			.filter(d -> !d.getEngine().equals(Engine.NONE))
			.collect(Collectors.toList());
		if (dependencies.isEmpty()) {
			return DependencyList.empty();
		}
		return new DependencyList(Engine.GRADLE, dependencies);
	}

	@NotNull
	private Dependency getDependency(String text) {
		String trimmed = text.trim().replaceAll("(\\s|\\t)+", "");
		Matcher matcher = fullPattern.matcher(trimmed);
		if (matcher.find()) {
			String scope = matcher.group(1);
			String group = matcher.group(2);
			String artifact = matcher.group(3);
			String version = matcher.group(4);

			return gradle(group, artifact, version, scope, null);
		} else {
			matcher = partPattern.matcher(trimmed);
			if (matcher.find()) {
				String scope = matcher.group(1);
				String group = matcher.group(2);
				String artifact = matcher.group(3);

				return gradle(group, artifact, null, scope, null);
			}
		}

		return Dependency.none();
	}
}
