/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit;

import com.rezzedup.util.valuables.Adapter;
import com.rezzedup.util.valuables.KeyGetter;
import com.rezzedup.util.valuables.KeySetter;
import org.bukkit.configuration.ConfigurationSection;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Objects;
import java.util.Optional;

public interface YamlAccessor<V> extends KeyGetter<ConfigurationSection, String, V>, KeySetter<ConfigurationSection, String, V>
{
    static <V> YamlAccessor<V> of(Adapter<Object, V> adapter)
    {
        Objects.requireNonNull(adapter, "adapter");
        
        return new YamlAccessor<>()
        {
            @Override
            public Optional<V> get(ConfigurationSection storage, String key)
            {
                return Optional.ofNullable(storage.get(key)).flatMap(adapter::deserialize);
            }
    
            @Override
            public void set(ConfigurationSection storage, String key, @NullOr V updated)
            {
                storage.set(key, Optional.ofNullable(updated).flatMap(adapter::serialize).orElse(null));
            }
        };
    }
    
    static <V> YamlAccessor<V> of(KeyGetter<ConfigurationSection, String, V> getter, KeySetter<ConfigurationSection, String, V> setter)
    {
        Objects.requireNonNull(getter, "getter");
        Objects.requireNonNull(setter, "setter");
        
        return new YamlAccessor<>()
        {
            @Override
            public Optional<V> get(ConfigurationSection storage, String key)
            {
                return getter.get(storage, key);
            }
    
            @Override
            public void set(ConfigurationSection storage, String key, @NullOr V updated)
            {
                setter.set(storage, key, updated);
            }
        };
    }
}
