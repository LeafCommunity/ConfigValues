package community.leaf.configvalues.bukkit;

import java.util.List;
import java.util.Map;
import java.util.UUID;

final class YamlAccessors
{
    private YamlAccessors() {}
    
    static final YamlAccessor<String> STRING = YamlAccessor.adapts(YamlAdapters.STRING);
    
    static final YamlAccessor<Boolean> BOOLEAN = YamlAccessor.adapts(YamlAdapters.BOOLEAN);
    
    static final YamlAccessor<Integer> INTEGER = YamlAccessor.adapts(YamlAdapters.INTEGER);
    
    static final YamlAccessor<Long> LONG = YamlAccessor.adapts(YamlAdapters.LONG);
    
    static final YamlAccessor<Float> FLOAT = YamlAccessor.adapts(YamlAdapters.FLOAT);
    
    static final YamlAccessor<Double> DOUBLE = YamlAccessor.adapts(YamlAdapters.DOUBLE);
    
    static final YamlAccessor<List<String>> STRING_LIST = YamlAccessor.adapts(YamlAdapters.STRING_LIST);
    
    static final YamlAccessor<List<Map<?, ?>>> MAP_LIST = YamlAccessor.adapts(YamlAdapters.MAP_LIST);
    
    static final YamlAccessor<UUID> U_UID = YamlAccessor.adapts(YamlAdapters.U_UID);
}
