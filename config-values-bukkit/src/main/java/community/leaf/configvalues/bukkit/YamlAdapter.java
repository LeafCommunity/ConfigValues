/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit;

import com.rezzedup.util.valuables.KeyValueAdapter;
import org.bukkit.configuration.ConfigurationSection;
import pl.tlinkowski.annotation.basic.NullOr;

public interface YamlAdapter<V> extends KeyValueAdapter<ConfigurationSection, Object, String, V>
{
    @Override
    default void set(ConfigurationSection storage, String key, @NullOr V value)
    {
        storage.set(key, (value == null) ? null : serialize(value));
    }
    
    @Override
    default @NullOr V get(ConfigurationSection storage, String key)
    {
        @NullOr Object value = storage.get(key);
        return (value == null) ? null : deserialize(value);
    }
}
