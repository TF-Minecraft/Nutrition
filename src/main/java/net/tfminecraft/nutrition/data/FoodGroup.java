package net.tfminecraft.nutrition.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

public class FoodGroup {
    private String id;
    private double level;
    private List<String> items = new ArrayList<>();

    public FoodGroup(String key, ConfigurationSection config) {
        id = key;
        level = config.getDouble("level", 5.0);
        if(config.contains("items")) items = config.getStringList("items");
    }

    public String getId() {
        return id;
    }
    public double getLevel() {
        return level;
    }
    public boolean contains(String s) {
        return items.contains(s);
    }
    public List<String> getItems() {
        return items;
    }
}
