package net.tfminecraft.nutrition.loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import net.tfminecraft.nutrition.data.FoodGroup;

public class GroupLoader {
    public static List<FoodGroup> oList = new ArrayList<>();
	public static List<FoodGroup> get(){
		return oList;
	}
	public static FoodGroup getByString(String id) {
		for(FoodGroup g : oList) {
			if(g.getId().equalsIgnoreCase(id)) return g;
		}
		return null;
	}
	public void load(File configFile) {
		FileConfiguration config = new YamlConfiguration();
        try {
        	config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
		Set<String> set = config.getKeys(false);

		List<String> list = new ArrayList<String>(set);
		
		for(String key : list) {
			FoodGroup g = new FoodGroup(key, config.getConfigurationSection(key));
			oList.add(g);
		}
	}

    public static FoodGroup getByItemString(String s) {
        for(FoodGroup g : oList) {
            if(g.contains(s)) return g;
        }
        return null;
    }
}
