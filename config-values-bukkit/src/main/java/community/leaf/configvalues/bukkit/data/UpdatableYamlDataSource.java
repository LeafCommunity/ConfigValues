/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit.data;

import community.leaf.configvalues.bukkit.YamlValue;
import pl.tlinkowski.annotation.basic.NullOr;

public interface UpdatableYamlDataSource extends YamlDataSource
{
    boolean isUpdated();
    
    void updated(boolean state);
    
    @Override
    default void set(String key, @NullOr Object value)
    {
        updated(true);
        data().set(key, value);
    }
    
    @Override
    default <T> void set(YamlValue<T> key, @NullOr T value)
    {
        updated(true);
        key.set(data(), value);
    }
}
