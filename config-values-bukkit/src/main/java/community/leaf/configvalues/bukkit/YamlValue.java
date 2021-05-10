/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit;

import com.rezzedup.util.constants.types.TypeCapture;
import com.rezzedup.util.valuables.KeyValue;
import community.leaf.configvalues.bukkit.migrations.Migration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface YamlValue<V> extends KeyValue<ConfigurationSection, String, V>
{
    static TypeCapture<YamlValue<?>> type()
    {
        return YamlValues.TYPE;
    }
    
    static <V> Builder<V> of(String key, YamlAccessor<V> accessor)
    {
        return YamlValues.builder(key, accessor);
    }
    
    static Builder<String> ofString(String key)
    {
        return of(key, YamlAccessors.STRING);
    }
    
    static Builder<Boolean> ofBoolean(String key)
    {
        return of(key, YamlAccessors.BOOLEAN);
    }
    
    static Builder<Integer> ofInteger(String key)
    {
        return of(key, YamlAccessors.INTEGER);
    }
    
    static Builder<Long> ofLong(String key)
    {
        return of(key, YamlAccessors.LONG);
    }
    
    static Builder<Float> ofFloat(String key)
    {
        return of(key, YamlAccessors.FLOAT);
    }
    
    static Builder<Double> ofDouble(String key)
    {
        return of(key, YamlAccessors.DOUBLE);
    }
    
    static Builder<List<String>> ofStringList(String key)
    {
        return of(key, YamlAccessors.STRING_LIST);
    }
    
    static Builder<List<Map<?, ?>>> ofMapList(String key)
    {
        return of(key, YamlAccessors.MAP_LIST);
    }
    
    static Builder<UUID> ofUuid(String key)
    {
        return of(key, YamlAccessors.U_UID);
    }
    
    List<Migration> migrations();
    
    interface Builder<V>
    {
        Builder<V> migrates(Migration ... migrations);
        
        YamlValue<V> maybe();
        
        DefaultYamlValue<V> defaults(V value);
    }
}
