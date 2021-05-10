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
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Objects;

final class Migrations
{
    private Migrations() { throw new UnsupportedOperationException(); }
    
    static void copy(ConfigurationSection existing, String key, YamlDataSource data, YamlValue<?> value)
    {
        @NullOr Object maybe = existing.get(key);
        if (maybe != null) { data.set(value.key(), maybe); }
    }
    
    static void move(ConfigurationSection existing, String key, YamlDataSource data, YamlValue<?> value)
    {
        @NullOr Object maybe = existing.get(key);
        if (maybe == null) { return; }
        
        data.set(value.key(), maybe);
        existing.set(key, null);
    }
    
    static final class MigrationImpl implements Migration
    {
        private final String key;
        private final MigrationStrategy strategy;
        
        MigrationImpl(String key, MigrationStrategy strategy)
        {
            this.key = Objects.requireNonNull(key, "key");
            this.strategy = Objects.requireNonNull(strategy, "strategy");
        }
    
        @Override
        public String key() { return key; }
    
        @Override
        public MigrationStrategy strategy() { return strategy; }
    }
}
