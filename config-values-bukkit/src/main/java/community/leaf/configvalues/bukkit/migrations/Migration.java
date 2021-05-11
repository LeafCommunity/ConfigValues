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
 * Migrates a specific config value from an existing
 * configuration into another.
 */
public interface Migration
{
    /**
     * Creates a new {@code Migration} using the
     * provided key and strategy.
     *
     * @param key       the key
     * @param strategy  the migration strategy
     *
     * @return  a new instance composed of the arguments
     */
    static Migration from(String key, MigrationStrategy strategy)
    {
        return new Migrations.Impl(key, strategy);
    }
    
    /**
     * Migrates values at the specified key by
     * copying them from one config to another.
     *
     * <p>This is the simplest migration strategy:
     * it leaves the existing value untouched
     * and intact.</p>
     *
     * @param key   the old value's key
     *
     * @return  a new instance composed of the
     *          key and a "copy" strategy
     */
    static Migration copy(String key)
    {
        return from(key, Migrations::copy);
    }
    
    /**
     * Migrates values at the specified key by
     * moving them from one config to another.
     *
     * <p>If migration is successful, the old
     * value is <b>removed</b> from the source
     * configuration.</p>
     *
     * @param key   the old value's key
     *
     * @return  a new instance composed of the
     *          key and a "move" strategy
     */
    static Migration move(String key)
    {
        return from(key, Migrations::move);
    }
    
    /**
     * Gets the key to migrate from.
     *
     * @return  the key
     */
    String key();
    
    /**
     * Gets the mechanism by which the value is migrated.
     *
     * @return  the migration strategy
     */
    MigrationStrategy strategy();
    
    /**
     * Migrates the specific value (retrieved with
     * {@link #key()}) from an existing configuration
     * into another by submitting it to the destination
     * YAML data at the provided value's key.
     *
     * <p>If the retrieved value doesn't exist, then
     * nothing is migrated.</p>
     *
     * @param existing  existing configuration
     * @param data      destination yaml data
     * @param value     destination yaml value
     */
    default void migrate(ConfigurationSection existing, YamlDataSource data, YamlValue<?> value)
    {
        strategy().migrate(existing, key(), data, value);
    }
}
