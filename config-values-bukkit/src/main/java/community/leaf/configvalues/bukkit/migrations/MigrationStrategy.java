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

@FunctionalInterface
public interface MigrationStrategy
{
    void migrate(ConfigurationSection existing, String key, YamlDataSource data, YamlValue<?> value);
}
