/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit;

import com.rezzedup.util.valuables.Adapter;
import com.rezzedup.util.valuables.DelegatedKeyAdapter;
import com.rezzedup.util.valuables.KeyGetter;
import com.rezzedup.util.valuables.KeySetter;
import org.bukkit.configuration.ConfigurationSection;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Objects;

public interface YamlAdapter<V> extends DelegatedKeyAdapter<ConfigurationSection, Object, String, V>
{
    static <V> YamlAdapter<V> delegates(Adapter<Object, V> adapter)
    {
        Objects.requireNonNull(adapter, "adapter");
        
        return new YamlAdapter<>()
        {
            @Override
            public @NullOr V get(ConfigurationSection storage, String key)
            {
                @NullOr Object value = storage.get(key);
                return (value == null) ? null : deserialize(value);
            }
            
            @Override
            public void set(ConfigurationSection storage, String key, @NullOr V value)
            {
                storage.set(key, (value == null) ? null : serialize(value));
            }
            
            @Override
            public @NullOr V deserialize(Object serialized) { return adapter.deserialize(serialized); }
            
            @Override
            public @NullOr Object serialize(V deserialized) { return adapter.serialize(deserialized); }
        };
    }
    
    static <V> YamlAdapter<V> directly(KeyGetter<ConfigurationSection, String, V> getter, KeySetter<ConfigurationSection, String, V> setter)
    {
        Objects.requireNonNull(getter, "getter");
        Objects.requireNonNull(setter, "setter");
        
        return new YamlAdapter<>()
        {
            @Override
            public @NullOr V get(ConfigurationSection storage, String key)
            {
                return getter.get(storage, key);
            }
            
            @Override
            public void set(ConfigurationSection storage, String key, @NullOr V value)
            {
                setter.set(storage, key, value);
            }
            
            @Override
            public @NullOr V deserialize(Object serialized) { return null; }
            
            @Override
            public @NullOr Object serialize(V deserialized) { return null; }
        };
    }
}
