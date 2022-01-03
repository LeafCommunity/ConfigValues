/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit;

import com.rezzedup.util.constants.types.Primitives;
import com.rezzedup.util.valuables.Adapter;
import org.bukkit.Material;
import org.bukkit.Sound;
import pl.tlinkowski.annotation.basic.NullOr;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class YamlAdapters
{
    private YamlAdapters() { throw new UnsupportedOperationException(); }
    
    private static <V> Adapter<Object, List<V>> list(Function<Object, @NullOr V> conversion)
    {
        return Adapter.of(
            serialized ->
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
            },
            Optional::of
        );
    }
    
    public static <E extends Enum<E>> Adapter<Object, E> ofEnum(Class<E> type)
    {
        return Adapter.of(
            serialized -> Adapter.ofString().intoEnum(type).deserialize(String.valueOf(serialized)),
            deserialized -> Optional.of(String.valueOf(deserialized))
        );
    }
    
    public static <V> Adapter<Object, V> ofParsed(Function<String, @NullOr V> parser)
    {
        return Adapter.of(
            serialized -> {
                try { return Optional.ofNullable(parser.apply(String.valueOf(serialized))); }
                catch (RuntimeException ignored) { return Optional.empty(); }
            },
            deserialized -> Optional.of(String.valueOf(deserialized))
        );
    }
    
    public static final Adapter<Object, List<String>> STRING_LIST =
        list(serialized ->
            (serialized instanceof String || Primitives.isBoxed(serialized))
                ? String.valueOf(serialized)
                : null
        );
    
    public static final Adapter<Object, List<Map<?, ?>>> MAP_LIST =
        list(serialized -> (serialized instanceof Map<?, ?>) ? (Map<?, ?>) serialized : null);
    
    public static final Adapter<Object, Sound> SOUND = ofEnum(Sound.class);
    
    public static final Adapter<Object, Material> MATERIAL = ofEnum(Material.class);
    
    public static final Adapter<Object, Instant> INSTANT = ofParsed(Instant::parse);
}
