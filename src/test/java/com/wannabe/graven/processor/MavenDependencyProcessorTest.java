package com.wannabe.graven.processor;

import com.wannabe.graven.domain.Dependency;
import com.wannabe.graven.domain.DependencyList;
import com.wannabe.graven.domain.Engine;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class MavenDependencyProcessorTest {
	private MavenDependencyProcessor processor;

	@Before
	public void setUp() throws Exception {
		processor = new MavenDependencyProcessor();
	}

	@Test
	public void extractDependency() throws Exception {
		String input = "<dependency>\n" +
			"\t<groupId>group</groupId>\n" +
			"\t<artifactId>artifact</artifactId>\n" +
			"\t<version>version</version>\n" +
			"\t<scope>scope</scope>\n" +
			"\t<type>type</type>\n" +
			"</dependency>";

		DependencyList dependency = processor.extractDependency(input);

		assertThat(dependency.get(0).getEngine(), is(Engine.MAVEN));
		assertThat(dependency.get(0).getGroup(), is("group"));
		assertThat(dependency.get(0).getArtifactId(), is("artifact"));
		assertThat(dependency.get(0).getVersion(), is("version"));
		assertThat(dependency.get(0).getScope(), is("scope"));
		assertThat(dependency.get(0).getType(), is("type"));
	}

	@Test
	public void extractDependencyMissingFields() throws Exception {
		String input = "<dependency>\n" +
			"\t<groupId>group1</groupId>\n" +
			"\t<artifactId>artifact1</artifactId>\n" +
			"\t<type>type1</type>\n" +
			"</dependency>";

		DependencyList dependency = processor.extractDependency(input);

		assertThat(dependency.get(0).getEngine(), is(Engine.MAVEN));
		assertThat(dependency.get(0).getGroup(), is("group1"));
		assertThat(dependency.get(0).getArtifactId(), is("artifact1"));
		assertThat(dependency.get(0).getVersion(), nullValue());
		assertThat(dependency.get(0).getScope(), is("compile"));
		assertThat(dependency.get(0).getType(), is("type1"));
	}

	@Test
	public void extractDependencyMessingTabs() throws Exception {
		String input = "\n\t<dependency>" +
			"\t\t<groupId>group</groupId>\n\n\n" +
			"   <artifactId>artifact</artifactId>" +
			"<scope>scope1</scope>\n" +
			"<type>type</type>\n\n" +
			"</dependency>\n";

		DependencyList dependency = processor.extractDependency(input);

		assertThat(dependency.get(0).getEngine(), is(Engine.MAVEN));
		assertThat(dependency.get(0).getGroup(), is("group"));
		assertThat(dependency.get(0).getArtifactId(), is("artifact"));
		assertThat(dependency.get(0).getVersion(), nullValue());
		assertThat(dependency.get(0).getScope(), is("scope1"));
		assertThat(dependency.get(0).getType(), is("type"));
	}

	@Test
	public void extractMultiplieDependencies() throws Exception {
		String input = "<dependency>\n" +
			"\t<groupId>group1</groupId>\n" +
			"\t<artifactId>artifact1</artifactId>\n" +
			"\t<type>type1</type>\n" +
			"</dependency>\n" +
			"<dependency>\n" +
			"\t<groupId>group2</groupId>\n" +
			"\t<artifactId>artifact2</artifactId>\n" +
			"\t<version>version</version>\n" +
			"\t<scope>runtime</scope>\n" +
			"\t<type>type2</type>\n" +
			"\t</dependency>";

		DependencyList dependency = processor.extractDependency(input);

		assertThat(dependency.get(0).getEngine(), is(Engine.MAVEN));
		assertThat(dependency.get(0).getGroup(), is("group1"));
		assertThat(dependency.get(0).getArtifactId(), is("artifact1"));
		assertThat(dependency.get(0).getVersion(), nullValue());
		assertThat(dependency.get(0).getScope(), is("compile"));
		assertThat(dependency.get(0).getType(), is("type1"));

		assertThat(dependency.get(1).getGroup(), is("group2"));
		assertThat(dependency.get(1).getArtifactId(), is("artifact2"));
		assertThat(dependency.get(1).getVersion(), is("version"));
		assertThat(dependency.get(1).getScope(), is("runtime"));
		assertThat(dependency.get(1).getType(), is("type2"));
	}

	@Test
	public void extractNonsense() throws Exception {
		String input = "asddasda";
		assertThat(processor.extractDependency(input).getEngine(), is(Engine.NONE));
	}
}