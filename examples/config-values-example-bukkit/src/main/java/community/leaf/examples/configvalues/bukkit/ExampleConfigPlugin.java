package community.leaf.examples.configvalues.bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import pl.tlinkowski.annotation.basic.NullOr;

import java.nio.file.Path;

public class ExampleConfigPlugin extends JavaPlugin implements Listener {
	private @NullOr Path dataDirectory;
	private @NullOr Path backupsDirectory;
	private @NullOr Config config;
	
	@Override
	public void onEnable() {
		this.dataDirectory = getDataFolder().toPath();
		this.backupsDirectory = dataDirectory.resolve("backups");
		
		this.config = new Config(this);
		
		getServer().getPluginManager().registerEvents(this, this);
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
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.getPlayer().playSound(event.getPlayer(), config().getOrDefault(Config.JOIN_SOUND), 1.0F, 1.0F);
	}
}
