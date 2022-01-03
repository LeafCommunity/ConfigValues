/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
module community.leaf.configvalues.bukkit
{
    requires transitive com.rezzedup.util.constants;
    requires transitive com.rezzedup.util.valuables;
    
    requires static org.bukkit;
    requires static pl.tlinkowski.annotation.basic;
    
    exports community.leaf.configvalues.bukkit;
    exports community.leaf.configvalues.bukkit.data;
    exports community.leaf.configvalues.bukkit.migrations;
    exports community.leaf.configvalues.bukkit.util;
}
