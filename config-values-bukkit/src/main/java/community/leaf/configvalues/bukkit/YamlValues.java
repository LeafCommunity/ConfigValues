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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

class YamlValues
{
    private YamlValues() { throw new UnsupportedOperationException(); }
    
    static final YamlAdapter<String> STRING = adapter(ConfigurationSection::getString);
    
    static final YamlAdapter<Boolean> BOOLEAN = adapter(ConfigurationSection::getBoolean);
    
    static final YamlAdapter<Integer> INTEGER = adapter(ConfigurationSection::getInt);
    
    static final YamlAdapter<Long> LONG = adapter(ConfigurationSection::getLong);
    
    static final YamlAdapter<Double> DOUBLE = adapter(ConfigurationSection::getDouble);
    
    static final YamlAdapter<List<String>> STRING_LIST = adapter(ConfigurationSection::getStringList);
    
    static final YamlAdapter<List<Map<?, ?>>> MAP_LIST = adapter(ConfigurationSection::getMapList);
    
    static <V> YamlAdapter<V> adapter(BiFunction<ConfigurationSection, String, V> getter)
    {
        return new AdapterImpl<>(getter);
    }
    
    static <V> YamlValue.Builder<V> builder(String key, YamlAdapter<V> adapter)
    {
        return new BuilderImpl<>(key, adapter);
    }
    
    static class AdapterImpl<V> implements YamlAdapter<V>
    {
        private final BiFunction<ConfigurationSection, String, V> getter;
    
        AdapterImpl(BiFunction<ConfigurationSection, String, V> getter) { this.getter = getter; }
    
        @Override
        public @NullOr V get(ConfigurationSection section, String key)
        {
            return getter.apply(section, key);
        }
    
        @Override
        public void set(ConfigurationSection section, String key, @NullOr V value)
        {
            section.set(key, value);
        }
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
        public boolean isSet(ConfigurationSection storage) { return storage.isSet(key); }
        
        @Override
        public Optional<V> get(ConfigurationSection storage)
        {
            return (isSet(storage)) ? Optional.ofNullable(adapter.get(storage, key)) : Optional.empty();
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
