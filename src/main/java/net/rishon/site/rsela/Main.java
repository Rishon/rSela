package net.rishon.site.rsela;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.rishon.site.rsela.commands.proxy.Alert;
import net.rishon.site.rsela.commands.proxy.rSela;
import net.rishon.site.rsela.filemanager.ConfigHandler;
import net.rishon.site.rsela.utils.Globals;

import java.nio.file.Path;
import java.util.logging.Logger;

@Plugin(
        id = "rsela",
        name = "rSela",
        version = "1.0.0",
        description = "An all-in one plugin for a velocity network server.",
        url = "rishon.site",
        authors = {"Rishon"}
)
public class Main {

    private static Main instance;

    public final ProxyServer server;
    private final Logger logger;
    public static Path dataDirectory;
    public static Configuration config;

    public static Main getInstance() {
        return instance;
    }

    @Inject
    public Main(ProxyServer server, Logger logger, @DataDirectory Path cfgDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = cfgDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        if (!ConfigHandler.loadConfig()) {
            logger.severe(Globals.rSela_prefix + "An error occurred while attempting to load config.yml");
            server.shutdown();
            return;
        }

        registerCommands();
        registerListeners();

        logger.info(Globals.rSela_prefix + "Loaded rSela plugin.");

    }

    private void registerCommands() {
        server.getCommandManager().register(new rSela(), "rsela");
        server.getCommandManager().register(new Alert(server), config.getString("Alert.command"));
    }

    private void registerListeners() {

    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent e) {

        instance = null;

        logger.info(Globals.rSela_prefix + "Shutting down rSela...");
    }
}
