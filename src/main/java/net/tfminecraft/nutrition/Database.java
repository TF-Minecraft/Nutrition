package net.tfminecraft.nutrition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Database {
    private static final File dataFolder = new File("plugins/Nutrition/PlayerData");
    private static final Gson gson = new Gson();

    public static void saveItemHistory(UUID uuid, List<String> itemHistory) {
        if (!dataFolder.exists()) dataFolder.mkdirs();

        File file = new File(dataFolder, uuid.toString() + ".json");

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(itemHistory, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> loadItemHistory(UUID uuid) {
        File file = new File(dataFolder, uuid.toString() + ".json");

        if (!file.exists()) return new ArrayList<>();

        try {
            String json = new String(Files.readAllBytes(file.toPath()));
            return gson.fromJson(json, new TypeToken<List<String>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
