package net.tfminecraft.nutrition.manager;

import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.lib.api.item.NBTItem;

import org.bukkit.entity.Player;

import net.tfminecraft.nutrition.Cache;
import net.tfminecraft.nutrition.Database;
import net.tfminecraft.nutrition.data.FoodGroup;
import net.tfminecraft.nutrition.data.PlayerData;
import net.tfminecraft.nutrition.loader.GroupLoader;

public class NutritionManager implements Listener{
    private static HashMap<Player, PlayerData> data = new HashMap<>();

    public static PlayerData get(Player p) {
        if (!data.containsKey(p)) {
            data.put(p, new PlayerData(p));
        }
        return data.get(p);
    }

    public static String getByItemStack(ItemStack i) {
        NBTItem nbt = NBTItem.get(i);
        if(!nbt.hasType()) {
            if(Cache.vanillaFoods.contains(i.getType().toString().toLowerCase())) return "vanilla";
            return "none";
        }
        return nbt.getString("MMOITEMS_ITEM_ID").toLowerCase();
    }

    public void load(Player p) {
        if(data.containsKey(p)) return;
        //TODO database
        data.put(p, new PlayerData(p));
    }
    public void save(Player p) {
        PlayerData pd = get(p);
        if(pd == null) return;
        Database.saveItemHistory(p.getUniqueId(), pd.getItemHistory());
        data.remove(p);
    }
    //join set up
    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        load(p);
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        save(p);
    }

    @EventHandler
    public void eat(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        PlayerData pd = get(p);

        ItemStack i = e.getItem();
        String item = getByItemStack(i);
        FoodGroup group = GroupLoader.getByItemString(item);
        if(group == null) return;
        pd.log(item, group);
    }
}
