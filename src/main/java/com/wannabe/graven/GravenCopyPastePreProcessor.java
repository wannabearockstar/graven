package com.wannabe.graven;

import com.intellij.codeInsight.editorActions.CopyPastePreProcessor;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RawText;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiFile;
import com.wannabe.graven.domain.DependencyList;
import com.wannabe.graven.domain.Engine;
import com.wannabe.graven.domain.ui.EditorData;
import com.wannabe.graven.services.GravenService;
import com.wannabe.graven.ui.ConfirmDialog;
import com.wannabe.graven.ui.GravenSettingsUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.groovy.GroovyFileType;

import java.util.HashMap;
import java.util.Map;

public class GravenCopyPastePreProcessor implements CopyPastePreProcessor {

	private static final Logger log = Logger.getInstance(GravenCopyPastePreProcessor.class);
	private final Map<Engine, FileType> engineFileTyp = new HashMap<>();

	public GravenCopyPastePreProcessor() {
		engineFileTyp.put(Engine.MAVEN, XmlFileType.INSTANCE);
		engineFileTyp.put(Engine.GRADLE, GroovyFileType.GROOVY_FILE_TYPE);
	}

	@Nullable
	@Override
	public String preprocessOnCopy(PsiFile file, int[] startOffsets, int[] endOffsets, String text) {
		return null;
	}

	@NotNull
	@Override
	public String preprocessOnPaste(Project project, PsiFile file, Editor editor, String text, RawText rawText) {
		try {
			GravenService gravenService = GravenService.getInstance();
			Pair<DependencyList, DependencyList> rewritten = gravenService.tryToRewrite(text, file.getName());
			if (hasNull(rewritten)) {
				return text;
			}
			if (!GravenSettingsUI.isConfirmDialogShow()) {
				return rewritten.second.toString();
			}

			ConfirmDialog confirmDialog = new ConfirmDialog(project, getEditorData(rewritten.first), getEditorData(rewritten.second));
			boolean isAccepted = confirmDialog.showAndGet();
			if (isAccepted) {
				return confirmDialog.getRewrittenValue();
			}
		} catch (Exception e) {
			log.warn(e);
			return text;
		}
		return text;
	}


	private EditorData getEditorData(DependencyList dependencies) {
		return new EditorData(
			dependencies.toString(),
			engineFileTyp.getOrDefault(dependencies.getEngine(), PlainTextFileType.INSTANCE)
		);
	}

	private boolean hasNull(Pair<?, ?> pair) {
		return pair.first == null || pair.second == null;
	}
}
