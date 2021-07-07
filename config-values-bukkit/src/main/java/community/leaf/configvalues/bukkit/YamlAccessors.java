/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit;

import java.util.List;
import java.util.Map;
import java.util.UUID;

final class YamlAccessors
{
    private YamlAccessors() {}
    
    static final YamlAccessor<String> STRING = YamlAccessor.of(YamlAdapters.STRING);
    
    static final YamlAccessor<Boolean> BOOLEAN = YamlAccessor.of(YamlAdapters.BOOLEAN);
    
    static final YamlAccessor<Integer> INTEGER = YamlAccessor.of(YamlAdapters.INTEGER);
    
    static final YamlAccessor<Long> LONG = YamlAccessor.of(YamlAdapters.LONG);
    
    static final YamlAccessor<Float> FLOAT = YamlAccessor.of(YamlAdapters.FLOAT);
    
    static final YamlAccessor<Double> DOUBLE = YamlAccessor.of(YamlAdapters.DOUBLE);
    
    static final YamlAccessor<List<String>> STRING_LIST = YamlAccessor.of(YamlAdapters.STRING_LIST);
    
    static final YamlAccessor<List<Map<?, ?>>> MAP_LIST = YamlAccessor.of(YamlAdapters.MAP_LIST);
    
    static final YamlAccessor<UUID> U_UID = YamlAccessor.of(YamlAdapters.U_UID);
}
