package com.wannabe.graven.editor;

import com.intellij.ide.ClipboardSynchronizer;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import com.wannabe.graven.domain.Engine;

import java.awt.datatransfer.StringSelection;

public class RewrittenPasteTest extends LightPlatformCodeInsightFixtureTestCase {

	public void testMavenToGradle() {
		doTest(Engine.MAVEN, Engine.GRADLE);
	}

	public void testMavenToGradleWithoutVersion() {
		doTest(Engine.MAVEN, Engine.GRADLE);
	}

	public void testGradleToMaven() {
		doTest(Engine.GRADLE, Engine.MAVEN);
	}

	public void testGradleToMavenWithoutVersion() {
		doTest(Engine.GRADLE, Engine.MAVEN);
	}

	public void testMavenToGradleWithoutScope() {
		doTest(Engine.MAVEN, Engine.GRADLE);
	}

	public void testMavenToGradleMultiplieDependencies() {
		doTest(Engine.MAVEN, Engine.GRADLE);
	}

	public void testGradleToMavenMultiplieDependencies() {
		doTest(Engine.GRADLE, Engine.MAVEN);
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
