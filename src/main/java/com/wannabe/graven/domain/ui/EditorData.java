package com.wannabe.graven.domain.ui;

import com.intellij.openapi.fileTypes.FileType;

public class EditorData {
	private final String text;
	private final FileType fileType;

	public EditorData(String text, FileType fileType) {
		this.text = text;
		this.fileType = fileType;
	}

	public String getText() {
		return text;
	}

	public FileType getFileType() {
		return fileType;
	}
}
