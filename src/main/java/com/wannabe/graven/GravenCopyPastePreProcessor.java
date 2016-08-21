package com.wannabe.graven;

import com.intellij.codeInsight.editorActions.CopyPastePreProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RawText;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.wannabe.graven.services.GravenService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.diagnostic.Logger;

public class GravenCopyPastePreProcessor implements CopyPastePreProcessor {
	private static final Logger log = Logger.getInstance(GravenCopyPastePreProcessor.class);

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
			return gravenService.tryToRewrite(text, file.getName());
		} catch (Exception e) {
			log.warn(e);
			return text;
		}
	}
}
