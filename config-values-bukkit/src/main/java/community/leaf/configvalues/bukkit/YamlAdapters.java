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
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

final class YamlAdapters
{
    private YamlAdapters() { throw new UnsupportedOperationException(); }
    
    static final YamlAdapter<String> STRING =
        simple(serialized -> Optional.of(String.valueOf(serialized)));
    
    static final YamlAdapter<Boolean> BOOLEAN =
        simple(serialized ->
            (serialized instanceof Boolean)
                ? Optional.of((Boolean) serialized)
                : Optional.empty()
        );
    
    static final YamlAdapter<Integer> INTEGER = number(Number::intValue);
    
    static final YamlAdapter<Long> LONG = number(Number::longValue);
    
    static final YamlAdapter<Float> FLOAT = number(Number::floatValue);
    
    static final YamlAdapter<Double> DOUBLE = number(Number::doubleValue);
    
    static final YamlAdapter<List<String>> STRING_LIST =
        list(val -> (val instanceof String || Primitives.isBoxed(val)) ? String.valueOf(val) : null);
    
    static final YamlAdapter<List<Map<?, ?>>> MAP_LIST =
        list(val ->(val instanceof Map<?, ?>) ? (Map<?, ?>) val : null);
    
    static final YamlAdapter<UUID> U_UID =
        convert(
            serialized -> {
                try { return Optional.of(UUID.fromString(String.valueOf(serialized))); }
                catch (IllegalArgumentException ignored) { return Optional.empty(); }
            },
            deserialized -> Optional.of(deserialized.toString())
        );
    
    //
    //  Factories
    //
    
    static <V> YamlAdapter<V> convert(Deserializer<Object, V> deserializer, Serializer<V, Object> serializer)
    {
        return YamlAdapter.adapts(deserializer, serializer);
    }
    
    static <V> YamlAdapter<V> simple(Deserializer<Object, V> deserializer)
    {
        return convert(deserializer, Optional::of);
    }
    
    static <V extends Number> YamlAdapter<V> number(Function<Number, V> conversion)
    {
        return simple(val ->
            (val instanceof Number)
                ? Optional.of(conversion.apply((Number) val))
                : Optional.empty()
        );
    }
    
    static <V> YamlAdapter<List<V>> list(Function<Object, @NullOr V> conversion)
    {
        return simple(serialized ->
        {
            if (!(serialized instanceof List)) { return Optional.empty(); }
            
            List<?> existing = (List<?>) serialized;
            List<V> values = new ArrayList<>();
            
            for (Object obj : existing)
            {
                @NullOr V converted = conversion.apply(obj);
                if (converted != null) { values.add(converted); }
            }
            
            return Optional.of(values);
        });
    }
}
