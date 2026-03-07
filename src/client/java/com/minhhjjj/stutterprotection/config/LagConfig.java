package com.minhhjjj.stutterprotection.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LagConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "stutterprotection.json");

    public static boolean SHOW_HUD = true;

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                ConfigData data = GSON.fromJson(reader, ConfigData.class);
                if (data != null) {
                    SHOW_HUD = data.showHud;
                }
            } catch (IOException e) {
                System.err.println("Không thể đọc file config của Stutter Protection!");
                e.printStackTrace();
            }
        } else {
            save(); 
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            ConfigData data = new ConfigData();
            data.showHud = SHOW_HUD;
            GSON.toJson(data, writer);
        } catch (IOException e) {
            System.err.println("Không thể lưu file config của Stutter Protection!");
            e.printStackTrace();
        }
    }

    private static class ConfigData {
        public boolean showHud = SHOW_HUD;
    }
}