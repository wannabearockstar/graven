package com.wannabe.graven.ui;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.EditorTextField;
import com.wannabe.graven.domain.ui.EditorData;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class ConfirmDialog extends DialogWrapper {

	private final EditorTextField originalField;
	private final EditorTextField rewrittenField;
	private JPanel contentPane;

	private JPanel originalPanel;
	private JPanel rewrittenPanel;

	public ConfirmDialog(@Nullable Project project, EditorData original, EditorData rewritten) {
		super(project);

		originalField = addEditorField(project, original.getText(), originalPanel, original.getFileType());
		rewrittenField = addEditorField(project, rewritten.getText(), rewrittenPanel, rewritten.getFileType());

		init();
		setTitle("Graven Rewrite");
		setOKButtonText("Paste rewritten");
		setCancelButtonText("Keep original");
	}

	private EditorTextField addEditorField(@Nullable Project project, String text, JPanel root, FileType fileType) {
		EditorTextField field = new EditorTextField(text, project, fileType);
		field.setText(text);
		field.setEnabled(true);
		field.setVisible(true);
		field.setOneLineMode(false);
		root.setLayout(new BorderLayout());
		root.add(field);

		return field;
	}

	@Nullable
	@Override
	protected JComponent createCenterPanel() {
		return contentPane;
	}

	@Nullable
	@Override
	public JComponent getPreferredFocusedComponent() {
		return rewrittenField;
	}

	public String getRewrittenValue() {
		return rewrittenField.getText();
	}

}
