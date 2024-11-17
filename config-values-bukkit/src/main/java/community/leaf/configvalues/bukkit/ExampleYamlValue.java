/*
 * Copyright © 2021-2024, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit;

import com.rezzedup.util.constants.types.TypeCapture;

public interface ExampleYamlValue<V> extends DefaultYamlValue<V> {
	static TypeCapture<ExampleYamlValue<?>> type() {
		return YamlValues.EXAMPLE_TYPE;
	}
}
