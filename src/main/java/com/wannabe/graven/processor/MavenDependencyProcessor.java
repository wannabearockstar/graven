package com.wannabe.graven.processor;

import com.wannabe.graven.domain.Dependency;
import com.wannabe.graven.domain.DependencyList;
import com.wannabe.graven.domain.Engine;
import org.apache.xerces.dom.DeferredElementImpl;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

import static com.wannabe.graven.domain.Dependency.maven;
import static java.lang.String.format;
import static java.util.stream.IntStream.range;

public class MavenDependencyProcessor implements DependencyProcessor {

	@Override
	public DependencyList extractDependency(String text) {
		try {
			Document document = loadXMLFromString(prepare(text));
			if (!isValid(document)) {
				return DependencyList.empty();
			}
			NodeList dependencies = document.getElementsByTagName("dependency");
			List<Dependency> parsed = range(0, dependencies.getLength()).boxed().map(dependencies::item)
				.map(e -> getDependency((Element) e))
				.collect(Collectors.toList());
			return new DependencyList(Engine.MAVEN, parsed);
		} catch (Exception e) {
			return DependencyList.empty();
		}
	}

	@NotNull
	private Dependency getDependency(Element document) {
		String groupId = document.getElementsByTagName("groupId").item(0).getTextContent();
		String artifactId = document.getElementsByTagName("artifactId").item(0).getTextContent();

		NodeList versionTag = document.getElementsByTagName("version");
		String version = versionTag != null && versionTag.item(0) != null ? versionTag.item(0).getTextContent() : null;

		NodeList scopeTag = document.getElementsByTagName("scope");
		String scope = scopeTag != null && scopeTag.item(0) != null ? scopeTag.item(0).getTextContent() : null;

		NodeList typeTag = document.getElementsByTagName("type");
		String type = typeTag != null && typeTag.item(0) != null ? typeTag.item(0).getTextContent() : null;

		return maven(groupId, artifactId, version, scope, type);
	}

	private String prepare(String s) {
		String trimmed = s.trim().replaceAll("(\\r|\\n|\\s|\\t)+", "");
		return format("<root>%s</root>", trimmed);
	}

	private boolean isValid(Document document) {
		return document.getElementsByTagName("groupId") != null &&
			document.getElementsByTagName("groupId").item(0) != null &&
			document.getElementsByTagName("artifactId") != null &&
			document.getElementsByTagName("artifactId").item(0) != null;
	}

	private static Document loadXMLFromString(String xml) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		return builder.parse(is);
	}
}
