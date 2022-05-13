package net.rishon.systems.filemanager;

import com.google.common.io.ByteStreams;
import lombok.Getter;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.rishon.systems.Main;

import java.io.*;

@Getter
public class FileHandler {

    private final Main instance;

    public FileHandler(Main instance) {
        this.instance = instance;
    }

    private File getFile(String resource) {

        File folder = instance.getDataDirectory().toFile();
        if (!folder.exists()) folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                if (resource.equalsIgnoreCase("config.yml")) {
                    try (InputStream in = Main.class.getClassLoader().getResourceAsStream("config.yml"); OutputStream out = new FileOutputStream(resourceFile)) {
                        ByteStreams.copy(in, out);
                    }
                }
                if (resource.equalsIgnoreCase("data.yml")) {
                    try (InputStream in = Main.class.getClassLoader().getResourceAsStream("data.yml"); OutputStream out = new FileOutputStream(resourceFile)) {
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
    public boolean loadConfig() {
        try {
            Main.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(getFile("config.yml"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(Main.config, new File(instance.getDataDirectory().toFile(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Data file
    public boolean loadData() {
        try {
            Main.data = ConfigurationProvider.getProvider(YamlConfiguration.class).load(getFile("data.yml"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveData() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(Main.data, new File(instance.getDataDirectory().toFile(), "data.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
