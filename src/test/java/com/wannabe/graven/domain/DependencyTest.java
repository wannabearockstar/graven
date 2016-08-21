package com.wannabe.graven.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class DependencyTest {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void toStringTestMaven() throws Exception {
		DependencyList.Dependency dependency = new DependencyList.Dependency("group", "artifact", "version", "scope", "type", Engine.MAVEN);
		String expected = "<dependency>\n" +
			"\t<groupId>group</groupId>\n" +
			"\t<artifactId>artifact</artifactId>\n" +
			"\t<version>version</version>\n" +
			"\t<scope>scope</scope>\n" +
			"\t<type>type</type>\n" +
			"</dependency>";
		assertThat(dependency.toString(), is(expected));

		DependencyList.Dependency dependencyWithoutSomeTags = new DependencyList.Dependency("group", "artifact", Engine.MAVEN);
		expected = "<dependency>\n" +
			"\t<groupId>group</groupId>\n" +
			"\t<artifactId>artifact</artifactId>\n" +
			"\t<scope>compile</scope>\n" +
			"</dependency>";
		assertThat(dependencyWithoutSomeTags.toString(), is(expected));
	}

	@Test
	public void toStringTestGradle() throws Exception {
		DependencyList.Dependency dependency = new DependencyList.Dependency("group", "artifact", "version", "compile", "type", Engine.GRADLE);
		String expected = "compile 'group:artifact:version'";
		assertThat(dependency.toString(), is(expected));

		DependencyList.Dependency dependencyWithoutSomeTags = new DependencyList.Dependency("group", "artifact", null, "compile", null,  Engine.GRADLE);
		expected = "compile 'group:artifact'";
		assertThat(dependencyWithoutSomeTags.toString(), is(expected));
	}

	@Test
	public void toStringTestMavenMultiplie() throws Exception {
		DependencyList.Dependency dependency = new DependencyList.Dependency("group", "artifact", "version", "scope", "type", Engine.MAVEN);
		DependencyList.Dependency dependency1 = new DependencyList.Dependency("group1", "artifact1", "version1", null, null, Engine.MAVEN);
		DependencyList dependencyList = new DependencyList(Engine.MAVEN, Arrays.asList(dependency, dependency1));
		String expected = "<dependency>\n" +
			"\t<groupId>group</groupId>\n" +
			"\t<artifactId>artifact</artifactId>\n" +
			"\t<version>version</version>\n" +
			"\t<scope>scope</scope>\n" +
			"\t<type>type</type>\n" +
			"</dependency>\n" +
			"<dependency>\n" +
			"\t<groupId>group1</groupId>\n" +
			"\t<artifactId>artifact1</artifactId>\n" +
			"\t<version>version1</version>\n" +
			"\t<scope>compile</scope>\n" +
			"</dependency>";
		assertThat(dependencyList.toString(), is(expected));
	}

	@Test
	public void toStringTestGradleMultiplie() throws Exception {
		DependencyList.Dependency dependency = new DependencyList.Dependency("group", "artifact", "version", "scope", "type", Engine.GRADLE);
		DependencyList.Dependency dependency1 = new DependencyList.Dependency("group1", "artifact1", "version1", null, null, Engine.GRADLE);
		DependencyList dependencyList = new DependencyList(Engine.GRADLE, Arrays.asList(dependency, dependency1));
		String expected = "scope 'group:artifact:version'\n" +
			"compile 'group1:artifact1:version1'";
		assertThat(dependencyList.toString(), is(expected));
	}

	@Test
	public void testCopy() throws Exception {
		DependencyList.Dependency dependency = new DependencyList.Dependency("group", "artifact", "version", "compile", "type", Engine.GRADLE);
		DependencyList.Dependency copy = dependency.copy(Engine.MAVEN);

		assertThat(copy.getEngine(), is(Engine.MAVEN));
		assertThat(copy.getGroup(), is(dependency.getGroup()));
		assertThat(copy.getArtifactId(), is(dependency.getArtifactId()));
		assertThat(copy.getVersion(), is(dependency.getVersion()));
		assertThat(copy.getScope(), is(dependency.getScope()));
		assertThat(copy.getType(), is(dependency.getType()));
	}
}