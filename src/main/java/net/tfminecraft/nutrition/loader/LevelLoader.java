package net.tfminecraft.nutrition.loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.tfminecraft.nutrition.data.NutritionLevel;

public class LevelLoader {
    public static List<NutritionLevel> oList = new ArrayList<>();
	public static List<NutritionLevel> get(){
		return oList;
	}
    public static NutritionLevel getDefault() {
        for(NutritionLevel n : oList) {
			if(n.isDefault()) return n;
		}
		return oList.get(0);
    }
	public static NutritionLevel getByString(String id) {
		for(NutritionLevel n : oList) {
			if(n.getId().equalsIgnoreCase(id)) return n;
		}
		return null;
	}
    public static int getMaxLevel() {
        double level = 1.0;
        for(NutritionLevel l : oList) {
            if(l.getLevel() > level) level = l.getLevel();
        }
        return (int) Math.round(level);
    }
    public static NutritionLevel getByLevel(double level) {
        NutritionLevel result = getDefault();
        double maxLevelFound = Double.NEGATIVE_INFINITY;

        for (NutritionLevel n : oList) {
            double nLevel = n.getLevel();
            if (nLevel <= level && nLevel > maxLevelFound) {
                maxLevelFound = nLevel;
                result = n;
            }
        }

        return result;
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
			NutritionLevel n = new NutritionLevel(key, config.getConfigurationSection(key));
			oList.add(n);
		}
	}
}
