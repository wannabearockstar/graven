package com.wannabe.graven.processor;

import com.intellij.openapi.diagnostic.Logger;
import com.wannabe.graven.GravenCopyPastePreProcessor;
import com.wannabe.graven.domain.DependencyList;
import com.wannabe.graven.domain.Engine;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;

import static com.wannabe.graven.domain.DependencyList.Dependency.maven;
import static com.wannabe.graven.domain.DependencyList.empty;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public class MavenDependencyProcessor implements DependencyProcessor {
	private static final Logger log = Logger.getInstance(MavenDependencyProcessor.class);

	@Override
	@NotNull
	public DependencyList extractDependency(@NotNull String text) {
		try {
			Document document = loadXMLFromString(prepare(text));
			if (!isValid(document)) {
				return empty();
			}
			NodeList dependencies = document.getElementsByTagName("dependency");
			List<DependencyList.Dependency> parsed = range(0, dependencies.getLength()).boxed().map(dependencies::item)
				.map(e -> getDependency((Element) e))
				.collect(toList());
			return new DependencyList(Engine.MAVEN, parsed);
		} catch (Exception e) {
			log.warn(e);
			return empty();
		}
	}

	private DependencyList.Dependency getDependency(Element document) {
		String groupId = document.getElementsByTagName("groupId").item(0).getTextContent();
		String artifactId = document.getElementsByTagName("artifactId").item(0).getTextContent();

		String version = getTextByTagName(document, "version");
		String scope = getTextByTagName(document, "scope");
		String type = getTextByTagName(document, "type");

		return maven(groupId, artifactId, version, scope, type);
	}

	private String getTextByTagName(Element document, String tagName) {
		NodeList versionTag = document.getElementsByTagName(tagName);
		return Optional.ofNullable(versionTag)
			.map(v -> v.item(0))
			.map(Node::getTextContent)
			.orElse(null);
	}

	private String prepare(String s) {
		String trimmed = s.trim().replaceAll("(\\r|\\n|\\s|\\t)+", "");
		return format("<root>%s</root>", trimmed);
	}

	private boolean isValid(Document document) {
		return getTextByTagName(document.getDocumentElement(), "groupId") != null &&
			getTextByTagName(document.getDocumentElement(), "artifactId") != null;
	}

	private static Document loadXMLFromString(String xml) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		return builder.parse(is);
	}
}
