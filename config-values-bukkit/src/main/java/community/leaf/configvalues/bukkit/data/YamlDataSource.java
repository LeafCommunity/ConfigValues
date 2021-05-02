/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
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
    
    <T> void set(YamlValue<T> key, @NullOr T value);
    
    default <T> Optional<T> get(YamlValue<T> value)
    {
        return value.get(data());
    }
    
    default <T> T getOrDefault(DefaultYamlValue<T> value)
    {
        return value.getOrDefault(data());
    }
    
    default boolean has(YamlValue<?> value)
    {
        return value.isSet(data());
    }
}
