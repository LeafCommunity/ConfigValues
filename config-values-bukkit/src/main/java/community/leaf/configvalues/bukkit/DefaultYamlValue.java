/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit;

import com.rezzedup.util.constants.types.TypeCapture;
import com.rezzedup.util.valuables.DefaultKeyValue;
import org.bukkit.configuration.ConfigurationSection;

public interface DefaultYamlValue<V> extends DefaultKeyValue<ConfigurationSection, String, V>, YamlValue<V>
{
    static TypeCapture<DefaultYamlValue<?>> type()
    {
        return YamlValues.DEFAULT_TYPE;
    }
}
