/*
 * Copyright © 2021-2024, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit.migrations;

import community.leaf.configvalues.bukkit.data.YamlDataSource;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Migrates values from one configuration to another.
 */
@FunctionalInterface
public interface MigrationStrategy {
	/**
	 * Migrates a value from an existing configuration
	 * to another by retrieving then submitting it to
	 * the destination YAML.
	 *
	 * <p>Note:</p>
	 *
	 * <ul>
	 *     <li>
	 *         If the destination YAML already has a value at
	 *         the destination key, then migration is skipped
	 *         (but some implementations may still modify the
	 *         existing value, such as removing it).
	 *     </li>
	 *     <li>
	 *         If the retrieved "existing" value doesn't exist,
	 *         then nothing is migrated.
	 *     </li>
	 * </ul>
	 *
	 * @param existing       existing configuration
	 * @param existingKey    key to retrieve the value from
	 * @param destination    destination yaml data
	 * @param destinationKey destination value key
	 */
	void migrate(ConfigurationSection existing, String existingKey, YamlDataSource destination, String destinationKey);
}
