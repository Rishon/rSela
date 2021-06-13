package net.rishon.site.rsela;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.rishon.site.rsela.commands.messages.Message;
import net.rishon.site.rsela.commands.proxy.*;
import net.rishon.site.rsela.filemanager.ConfigHandler;
import net.rishon.site.rsela.listeners.Connections;
import net.rishon.site.rsela.listeners.ProxyPing;
import net.rishon.site.rsela.utils.Globals;

import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

@Plugin(
        id = "rsela",
        name = "rSela",
        version = "1.0.0",
        description = "Your all-in one plugin for a Velocity network server.",
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

        if (config.getBoolean("Commands.Alert.enabled")) {
            server.getCommandManager().register(new Alert(server), config.getString("Commands.Alert.command"));
            List<String> Aliases = config.getStringList("Commands.Alert.aliases");
            for(String cmd : Aliases) {
                server.getCommandManager().register(new Alert(server), cmd);
            }
        }
        if (config.getBoolean("Commands.Find.enabled")) {
            server.getCommandManager().register(new Find(server), config.getString("Commands.Find.command"));
            List<String> Aliases = config.getStringList("Commands.Find.aliases");
            for(String cmd : Aliases) {
                server.getCommandManager().register(new Find(server), cmd);
            }
        }
        if (config.getBoolean("Commands.Send.enabled")) {
            server.getCommandManager().register(new Send(server), config.getString("Commands.Send.command"));
            List<String> Aliases = config.getStringList("Commands.Send.aliases");
            for(String cmd : Aliases) {
                server.getCommandManager().register(new Send(server), cmd);
            }
        }
        if (config.getBoolean("Commands.ServerSend.enabled")) {
            server.getCommandManager().register(new ServerSend(server), config.getString("Commands.ServerSend.command"));
            List<String> Aliases = config.getStringList("Commands.ServerSend.aliases");
            for(String cmd : Aliases) {
                server.getCommandManager().register(new ServerSend(server), cmd);
            }
        }
        if (config.getBoolean("Commands.Maintenance.enabled")) {
            server.getCommandManager().register(new Maintenance(), config.getString("Commands.Maintenance.command"));
            List<String> Aliases = config.getStringList("Commands.Maintenance.aliases");
            for(String cmd : Aliases) {
                server.getCommandManager().register(new Maintenance(), cmd);
            }
        }
        if (config.getBoolean("Commands.IP.enabled")) {
            server.getCommandManager().register(new IP(server), config.getString("Commands.IP.command"));
            List<String> Aliases = config.getStringList("Commands.IP.aliases");
            for(String cmd : Aliases) {
                server.getCommandManager().register(new IP(server), cmd);
            }
        }
        if (config.getBoolean("Commands.StaffChat.enabled")) {
            server.getCommandManager().register(new StaffChat(server), config.getString("Commands.StaffChat.command"));
            List<String> Aliases = config.getStringList("Commands.StaffChat.aliases");
            for(String cmd : Aliases) {
                server.getCommandManager().register(new StaffChat(server), cmd);
            }
        }
        if (config.getBoolean("Commands.Message.enabled")) {
            server.getCommandManager().register(new Message(server), config.getString("Commands.Message.command"));
            List<String> Aliases = config.getStringList("Commands.Message.aliases");
            for(String cmd : Aliases) {
                server.getCommandManager().register(new Message(server), cmd);
            }
        }
    }

    private void registerListeners() {
        server.getEventManager().register(this, new Connections());
        server.getEventManager().register(this, new ProxyPing());
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent e) {

        instance = null;

        logger.info(Globals.rSela_prefix + "Shutting down rSela...");
    }
}
