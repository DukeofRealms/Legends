package com.mcthepond.champs.library.configuration;

import java.util.Map;

import com.mcthepond.champs.library.util.Preconditions;

/**
 * This is a {@link Configuration} implementation that does not save or load
 * from any source, and stores all values in memory only.
 * This is useful for temporary Configurations for providing defaults.
 */
public class MemoryConfiguration extends MemorySection implements Configuration {
	protected Configuration defaults;
	protected MemoryConfigurationOptions options;

	/**
	 * Creates an empty {@link MemoryConfiguration} with no default values.
	 */
	public MemoryConfiguration() {
	}

	/**
	 * Creates an empty {@link MemoryConfiguration} using the specified {@link Configuration}
	 * as a source for all default values.
	 *
	 * @param defaults Default value provider
	 * @throws IllegalArgumentException Thrown if defaults is null
	 */
	public MemoryConfiguration(Configuration defaults) {
		this.defaults = defaults;
	}

	@Override
	public void addDefault(String path, Object value) {
		Preconditions.checkNotNull(path, "Path may not be null");

		if (defaults == null) {
			defaults = new MemoryConfiguration();
		}

		defaults.set(path, value);
	}

	public void addDefaults(Map<String, Object> defaults) {
		Preconditions.checkNotNull(defaults, "Defaults may not be null");

		for (Map.Entry<String, Object> entry : defaults.entrySet()) {
			addDefault(entry.getKey(), entry.getValue());
		}
	}

	public void addDefaults(Configuration defaults) {
		Preconditions.checkNotNull(defaults, "Defaults may not be null");

		addDefaults(defaults.getValues(true));
	}

	public void setDefaults(Configuration defaults) {
		Preconditions.checkNotNull(defaults, "Defaults may not be null");

		this.defaults = defaults;
	}

	public Configuration getDefaults() {
		return defaults;
	}

	@Override
	public ConfigurationSection getParent() {
		return null;
	}

	public MemoryConfigurationOptions options() {
		if (options == null) {
			options = new MemoryConfigurationOptions(this);
		}

		return options;
	}
}
