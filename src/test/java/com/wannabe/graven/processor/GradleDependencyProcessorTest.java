package com.wannabe.graven.processor;

import com.wannabe.graven.domain.DependencyList;
import com.wannabe.graven.domain.Engine;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class GradleDependencyProcessorTest {
	private GradleDependencyProcessor processor;

	@Before
	public void setUp() throws Exception {
		processor = new GradleDependencyProcessor();
	}

	@Test
	public void extractDependency() throws Exception {
		String input = "runtime 'group:artifact:version'";

		DependencyList dependency = processor.extractDependency(input);

		assertThat(dependency.getEngine(), is(Engine.GRADLE));
		assertThat(dependency.get(0).getGroup(), is("group"));
		assertThat(dependency.get(0).getArtifactId(), is("artifact"));
		assertThat(dependency.get(0).getVersion(), is("version"));
		assertThat(dependency.get(0).getScope(), is("runtime"));
		assertThat(dependency.get(0).getType(), nullValue());
	}

	@Test
	public void extractDependenctMissingFields() throws Exception {
		String input = "compile 'group1:artifact1'";

		DependencyList dependency = processor.extractDependency(input);

		assertThat(dependency.getEngine(), is(Engine.GRADLE));
		assertThat(dependency.get(0).getGroup(), is("group1"));
		assertThat(dependency.get(0).getArtifactId(), is("artifact1"));
		assertThat(dependency.get(0).getVersion(), nullValue());
		assertThat(dependency.get(0).getScope(), is("compile"));
		assertThat(dependency.get(0).getType(), nullValue());
	}

	@Test
	public void extractMultiplieDependencies() throws Exception {
		String input = "compile 'group:artifact:version'\n" +
			"runtime 'group1:artifact1'";
		DependencyList dependency = processor.extractDependency(input);

		assertThat(dependency.get(0).getEngine(), is(Engine.GRADLE));
		assertThat(dependency.get(0).getScope(), is("compile"));
		assertThat(dependency.get(0).getGroup(), is("group"));
		assertThat(dependency.get(0).getArtifactId(), is("artifact"));
		assertThat(dependency.get(0).getVersion(), is("version"));

		assertThat(dependency.get(1).getEngine(), is(Engine.GRADLE));
		assertThat(dependency.get(1).getScope(), is("runtime"));
		assertThat(dependency.get(1).getGroup(), is("group1"));
		assertThat(dependency.get(1).getArtifactId(), is("artifact1"));
		assertThat(dependency.get(1).getVersion(), nullValue());
	}

	@Test
	public void extractNonsense() throws Exception {
		String input = "asddasda";
		assertThat(processor.extractDependency(input).getEngine(), is(Engine.NONE));
	}
}