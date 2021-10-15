package net.rishon.codes.rsela.filemanager;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.rishon.codes.rsela.Main;

import java.io.*;

public class FileHandler {

    public static File getFile(String resource) {

        File folder = Main.dataDirectory.toFile();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                if (resource.equalsIgnoreCase("config.yml")) {
                    try (InputStream in = Main.class.getClassLoader().getResourceAsStream("config.yml");
                         OutputStream out = new FileOutputStream(resourceFile)) {
                        ByteStreams.copy(in, out);
                    }
                } else if (resource.equalsIgnoreCase("data.yml")) {
                    try (InputStream in = Main.class.getClassLoader().getResourceAsStream("data.yml");
                         OutputStream out = new FileOutputStream(resourceFile)) {
                        ByteStreams.copy(in, out);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }

    // Config File

    public static boolean loadConfig() {
        try {
            Main.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(getFile("config.yml"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(Main.config, new File(Main.dataDirectory.toFile(), "config.yml"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Configuration getConfig() {
        return Main.config;
    }

    // Data file

    public static boolean loadData() {
        try {
            Main.data = ConfigurationProvider.getProvider(YamlConfiguration.class).load(getFile("data.yml"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveData() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(Main.data, new File(Main.dataDirectory.toFile(), "data.yml"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Configuration getData() {
        return Main.data;
    }

}
