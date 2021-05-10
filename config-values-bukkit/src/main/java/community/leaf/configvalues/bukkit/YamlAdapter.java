/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit;

import com.rezzedup.util.valuables.Adapter;
import com.rezzedup.util.valuables.Deserializer;
import com.rezzedup.util.valuables.Serializer;

import java.util.Objects;
import java.util.Optional;

public interface YamlAdapter<V> extends Adapter<Object, V>
{
    static <V> YamlAdapter<V> adapts(Deserializer<Object, V> deserializer, Serializer<V, Object> serializer)
    {
        Objects.requireNonNull(deserializer, "deserializer");
        Objects.requireNonNull(serializer, "serializer");
        
        return new YamlAdapter<>()
        {
            @Override
            public Optional<V> deserialize(Object serialized) { return deserializer.deserialize(serialized); }
            
            @Override
            public Optional<Object> serialize(V deserialized) { return serializer.serialize(deserialized); }
        };
    }
    
    static <V> YamlAdapter<V> from(Adapter<Object, V> adapter)
    {
        Objects.requireNonNull(adapter, "adapter");
        return adapts(adapter, adapter);
    }
}
