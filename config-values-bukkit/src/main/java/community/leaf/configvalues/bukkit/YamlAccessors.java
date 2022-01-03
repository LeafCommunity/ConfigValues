/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit;

import com.rezzedup.util.valuables.Adapter;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

final class YamlAccessors
{
    private YamlAccessors() {}
    
    static final YamlAccessor<String> STRING = YamlAccessor.of(Adapter.ofObject().intoString());
    
    static final YamlAccessor<Boolean> BOOLEAN = YamlAccessor.of(Adapter.ofObject().intoBoolean());
    
    static final YamlAccessor<Integer> INTEGER = YamlAccessor.of(Adapter.ofObject().intoInteger());
    
    static final YamlAccessor<Long> LONG = YamlAccessor.of(Adapter.ofObject().intoLong());
    
    static final YamlAccessor<Float> FLOAT = YamlAccessor.of(Adapter.ofObject().intoFloat());
    
    static final YamlAccessor<Double> DOUBLE = YamlAccessor.of(Adapter.ofObject().intoDouble());
    
    static final YamlAccessor<List<String>> STRING_LIST = YamlAccessor.of(YamlAdapters.STRING_LIST);
    
    static final YamlAccessor<List<Map<?, ?>>> MAP_LIST = YamlAccessor.of(YamlAdapters.MAP_LIST);
    
    static final YamlAccessor<UUID> U_UID = YamlAccessor.of(Adapter.ofObject().intoUuid());
    
    static final YamlAccessor<Sound> SOUND = YamlAccessor.of(YamlAdapters.SOUND);
    
    static final YamlAccessor<Material> MATERIAL = YamlAccessor.of(YamlAdapters.MATERIAL);
    
    static final YamlAccessor<Instant> INSTANT = YamlAccessor.of(YamlAdapters.INSTANT);
}
