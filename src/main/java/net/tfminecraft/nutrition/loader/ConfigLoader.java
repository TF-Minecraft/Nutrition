package net.tfminecraft.nutrition.loader;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.tfminecraft.nutrition.Cache;

public class ConfigLoader {
    public void loadConfig(File configFile) {
		FileConfiguration config = new YamlConfiguration();
        try {
        	config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Cache.maxTrack = config.getInt("max-track", 32);
        Cache.minTrack = config.getInt("min-track", 16);
	}
}
