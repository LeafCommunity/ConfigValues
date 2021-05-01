/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/ConfigValuables>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit;

import org.bukkit.configuration.ConfigurationSection;
import pl.tlinkowski.annotation.basic.NullOr;

public interface YamlAdapter<V>
{
    @SuppressWarnings("NullableProblems")
    @NullOr V get(ConfigurationSection section, String key);
    
    void set(ConfigurationSection section, String key, @NullOr V value);
}
