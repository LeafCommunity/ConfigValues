/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit.data;

import community.leaf.configvalues.bukkit.DefaultYamlValue;
import community.leaf.configvalues.bukkit.YamlValue;
import org.bukkit.configuration.ConfigurationSection;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Optional;

public interface YamlDataSource
{
    ConfigurationSection data();
    
    void set(String key, @NullOr Object value);
    
    <V> void set(YamlValue<V> key, @NullOr V value);
    
    default boolean has(YamlValue<?> value)
    {
        return value.get(data()).isPresent();
    }
    
    default <V> Optional<V> get(YamlValue<V> value)
    {
        return value.get(data());
    }
    
    default <V> V getOrDefault(DefaultYamlValue<V> value)
    {
        return value.getOrDefault(data());
    }
    
    default <V> void setAsDefault(DefaultYamlValue<V> value)
    {
        set(value, value.getDefaultValue());
    }
    
    default <V> void setAsDefaultIfUnset(DefaultYamlValue<V> value)
    {
        if (!has(value)) { setAsDefault(value); }
    }
}
