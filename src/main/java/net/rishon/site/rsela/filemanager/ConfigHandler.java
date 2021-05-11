package net.rishon.site.rsela.filemanager;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.rishon.site.rsela.Main;

import java.io.*;

public class ConfigHandler {

    public static File getFile(String resource) {

        File folder = Main.dataDirectory.toFile();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = Main.class.getClassLoader().getResourceAsStream("config.yml");
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }

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

}
