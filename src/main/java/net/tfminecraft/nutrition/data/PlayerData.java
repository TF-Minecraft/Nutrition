package net.tfminecraft.nutrition.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import net.tfminecraft.tlibs.objects.api.subapi.StringFormatter;
import net.Indyuce.mmocore.api.player.attribute.PlayerAttributes.AttributeInstance;
import net.tfminecraft.nutrition.Cache;
import net.tfminecraft.nutrition.Database;
import net.tfminecraft.nutrition.loader.GroupLoader;
import net.tfminecraft.nutrition.loader.LevelLoader;

public class PlayerData {
    private Player player;
    private List<FoodGroup> history = new ArrayList<>();
    private List<String> itemHistory = new ArrayList<>();

    private NutritionLevel level;
    private boolean initated = false;

    public PlayerData(Player p) {
        player = p;
        level = LevelLoader.getDefault();
        this.itemHistory = Database.loadItemHistory(p.getUniqueId());
        this.history = new ArrayList<>();

        int removed = 0;
        Iterator<String> iterator = itemHistory.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            FoodGroup group = GroupLoader.getByItemString(item);
            if (group == null) {
                iterator.remove(); // Remove bad entry
                removed++;
            } else {
                history.add(group);
            }
        }

        /*
        // Debug output
        player.sendMessage("§7[§bNutrition§7] §aLoaded " + itemHistory.size() + " food items.");
        player.sendMessage("§7[§bNutrition§7] §e" + history.size() + " valid FoodGroups added to history.");
        if (removed > 0) {
            player.sendMessage("§7[§bNutrition§7] §c" + removed + " outdated entries were removed.");
        }
        */
        calculate(false);
    }


    public Player getPlayer() {
        return player;
    }

    public List<FoodGroup> getHistory() {
        return history;
    }

    public List<String> getItemHistory() {
        return itemHistory;
    }

    public NutritionLevel getLevel() {
        return level;
    }
 
    public void log(String s, FoodGroup group) {
        history.add(0, group); // Add to front of queue
        if (history.size() > Cache.maxTrack) {
            history.remove(history.size() - 1); // Remove oldest if over capacity
        }
        itemHistory.add(0, s); // Add to front of queue
        if (itemHistory.size() > Cache.maxTrack) {
            itemHistory.remove(itemHistory.size() - 1); // Remove oldest if over capacity
        }
        calculate(true);
    }

    private double getDiversityMultiplier() {
        if (itemHistory.isEmpty()) return 0.5;

        Map<String, Integer> counts = new HashMap<>();
        for (String food : itemHistory) {
            counts.put(food, counts.getOrDefault(food, 0) + 1);
        }

        int uniqueFoods = counts.size();
        double idealFoods = 4.0;
        double idealCountPerFood = (double) itemHistory.size() / idealFoods;

        double diversityScore = 0.0;
        int consideredFoods = 0;

        // Only consider the top 4 most frequent foods
        List<Integer> sortedCounts = new ArrayList<>(counts.values());
        sortedCounts.sort((a, b) -> Integer.compare(b, a)); // descending
        for (int i = 0; i < Math.min(4, sortedCounts.size()); i++) {
            double ratio = Math.min(sortedCounts.get(i) / idealCountPerFood, 1.0);
            diversityScore += ratio;
            consideredFoods++;
        }

        double normalized = diversityScore / idealFoods; // 0.0 to 1.0
        double multiplier = 0.5 + normalized * 1.5;       // scale to 0.5–2.0
        return Math.min(multiplier, 2.0);
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    public void info(boolean changed) {
        player.sendMessage(changed ? "§aYour diet is now "+level.getName() : "§aYour diet is "+level.getName());
        // Build colored nutrition level bar
        int totalBars = 40;
        int maxLevel = LevelLoader.getMaxLevel();
        double levelProgress = level.getLevel(); // Assumes level.getLevel() returns double from 1.0 to 16.0
        int filledLevelBars = (int) ((levelProgress / maxLevel) * totalBars); // totalBars = 40

        StringBuilder levelBar = new StringBuilder("§f[");
        for (int i = 0; i < totalBars; i++) {
            double t = (double) i / (totalBars - 1);
            int r = clamp((int) (0x73 + t * (0x14 - 0x73)));
            int g = clamp((int) (0x20 + t * (0xf2 - 0x20)));
            int b = clamp((int) (0x20 + t * (0x0c - 0x20)));

            String hex = String.format("#%02x%02x%02x", r, g, b);
            String color = StringFormatter.formatHex(hex);

            if (i < filledLevelBars) {
                levelBar.append(color).append("|"); // Filled with gradient
            } else {
                levelBar.append("§7|"); // Empty gray
            }
        }
        levelBar.append(StringFormatter.formatHex("#ffffff")).append("§f]");

        player.sendMessage("§eNutrition: " + levelBar.toString());
        // Build colored diversity bar
        double multiplier = getDiversityMultiplier();
        int filledBars = (int) ((multiplier - 0.5) / 1.5 * totalBars); // Normalize to 0–40

        StringBuilder bar = new StringBuilder("§f[");
        for (int i = 0; i < totalBars; i++) {
            // Linear interpolate from red to green
            double t = (double) i / (totalBars - 1);
            int r = clamp((int) (0x73 + t * (0x14 - 0x73)));
            int g = clamp((int) (0x20 + t * (0xf2 - 0x20)));
            int b = clamp((int) (0x20 + t * (0x0c - 0x20)));

            String hex = String.format("#%02x%02x%02x", r, g, b);
            String color = StringFormatter.formatHex(hex);
            if (i < filledBars) {
                bar.append(color).append("|"); // Filled, gradient-colored
            } else {
                bar.append("§7|"); // Unfilled, always light gray
            }
        }
        bar.append(StringFormatter.formatHex("#ffffff")).append("§f]"); // Close with neutral color
        player.sendMessage("§eDiversity: "+bar.toString());
    }

    public void calculate(boolean message) {
        NutritionLevel newLevel = LevelLoader.getDefault();
        if(!history.isEmpty() && itemHistory.size() >= Cache.minTrack) {
            double sum = 0;
            for (FoodGroup group : history) {
                sum += group.getLevel();
            }

            double average = sum / history.size();
            double multiplier = getDiversityMultiplier();
            double adjustedAverage = average * multiplier;
            
            /*
            player.sendMessage("Base Average: " + average);
            player.sendMessage("Diversity Multiplier: " + multiplier);
            player.sendMessage("Adjusted Average: " + adjustedAverage);
            */

            newLevel = LevelLoader.getByLevel(adjustedAverage);
        }
        if(level.getId().equalsIgnoreCase(newLevel.getId())) {
            if(!initated) {
                initated = true;
                apply();
            }
            return;
        }
        level = newLevel;
        if(message) {
            info(true);
        }
        apply();
    }

    public void apply() {
        for(String effect : level.getEffects()) {
            String type = effect.split("\\(")[0];
            String value = effect.split("\\(")[1].replace(")", "");
            if(type.equalsIgnoreCase("attribute")) {
                String a = value.split("\\.")[0];
                int amount = Integer.parseInt(value.split("\\.")[1]);
                net.Indyuce.mmocore.api.player.PlayerData pd = net.Indyuce.mmocore.api.player.PlayerData.get(player.getUniqueId());
                AttributeInstance attribute = pd.getAttributes().getInstance(a);
                attribute.setBase(amount);
            }
        }
    }
}
