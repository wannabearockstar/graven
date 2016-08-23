package com.wannabe.graven.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurableUi;
import com.intellij.openapi.options.ConfigurationException;
import com.wannabe.graven.settings.GravenSettingsConfigurableProvider;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class GravenSettingsUI implements Configurable {

	private JCheckBox showConfirmDialogBeforeCheckBox;
	private JPanel myPanel;
	private static volatile AtomicBoolean isConfirmFormShow = new AtomicBoolean(true);

	@Nls
	@Override
	public String getDisplayName() {
		return "Graven";
	}

	@Nullable
	@Override
	public String getHelpTopic() {
		return null;
	}

	@Nullable
	@Override
	public JComponent createComponent() {
		showConfirmDialogBeforeCheckBox.setSelected(isConfirmFormShow.get());
		return myPanel;
	}

	@Override
	public boolean isModified() {
		return showConfirmDialogBeforeCheckBox.isSelected() != isConfirmFormShow.get();
	}

	@Override
	public void apply() throws ConfigurationException {
		isConfirmFormShow.getAndSet(showConfirmDialogBeforeCheckBox.isSelected());
	}

	@Override
	public void reset() {

	}

	@Override
	public void disposeUIResources() {

	}

	public static boolean isConfirmDialogShow() {
		return isConfirmFormShow.get();
	}
}
