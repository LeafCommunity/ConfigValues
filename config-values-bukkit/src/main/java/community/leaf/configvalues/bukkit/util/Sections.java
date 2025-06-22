/*
 * Copyright © 2021-2025, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit.util;

import org.bukkit.configuration.ConfigurationSection;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class Sections {
	private Sections() {
		throw new UnsupportedOperationException();
	}
	
	private static void validate(ConfigurationSection section, String key) {
		Objects.requireNonNull(section, "section");
		Objects.requireNonNull(key, "key");
	}
	
	public static Optional<ConfigurationSection> get(ConfigurationSection section, String key) {
		validate(section, key);
		return Optional.ofNullable(section.getConfigurationSection(key));
	}
	
	public static ConfigurationSection getOrCreate(ConfigurationSection section, String key) {
		validate(section, key);
		@Nullable ConfigurationSection existing = section.getConfigurationSection(key);
		return (existing != null) ? existing : section.createSection(key);
	}
	
	public static ConfigurationSection getOrCreate(ConfigurationSection section, String... keys) {
		Objects.requireNonNull(keys, "keys");
		ConfigurationSection result = section;
		for (String key : keys) {
			result = getOrCreate(result, key);
		}
		return result;
	}
}
