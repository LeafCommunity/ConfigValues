package community.leaf.examples.configvalues.bukkit;

import com.rezzedup.util.constants.Aggregates;
import com.rezzedup.util.constants.annotations.AggregatedResult;
import community.leaf.configvalues.bukkit.DefaultYamlValue;
import community.leaf.configvalues.bukkit.ExampleYamlValue;
import community.leaf.configvalues.bukkit.migrations.Migration;
import community.leaf.configvalues.bukkit.YamlValue;
import community.leaf.configvalues.bukkit.data.YamlDataFile;

import java.util.List;

public class Config extends YamlDataFile
{
    public static final YamlValue<String> VERSION = YamlValue.ofString("version").maybe();
    
    public static final DefaultYamlValue<String> JOIN_MESSAGE =
        YamlValue.ofString("messages.join")
            .defaults("Hey there %player%!\nWelcome back.");
    
    public static final DefaultYamlValue<String> HELLO_MESSAGE =
        YamlValue.ofString("messages.hello-world")
            .migrates(
                Migration.move("messages.hi"),
                Migration.move("messages.hello")
            )
            .defaults("Hello world.");
    
    public static final ExampleYamlValue<String> EXAMPLE_MESSAGE =
        YamlValue.ofString("messages.example").example("I only get set once!");
    
    @AggregatedResult
    private static final List<YamlValue<?>> VALUES =
        Aggregates.fromThisClass().constantsOfType(YamlValue.type()).toList();
    
    public Config(ExampleConfigPlugin plugin)
    {
        super(plugin.directory(), "config.yml");
        
        reloadsWith(() ->
        {
            if (isInvalid()) { return; }
        
            String configVersion = get(VERSION).orElse("");
            String pluginVersion = plugin.getDescription().getVersion();
            
            boolean isOutdated = !configVersion.equals(pluginVersion);
            
            if (isOutdated) { set(VERSION, pluginVersion); }
        
            headerFromResource("config.header.txt");
            defaultValues(VALUES);
            
            if (isOutdated) { migrateValues(VALUES, data()); }
            if (isUpdated()) { backupThenSave(plugin.backups(), "v" + configVersion); }
        });
    }
}
