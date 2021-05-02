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
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Map;

public interface YamlValue<V> extends KeyValue<ConfigurationSection, String, V>
{
    static TypeCapture<YamlValue<?>> type()
    {
        return YamlValues.TYPE;
    }
    
    static <V> Builder<V> of(String key, YamlAdapter<V> adapter)
    {
        return YamlValues.builder(key, adapter);
    }
    
    static Builder<String> ofString(String key)
    {
        return of(key, YamlValues.STRING);
    }
    
    static Builder<Boolean> ofBoolean(String key)
    {
        return of(key, YamlValues.BOOLEAN);
    }
    
    static Builder<Integer> ofInteger(String key)
    {
        return of(key, YamlValues.INTEGER);
    }
    
    static Builder<Long> ofLong(String key)
    {
        return of(key, YamlValues.LONG);
    }
    
    static Builder<Double> ofDouble(String key)
    {
        return of(key, YamlValues.DOUBLE);
    }
    
    static Builder<List<String>> ofStringList(String key)
    {
        return of(key, YamlValues.STRING_LIST);
    }
    
    static Builder<List<Map<?, ?>>> ofMapList(String key)
    {
        return of(key, YamlValues.MAP_LIST);
    }
    
    List<String> migrations();
    
    interface Builder<V>
    {
        Builder<V> migrates(String ... keys);
        
        YamlValue<V> maybe();
        
        DefaultYamlValue<V> defaults(V value);
    }
}
