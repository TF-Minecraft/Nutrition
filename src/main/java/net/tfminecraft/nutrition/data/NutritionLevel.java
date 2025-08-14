package net.tfminecraft.nutrition.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import me.Plugins.TLibs.Objects.API.SubAPI.StringFormatter;

public class NutritionLevel {
    private String id;
    private String name;
    private double level;
    private List<String> effects = new ArrayList<>();
    private boolean base;

    public NutritionLevel(String key, ConfigurationSection config) {
        id = key;
        name = StringFormatter.formatHex(config.getString("name", key));
        level = config.getDouble("level", 5.0);
        base = config.getBoolean("default", false);
        if(config.contains("effects")) effects = config.getStringList("effects");
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getLevel() {
        return level;
    }
    public List<String> getEffects() {
        return effects;
    }
    public boolean isDefault() {
        return base;
    }
}
