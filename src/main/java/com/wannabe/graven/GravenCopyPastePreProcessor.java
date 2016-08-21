package com.wannabe.graven;

import com.intellij.codeInsight.editorActions.CopyPastePreProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RawText;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.wannabe.graven.services.GravenService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GravenCopyPastePreProcessor implements CopyPastePreProcessor {

	@Nullable
	@Override
	public String preprocessOnCopy(PsiFile file, int[] startOffsets, int[] endOffsets, String text) {
		return null;
	}

	@NotNull
	@Override
	public String preprocessOnPaste(Project project, PsiFile file, Editor editor, String text, RawText rawText) {
		GravenService gravenService = GravenService.getInstance();
		return gravenService.tryToRewrite(text, file.getName());
	}
}
