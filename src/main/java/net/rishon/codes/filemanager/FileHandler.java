package net.rishon.codes.filemanager;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.rishon.codes.Main;

import java.io.*;
import java.nio.file.Path;

public class FileHandler {

    private final Path directory;

    public FileHandler(Path directory) {
        this.directory = directory;
    }

    private File getFile(String resource) {

        File folder = directory.toFile();
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
            Main.getInstance().config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(getFile("config.yml"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(Main.getInstance().config, new File(directory.toFile(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getConfig() {
        return Main.getInstance().config;
    }

    // Data file

    public boolean loadData() {
        try {
            Main.getInstance().data = ConfigurationProvider.getProvider(YamlConfiguration.class).load(getFile("data.yml"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveData() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(Main.getInstance().data, new File(directory.toFile(), "data.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getData() {
        return Main.getInstance().data;
    }

}
