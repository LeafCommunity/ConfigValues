/*
 * Copyright © 2021-2025, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit;

import com.rezzedup.util.constants.types.Primitives;
import com.rezzedup.util.valuables.Adapter;
import community.leaf.configvalues.bukkit.util.EnumLike;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public final class YamlAdapters {
	private YamlAdapters() {
		throw new UnsupportedOperationException();
	}
	
	private static <V> Adapter<Object, List<V>> list(Function<Object, @Nullable V> conversion) {
		return Adapter.of(
			serialized ->
			{
				if (!(serialized instanceof List)) {
					return Optional.empty();
				}
				
				List<?> existing = (List<?>) serialized;
				List<V> values = new ArrayList<>();
				
				for (Object obj : existing) {
					@Nullable V converted = conversion.apply(obj);
					if (converted != null) {
						values.add(converted);
					}
				}
				
				return Optional.of(values);
			},
			Optional::of
		);
	}
	
	public static <E> Adapter<Object, E> ofEnumLike(Class<E> type) {
		EnumLike<E> accessor = EnumLike.of(type);
		
		return Adapter.of(
			serialized -> accessor.valueOf(String.valueOf(serialized)),
			deserialized -> Optional.of(accessor.name(deserialized))
		);
	}
	
	public static <V> Adapter<Object, V> ofParsed(Function<String, @Nullable V> parser) {
		return Adapter.of(
			serialized -> {
				try {
					return Optional.ofNullable(parser.apply(String.valueOf(serialized)));
				} catch (RuntimeException ignored) {
					return Optional.empty();
				}
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
	
	public static final Adapter<Object, Sound> SOUND = ofEnumLike(Sound.class);
	
	public static final Adapter<Object, Material> MATERIAL = ofEnumLike(Material.class);
	
	public static final Adapter<Object, Instant> INSTANT = ofParsed(Instant::parse);
}
