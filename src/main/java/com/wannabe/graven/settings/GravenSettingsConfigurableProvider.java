package com.wannabe.graven.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurableProvider;
import com.wannabe.graven.ui.GravenSettingsUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GravenSettingsConfigurableProvider extends ConfigurableProvider {

	@Nullable
	@Override
	public Configurable createConfigurable() {
		return new GravenSettingsUI();
	}
}
