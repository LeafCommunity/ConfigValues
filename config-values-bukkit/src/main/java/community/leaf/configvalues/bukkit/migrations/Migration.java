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

public interface Migration
{
    static Migration with(String key, MigrationStrategy strategy)
    {
        return new Migrations.MigrationImpl(key, strategy);
    }
    
    static Migration copy(String key)
    {
        return with(key, Migrations::copy);
    }
    
    static Migration move(String key)
    {
        return with(key, Migrations::move);
    }
    
    String key();
    
    MigrationStrategy strategy();
    
    default void migrate(ConfigurationSection existing, YamlDataSource data, YamlValue<?> value)
    {
        strategy().migrate(existing, key(), data, value);
    }
}
