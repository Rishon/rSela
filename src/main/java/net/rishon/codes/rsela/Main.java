package net.rishon.codes.rsela;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.rsela.commands.messages.Message;
import net.rishon.codes.rsela.commands.messages.MessageToggle;
import net.rishon.codes.rsela.commands.proxy.*;
import net.rishon.codes.rsela.filemanager.FileHandler;
import net.rishon.codes.rsela.listeners.Connections;
import net.rishon.codes.rsela.listeners.ProxyPing;
import net.rishon.codes.rsela.utils.Lists;
import net.rishon.codes.rsela.utils.Permissions;

import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

@Plugin(
        id = "rsela",
        name = "rSela",
        version = "1.0.2",
        description = "Your all-in one plugin for a Velocity network server.",
        url = "rishon.codes",
        authors = {"Rishon"}
)
public class Main {

    private static Main instance;

    public final ProxyServer server;
    private final Logger logger;
    public static Path dataDirectory;
    public static Configuration config, data;

    public static Main getInstance() {
        return instance;
    }

    @Inject
    public Main(ProxyServer server, Logger logger, @DataDirectory Path configDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = configDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        if (!FileHandler.loadConfig()) {
            logger.severe(Permissions.rSela_prefix + "An error occurred while attempting to load config.yml");
            server.shutdown();
            return;
        } else if (!FileHandler.loadData()) {
            logger.severe(Permissions.rSela_prefix + "An error occurred while attempting to load data.yml");
            server.shutdown();
            return;
        }

        registerCommands();
        registerListeners();

        Lists.toggled_messages = data.getStringList("toggled_messages");

        logger.info(Permissions.rSela_prefix + "Loaded rSela plugin.");

    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent e) {

        data.set("toggled_messages", Lists.toggled_messages);
        FileHandler.saveData();

        instance = null;

        logger.info(Permissions.rSela_prefix + "Shutting down rSela...");
    }

    private void registerListeners() {
        server.getEventManager().register(this, new Connections());
        server.getEventManager().register(this, new ProxyPing());
    }

    private void registerCommands() {

        server.getCommandManager().register("rsela", new rSela());

        if (config.getBoolean("Commands.Alert.enabled")) {
            server.getCommandManager().register(config.getString("Commands.Alert.command"), new Alert(server));
            List<String> Aliases = config.getStringList("Commands.Alert.aliases");
            for (String cmd : Aliases) {
                server.getCommandManager().register(cmd, new Alert(server));
            }
        }
        if (config.getBoolean("Commands.Find.enabled")) {
            server.getCommandManager().register(config.getString("Commands.Find.command"), new Find(server));
            List<String> Aliases = config.getStringList("Commands.Find.aliases");
            for (String cmd : Aliases) {
                server.getCommandManager().register(cmd, new Find(server));
            }
        }
        if (config.getBoolean("Commands.Send.enabled")) {
            server.getCommandManager().register(config.getString("Commands.Send.command"), new Send(server));
            List<String> Aliases = config.getStringList("Commands.Send.aliases");
            for (String cmd : Aliases) {
                server.getCommandManager().register(cmd, new Send(server));
            }
        }
        if (config.getBoolean("Commands.ServerSend.enabled")) {
            server.getCommandManager().register(config.getString("Commands.ServerSend.command"), new ServerSend(server));
            List<String> Aliases = config.getStringList("Commands.ServerSend.aliases");
            for (String cmd : Aliases) {
                server.getCommandManager().register(cmd, new ServerSend(server));
            }
        }
        if (config.getBoolean("Commands.Maintenance.enabled")) {
            server.getCommandManager().register(config.getString("Commands.Maintenance.command"), new Maintenance());
            List<String> Aliases = config.getStringList("Commands.Maintenance.aliases");
            for (String cmd : Aliases) {
                server.getCommandManager().register(cmd, new Maintenance());
            }
        }
        if (config.getBoolean("Commands.IP.enabled")) {
            server.getCommandManager().register(config.getString("Commands.IP.command"), new IP(server));
            List<String> Aliases = config.getStringList("Commands.IP.aliases");
            for (String cmd : Aliases) {
                server.getCommandManager().register(cmd, new IP(server));
            }
        }
        if (config.getBoolean("Commands.StaffChat.enabled")) {
            server.getCommandManager().register(config.getString("Commands.StaffChat.command"), new StaffChat(server));
            List<String> Aliases = config.getStringList("Commands.StaffChat.aliases");
            for (String cmd : Aliases) {
                server.getCommandManager().register(cmd, new StaffChat(server));
            }
        }
        if (config.getBoolean("Commands.Message.enabled")) {
            server.getCommandManager().register(config.getString("Commands.Message.command"), new Message(server));
            List<String> Aliases = config.getStringList("Commands.Message.aliases");
            for (String cmd : Aliases) {
                server.getCommandManager().register(cmd, new Message(server));
            }
        }
        if (config.getBoolean("Commands.ClearChat.enabled")) {
            server.getCommandManager().register(config.getString("Commands.ClearChat.command"), new ClearChat(server));
            List<String> Aliases = config.getStringList("Commands.ClearChat.aliases");
            for (String cmd : Aliases) {
                server.getCommandManager().register(cmd, new ClearChat(server));
            }
        }
        if (config.getBoolean("Commands.MuteServer.enabled")) {
            server.getCommandManager().register(config.getString("Commands.MuteServer.command"), new MuteServer(server));
            List<String> Aliases = config.getStringList("Commands.MuteServer.aliases");
            for (String cmd : Aliases) {
                server.getCommandManager().register(cmd, new MuteServer(server));
            }
        }
        if (config.getBoolean("Commands.MessageToggle.enabled")) {
            server.getCommandManager().register(config.getString("Commands.MessageToggle.command"), new MessageToggle(server));
            List<String> Aliases = config.getStringList("Commands.MessageToggle.aliases");
            for (String cmd : Aliases) {
                server.getCommandManager().register(cmd, new MessageToggle(server));
            }
        }
    }
}
