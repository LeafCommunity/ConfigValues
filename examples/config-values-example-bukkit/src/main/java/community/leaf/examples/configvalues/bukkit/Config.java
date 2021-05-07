package community.leaf.examples.configvalues.bukkit;

import com.rezzedup.util.constants.Aggregates;
import com.rezzedup.util.constants.annotations.Aggregated;
import community.leaf.configvalues.bukkit.DefaultYamlValue;
import community.leaf.configvalues.bukkit.YamlValue;
import community.leaf.configvalues.bukkit.data.YamlDataFile;

import java.util.List;

public class Config extends YamlDataFile
{
    public static final YamlValue<String> VERSION = YamlValue.ofString("version").maybe();
    
    public static final DefaultYamlValue<String> JOIN_MESSAGE =
        YamlValue.ofString("messages.join").defaults("Hey there %player%!\nWelcome back.");
    
    @Aggregated.Result
    private static final List<YamlValue<?>> VALUES =
        Aggregates.list(Config.class, YamlValue.type(), Aggregates.matching().all());
    
    private final ExampleConfigPlugin plugin;
    
    public Config(ExampleConfigPlugin plugin)
    {
        super(plugin.directory(), "config.yml");
        this.plugin = plugin;
        handleReload();
    }
    
    @Override
    protected void handleReload()
    {
        if (isInvalid()) { return; }
        
        String configVersion = get(VERSION).orElse("");
        String pluginVersion = plugin.getDescription().getVersion();
        
        boolean isOutdated = !configVersion.equals(pluginVersion);
        
        if (isOutdated)
        {
            set(VERSION, pluginVersion);
        }
        
        setupHeader("config.header.txt");
        setupDefaults(VALUES);
        
        if (isUpdated())
        {
            backupThenSave(plugin.backups(), "v" + configVersion);
        }
    }
}
