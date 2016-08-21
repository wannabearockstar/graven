package com.wannabe.graven.editor;

import com.intellij.ide.ClipboardSynchronizer;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import com.wannabe.graven.domain.Engine;

import java.awt.datatransfer.StringSelection;

import static com.wannabe.graven.domain.Engine.GRADLE;
import static com.wannabe.graven.domain.Engine.MAVEN;

public class RewrittenPasteTest extends LightPlatformCodeInsightFixtureTestCase {

	public void testMavenToGradle() {
		doTest(MAVEN, GRADLE);
	}

	public void testMavenToGradleWithoutVersion() {
		doTest(MAVEN, GRADLE);
	}

	public void testGradleToMaven() {
		doTest(GRADLE, MAVEN);
	}

	public void testGradleToMavenWithoutVersion() {
		doTest(GRADLE, MAVEN);
	}

	public void testMavenToGradleWithoutScope() {
		doTest(MAVEN, GRADLE);
	}

	public void testMavenToGradleMultiplieDependencies() {
		doTest(MAVEN, GRADLE);
	}

	public void testGradleToMavenMultiplieDependencies() {
		doTest(GRADLE, MAVEN);
	}

	public void testGradleToMavenInvalid() {
		doTest(GRADLE, MAVEN);
	}

	public void testMavenToGradleInvalid() {
		doTest(MAVEN, GRADLE);
	}

	private void doTest(Engine source, Engine target) {
		myFixture.configureByFile(getTestName(true) + "." + getExtension(source));
		StringSelection input = new StringSelection(myFixture.getFile().getText());

		myFixture.addFileToProject(target.getFileName(), " ");
		myFixture.configureByFile(target.getFileName());

		ClipboardSynchronizer.getInstance().setContent(input, input);
		myFixture.performEditorAction(IdeActions.ACTION_EDITOR_PASTE);

		checkByContentWithoutTabs(myFixture, getTestName(true) + "." + getExtension(target));
	}

	private String getExtension(Engine e) {
		return e.getFileName().split("\\.")[1];
	}

	@Override
	protected String getTestDataPath() {
		return "testData/editor/rewrittenPaste/mavenToGradle";
	}

	public void checkByContentWithoutTabs(CodeInsightTestFixture fixture, String filename) {
		String current = fixture.getFile().getText().trim().replaceAll("(\\r|\\n|\\s|\\t)+", "");
		fixture.configureByFile(filename);
		String compared = fixture.getFile().getText().trim().replaceAll("(\\r|\\n|\\s|\\t)+", "");

		assertEquals(current, compared);
	}
}
