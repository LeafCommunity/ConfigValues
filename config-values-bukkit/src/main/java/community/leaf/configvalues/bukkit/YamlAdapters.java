/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit;

import com.rezzedup.util.constants.types.Primitives;
import com.rezzedup.util.valuables.Deserializer;
import com.rezzedup.util.valuables.Serializer;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

final class YamlAdapters
{
    private YamlAdapters() { throw new UnsupportedOperationException(); }
    
    static final YamlAdapter<String> STRING = basic(String::valueOf);
    
    static final YamlAdapter<Boolean> BOOLEAN = basic(val -> (val instanceof Boolean) ? (Boolean) val : null);
    
    static final YamlAdapter<Integer> INTEGER = number(Number::intValue);
    
    static final YamlAdapter<Long> LONG = number(Number::longValue);
    
    static final YamlAdapter<Float> FLOAT = number(Number::floatValue);
    
    static final YamlAdapter<Double> DOUBLE = number(Number::doubleValue);
    
    static final YamlAdapter<List<String>> STRING_LIST =
        list(val -> (val instanceof String || Primitives.isBoxed(val)) ? String.valueOf(val) : null);
    
    static final YamlAdapter<List<Map<?, ?>>> MAP_LIST =
        list(val ->(val instanceof Map<?, ?>) ? (Map<?, ?>) val : null);
    
    static final YamlAdapter<UUID> UUID_TYPE =
        convert(
            val -> {
                try { return UUID.fromString(String.valueOf(val)); }
                catch (IllegalArgumentException ignored) { return null; }
            },
            UUID::toString
        );
    
    //
    //  Factories
    //
    
    static <V> YamlAdapter<V> convert(Deserializer<Object, V> deserializer, Serializer<V, Object> serializer)
    {
        return new YamlAdapter<>()
        {
            @Override
            public @NullOr V deserialize(Object serialized) { return deserializer.deserialize(serialized); }
            
            @Override
            public @NullOr Object serialize(V deserialized) { return serializer.serialize(deserialized); }
        };
    }
    
    static <V> YamlAdapter<V> basic(Deserializer<Object, V> deserializer)
    {
        return convert(deserializer, val -> val);
    }
    
    static <V extends Number> YamlAdapter<V> number(Function<Number, V> conversion)
    {
        return basic(val -> (val instanceof Number) ? conversion.apply((Number) val) : null);
    }
    
    static <V> YamlAdapter<List<V>> list(Function<Object, @NullOr V> conversion)
    {
        return basic(val ->
        {
            if (!(val instanceof List)) { return null; }
            
            List<?> existing = (List<?>) val;
            List<V> values = new ArrayList<>();
            
            for (Object obj : existing)
            {
                @NullOr V converted = conversion.apply(obj);
                if (converted != null) { values.add(converted); }
            }
            
            return values;
        });
    }
}
