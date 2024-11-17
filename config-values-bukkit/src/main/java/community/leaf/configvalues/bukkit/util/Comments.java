/*
 * Copyright © 2021-2024, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.List;
import java.util.stream.Collectors;

public class Comments {
	private static final boolean SUPPORTS_COMMENTS;
	
	static {
		boolean supported = false;
		
		try {
			ConfigurationSection section = new YamlConfiguration();
			section.setComments("test", List.of("test"));
			supported = true;
		} catch (NoSuchMethodError ignored) {
		}
		
		SUPPORTS_COMMENTS = supported;
	}
	
	public static boolean isSupported() {
		return SUPPORTS_COMMENTS;
	}
	
	public static List<@NullOr String> above(ConfigurationSection section, String key) {
		return (SUPPORTS_COMMENTS) ? section.getComments(key) : List.of();
	}
	
	public static void above(ConfigurationSection section, String key, @NullOr List<@NullOr String> lines) {
		if (SUPPORTS_COMMENTS) {
			section.setComments(key, lines);
		}
	}
	
	public static List<@NullOr String> inline(ConfigurationSection section, String key) {
		return (SUPPORTS_COMMENTS) ? section.getInlineComments(key) : List.of();
	}
	
	public static void inline(ConfigurationSection section, String key, @NullOr List<@NullOr String> lines) {
		if (SUPPORTS_COMMENTS) {
			section.setInlineComments(key, lines);
		}
	}
	
	@SuppressWarnings({"ConstantConditions", "deprecation"}) // .header() is deprecated and was previously @Nullable
	public static List<@NullOr String> header(FileConfiguration config) {
		if (SUPPORTS_COMMENTS) {
			return config.options().getHeader();
		}
		
		@NullOr String header = config.options().header();
		return (header == null) ? List.of() : List.of(config.options().header().split("[\n\r]+"));
	}
	
	@SuppressWarnings("deprecation") // .header() is deprecated
	public static void header(FileConfiguration config, @NullOr List<@NullOr String> lines) {
		if (SUPPORTS_COMMENTS) {
			config.options().setHeader(lines);
		} else {
			if (lines == null || lines.isEmpty()) {
				config.options().header(null);
			} else {
				config.options().header(
					lines.stream()
						.map(line -> (line == null) ? "" : line)
						.collect(Collectors.joining("\n"))
				);
			}
		}
	}
	
	public static List<@NullOr String> footer(FileConfiguration config) {
		return (SUPPORTS_COMMENTS) ? config.options().getFooter() : List.of();
	}
	
	public static void footer(FileConfiguration config, @NullOr List<@NullOr String> lines) {
		if (SUPPORTS_COMMENTS) {
			config.options().setFooter(lines);
		}
	}
}
