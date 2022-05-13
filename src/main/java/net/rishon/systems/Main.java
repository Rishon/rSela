package net.rishon.systems;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Data;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.config.Configuration;
import net.rishon.systems.datamanager.dataManager;
import net.rishon.systems.filemanager.FileHandler;
import net.rishon.systems.handler.MainHandler;
import net.rishon.systems.utils.Permissions;

import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

@Data
@Plugin(id = "rsela", name = "rSela", version = "1.0.4", description = "Your all-in one plugin for a Velocity network server.", url = "rishon.codes", authors = {"Rishon"})
public class Main {


    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    // MiniMessage Library
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    public Main instance;
    // File Handlers
    public FileHandler fileHandler;
    public Configuration config, data;
    // Permissions
    public Permissions permissions;
    // DataManager
    public dataManager dataManager;
    private MainHandler handler;

    @Inject
    public Main(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    private void onProxyInitialization(ProxyInitializeEvent event) {
        this.instance = this;

        this.fileHandler = new FileHandler(this);

        if (!this.fileHandler.loadConfig()) {
            this.getLogger().severe("An error occurred while attempting to load config.yml");
            this.getServer().shutdown();
            return;
        }
        if (!this.fileHandler.loadData()) {
            this.getLogger().severe("An error occurred while attempting to load data.yml");
            this.getServer().shutdown();
            return;
        }

        // TO:DO Connect to rishon.codes web-server to receive live-verison of the plugin.
        this.config.set("version", "1.0.4");

        handler = new MainHandler(this);

        init();

        this.logger.log(Level.INFO, "Loaded rSela plugin.");

    }

    @Subscribe
    private void onProxyShutdown(ProxyShutdownEvent event) {
        stop();
        this.logger.log(Level.INFO, permissions.rSela_prefix + "Shutting down rSela...");
    }

    void init() {
        handler.init();
    }

    void stop() {
        handler.stop();
    }

}
