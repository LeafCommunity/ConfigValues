package community.leaf.configvalues.bukkit;

import com.rezzedup.util.valuables.KeyGetter;
import com.rezzedup.util.valuables.KeySetter;
import org.bukkit.configuration.ConfigurationSection;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Objects;
import java.util.Optional;

public interface YamlAccessor<V> extends KeyGetter<ConfigurationSection, String, V>, KeySetter<ConfigurationSection, String, V>
{
    static <V> YamlAccessor<V> adapts(YamlAdapter<V> adapter)
    {
        Objects.requireNonNull(adapter, "adapter");
        
        return new YamlAccessor<>()
        {
            @Override
            public Optional<V> get(ConfigurationSection storage, String key)
            {
                return Optional.ofNullable(storage.get(key)).flatMap(adapter::deserialize);
            }
    
            @Override
            public void set(ConfigurationSection storage, String key, @NullOr V updated)
            {
                storage.set(key, Optional.ofNullable(updated).flatMap(adapter::serialize).orElse(null));
            }
        };
    }
    
    static <V> YamlAccessor<V> directly(KeyGetter<ConfigurationSection, String, V> getter, KeySetter<ConfigurationSection, String, V> setter)
    {
        Objects.requireNonNull(getter, "getter");
        Objects.requireNonNull(setter, "setter");
        
        return new YamlAccessor<>()
        {
            @Override
            public Optional<V> get(ConfigurationSection storage, String key)
            {
                return getter.get(storage, key);
            }
    
            @Override
            public void set(ConfigurationSection storage, String key, @NullOr V updated)
            {
                setter.set(storage, key, updated);
            }
        };
    }
}
