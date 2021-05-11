/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit.migrations;

import community.leaf.configvalues.bukkit.YamlValue;
import community.leaf.configvalues.bukkit.data.YamlDataSource;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Mechanism that migrates values from
 * one configuration to another.
 */
@FunctionalInterface
public interface MigrationStrategy
{
    /**
     * Migrates a value from an existing configuration
     * into another by retrieving it from the specified
     * key then submitting it to the destination YAML
     * data at the provided value's key.
     *
     * <p>If the retrieved value doesn't exist, then
     * nothing is migrated.</p>
     *
     * @param existing  existing configuration
     * @param key       key to retrieve the existing value from
     * @param data      destination yaml data
     * @param value     destination yaml value
     */
    void migrate(ConfigurationSection existing, String key, YamlDataSource data, YamlValue<?> value);
}
