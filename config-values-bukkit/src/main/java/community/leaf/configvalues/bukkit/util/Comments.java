/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.List;

public class Comments
{
    private static boolean SUPPORTED = false;
    
    static
    {
        try
        {
            ConfigurationSection section = new YamlConfiguration();
            section.setComments("test", List.of("test"));
            SUPPORTED = true;
        }
        catch (NoSuchMethodError ignored) {}
    }
    
    public static boolean isSupported() { return SUPPORTED; }
    
    public static void set(ConfigurationSection section, String key, @NullOr List<String> lines)
    {
        if (SUPPORTED) { section.setComments(key, lines); }
    }
}
