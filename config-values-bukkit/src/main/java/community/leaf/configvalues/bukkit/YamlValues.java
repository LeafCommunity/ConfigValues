/*
 * Copyright © 2021-2022, RezzedUp <https://github.com/LeafCommunity/ConfigValues>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.configvalues.bukkit;

import com.rezzedup.util.constants.types.TypeCapture;
import community.leaf.configvalues.bukkit.migrations.Migration;
import org.bukkit.configuration.ConfigurationSection;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.ArrayList;
import java.util.Collections;
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
    
    static final TypeCapture<ExampleYamlValue<?>> EXAMPLE_TYPE = new TypeCapture<>() {};
    
    //
    //  Builders
    //
    
    static <V> YamlValue.Builder<V> builder(String key, YamlAccessor<V> accessor)
    {
        return new BuilderImpl<>(key, accessor);
    }
    
    static class BuilderImpl<V> implements YamlValue.Builder<V>
    {
        @NullOr List<Migration> migrations = null;
        
        final String key;
        final YamlAccessor<V> accessor;
        
        BuilderImpl(String key, YamlAccessor<V> accessor)
        {
            this.key = Objects.requireNonNull(key, "key");
            this.accessor = Objects.requireNonNull(accessor, "accessor");
        }
    
        @Override
        public YamlValue.Builder<V> migrates(Migration ... policies)
        {
            Objects.requireNonNull(policies, "policies");
            
            if (migrations == null) { migrations = new ArrayList<>(); }
            Collections.addAll(migrations, policies);
            
            return this;
        }
    
        @Override
        public YamlValue<V> maybe()
        {
            return new MaybeImpl<>(key, accessor, migrations);
        }
        
        @Override
        public DefaultYamlValue<V> defaults(V def)
        {
            return new DefaultImpl<>(key, accessor, migrations, def);
        }
    
        @Override
        public ExampleYamlValue<V> example(V example)
        {
            return new ExampleImpl<>(key, accessor, migrations, example);
        }
    }
    
    //
    //  Implementations
    //
    
    static class MaybeImpl<V> implements YamlValue<V>
    {
        final String key;
        final YamlAccessor<V> accessor;
        final List<Migration> migrations;
    
        MaybeImpl(String key, YamlAccessor<V> accessor, @NullOr List<Migration> migrations)
        {
            this.key = key;
            this.accessor = accessor;
            this.migrations = (migrations == null) ? List.of() : List.copyOf(migrations);
        }
    
        @Override
        public String key() { return key; }
    
        @Override
        public List<Migration> migrations() { return migrations; }
        
        @Override
        public Optional<V> get(ConfigurationSection storage) { return accessor.get(storage, key); }
        
        @Override
        public void set(ConfigurationSection storage, @NullOr V value) { accessor.set(storage, key, value); }
    }
    
    static class DefaultImpl<V> extends MaybeImpl<V> implements DefaultYamlValue<V>
    {
        V def;
    
        DefaultImpl(String key, YamlAccessor<V> accessor, @NullOr List<Migration> migrations, V def)
        {
            super(key, accessor, migrations);
            this.def = Objects.requireNonNull(def, "def");
        }
        
        @SuppressWarnings("NullableProblems")
        @Override
        public V getDefaultValue() { return def; }
    }
    
    static class ExampleImpl<V> extends DefaultImpl<V> implements ExampleYamlValue<V>
    {
        ExampleImpl(String key, YamlAccessor<V> accessor, @NullOr List<Migration> migrations, V example)
        {
            super(key, accessor, migrations, example);
        }
    }
}
