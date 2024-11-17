package community.leaf.examples.configvalues.bukkit;

import org.bukkit.plugin.java.JavaPlugin;
import pl.tlinkowski.annotation.basic.NullOr;

import java.nio.file.Path;

public class ExampleConfigPlugin extends JavaPlugin {
	private @NullOr Path dataDirectory;
	private @NullOr Path backupsDirectory;
	private @NullOr Config config;
	
	@Override
	public void onEnable() {
		this.dataDirectory = getDataFolder().toPath();
		this.backupsDirectory = dataDirectory.resolve("backups");
		
		this.config = new Config(this);
	}
	
	private <T> T initialized(@NullOr T thing, String name) {
		if (thing != null) {
			return thing;
		}
		throw new IllegalStateException(name + " is not initialized yet");
	}
	
	public Path directory() {
		return initialized(dataDirectory, "dataDirectory");
	}
	
	public Path backups() {
		return initialized(backupsDirectory, "backupsDirectory");
	}
	
	public Config config() {
		return initialized(config, "config");
	}
}
