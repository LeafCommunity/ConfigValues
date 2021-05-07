/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit;

import com.rezzedup.util.constants.types.TypeCapture;
import org.bukkit.configuration.ConfigurationSection;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

final class YamlValues
{
    private YamlValues() { throw new UnsupportedOperationException(); }
    
    //
    //  Type tokens
    //
    
    static final TypeCapture<YamlValue<?>> TYPE = new TypeCapture<>() {};
    
    static final TypeCapture<DefaultYamlValue<?>> DEFAULT_TYPE = new TypeCapture<>() {};
    
    //
    //  Builders
    //
    
    static <V> YamlValue.Builder<V> builder(String key, YamlAdapter<V> adapter)
    {
        return new BuilderImpl<>(key, adapter);
    }
    
    static class BuilderImpl<V> implements YamlValue.Builder<V>
    {
        @NullOr List<String> migrations = null;
        
        final String key;
        final YamlAdapter<V> adapter;
        
        BuilderImpl(String key, YamlAdapter<V> adapter)
        {
            this.key = Objects.requireNonNull(key, "key");
            this.adapter = Objects.requireNonNull(adapter, "adapter");
        }
        
        @Override
        public YamlValue.Builder<V> migrates(String... keys)
        {
            this.migrations = List.of(keys);
            return this;
        }
        
        @Override
        public YamlValue<V> maybe()
        {
            return new MaybeImpl<>(key, adapter, migrations);
        }
        
        @Override
        public DefaultYamlValue<V> defaults(V def)
        {
            return new DefaultImpl<>(key, adapter, migrations, def);
        }
    }
    
    static class MaybeImpl<V> implements YamlValue<V>
    {
        final String key;
        final YamlAdapter<V> adapter;
        final List<String> migrations;
    
        MaybeImpl(String key, YamlAdapter<V> adapter, @NullOr List<String> migrations)
        {
            this.key = key;
            this.adapter = adapter;
            this.migrations = (migrations == null) ? List.of() : List.copyOf(migrations);
        }
    
        @Override
        public String key() { return key; }
    
        @Override
        public List<String> migrations() { return migrations; }
        
        @Override
        public Optional<V> get(ConfigurationSection storage)
        {
            return Optional.ofNullable(adapter.get(storage, key));
        }
        
        @Override
        public void set(ConfigurationSection storage, @NullOr V value)
        {
            adapter.set(storage, key, value);
        }
    }
    
    static class DefaultImpl<V> extends MaybeImpl<V> implements DefaultYamlValue<V>
    {
        V def;
    
        DefaultImpl(String key, YamlAdapter<V> adapter, @NullOr List<String> migrations, V def)
        {
            super(key, adapter, migrations);
            this.def = Objects.requireNonNull(def, "def");
        }
        
        @SuppressWarnings("NullableProblems")
        @Override
        public V getDefaultValue() { return def; }
    }
}
