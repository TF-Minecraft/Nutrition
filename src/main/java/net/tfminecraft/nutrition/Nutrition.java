package net.tfminecraft.nutrition;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.tfminecraft.nutrition.commands.NutritionCommand;
import net.tfminecraft.nutrition.loader.ConfigLoader;
import net.tfminecraft.nutrition.loader.GroupLoader;
import net.tfminecraft.nutrition.loader.LevelLoader;
import net.tfminecraft.nutrition.manager.NutritionManager;

public class Nutrition extends JavaPlugin{
	public static FileConfiguration config;
	public static Nutrition plugin;
	//Classes
	private final ConfigLoader configLoader = new ConfigLoader();
	private final GroupLoader groupLoader = new GroupLoader();
	private final LevelLoader levelLoader = new LevelLoader();
	
	private final NutritionManager nutritionManager = new NutritionManager();
	//private final InventoryManager inventoryManager = new InventoryManager();
	
	@Override
	public void onEnable() {
		config = getConfig();
		plugin = this;
		createFolders();
		createConfigs();
		registerListeners();
		loadConfigs();
        for(Player p : Bukkit.getOnlinePlayers()) {
            nutritionManager.load(p);
        }
		getCommand("nutrition").setExecutor(new NutritionCommand());
	}
	@Override
	public void onDisable() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            nutritionManager.save(p);
        }
	}
	public void loadConfigs() {
		configLoader.loadConfig(new File(getDataFolder(), "config.yml"));
		groupLoader.load(new File(getDataFolder(), "groups.yml"));
		levelLoader.load(new File(getDataFolder(), "levels.yml"));
	}
	public void registerListeners() {
		getServer().getPluginManager().registerEvents(nutritionManager, this);
	}
	public void createFolders() {
		if (!getDataFolder().exists()) getDataFolder().mkdir();
		File subFolder = new File(getDataFolder(), "PlayerData");
		if(!subFolder.exists()) subFolder.mkdir();
	}
	public void createConfigs() {
		String[] files = {
				"config.yml",
				"levels.yml",
				"groups.yml"
				};
		for(String s : files) {
			File newConfigFile = new File(getDataFolder(), s);
	        if (!newConfigFile.exists()) {
	        	newConfigFile.getParentFile().mkdirs();
	            saveResource(s, false);
	        }
		}
	}
    
}
