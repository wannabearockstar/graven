package com.wannabe.graven.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

public class ConfirmDialog extends DialogWrapper {

	private JPanel contentPane;
	private JTextArea original;
	private JTextArea rewritten;

	public ConfirmDialog(@Nullable Project project, String originalText, String rewrittenText) {
		super(project);
		original.setText(originalText);
		original.setEditable(false);

		rewritten.setText(rewrittenText);
		init();
		setTitle("Graven Rewrite");
		setOKButtonText("Paste rewritten");
		setCancelButtonText("Keep original");
	}

	@Nullable
	@Override
	protected JComponent createCenterPanel() {
		return contentPane;
	}

	@Nullable
	@Override
	public JComponent getPreferredFocusedComponent() {
		return rewritten;
	}

	public String getRewrittenValue() {
		return rewritten.getText();
	}
}
