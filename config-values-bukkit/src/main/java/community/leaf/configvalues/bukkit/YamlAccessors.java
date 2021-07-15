/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit;

import com.rezzedup.util.valuables.Adapts;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

final class YamlAccessors
{
    private YamlAccessors() {}
    
    static final YamlAccessor<String> STRING = YamlAccessor.of(Adapts.object().intoString());
    
    static final YamlAccessor<Boolean> BOOLEAN = YamlAccessor.of(Adapts.object().intoBoolean());
    
    static final YamlAccessor<Integer> INTEGER = YamlAccessor.of(Adapts.object().intoInteger());
    
    static final YamlAccessor<Long> LONG = YamlAccessor.of(Adapts.object().intoLong());
    
    static final YamlAccessor<Float> FLOAT = YamlAccessor.of(Adapts.object().intoFloat());
    
    static final YamlAccessor<Double> DOUBLE = YamlAccessor.of(Adapts.object().intoDouble());
    
    static final YamlAccessor<List<String>> STRING_LIST = YamlAccessor.of(YamlAdapters.STRING_LIST);
    
    static final YamlAccessor<List<Map<?, ?>>> MAP_LIST = YamlAccessor.of(YamlAdapters.MAP_LIST);
    
    static final YamlAccessor<UUID> U_UID = YamlAccessor.of(Adapts.object().intoUuid());
    
    static final YamlAccessor<Sound> SOUND = YamlAccessor.of(YamlAdapters.SOUND);
    
    static final YamlAccessor<Material> MATERIAL = YamlAccessor.of(YamlAdapters.MATERIAL);
    
    static final YamlAccessor<Instant> INSTANT = YamlAccessor.of(YamlAdapters.INSTANT);
}
