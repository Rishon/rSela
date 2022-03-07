package net.rishon.codes;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.commands.messages.Message;
import net.rishon.codes.commands.messages.MessageToggle;
import net.rishon.codes.commands.proxy.*;
import net.rishon.codes.filemanager.FileHandler;
import net.rishon.codes.listeners.Connections;
import net.rishon.codes.listeners.ProxyPing;
import net.rishon.codes.utils.Lists;
import net.rishon.codes.utils.Permissions;

import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

@Plugin(id = "rsela", name = "rSela", version = "1.0.3", description = "Your all-in one plugin for a Velocity network server.", url = "rishon.codes", authors = {"Rishon"})
public class Main {

    public final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    public static Configuration config, data;

    // MiniMessage Library
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    // Permissions
    private final Permissions permissions = new Permissions();

    @Inject
    public Main(ProxyServer server, Logger logger, @DataDirectory Path configDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = configDirectory;
    }

    @Subscribe
    private void onProxyInitialization(ProxyInitializeEvent event) {

        if (!FileHandler.loadConfig()) {
            this.logger.severe(permissions.rSela_prefix + "An error occurred while attempting to load config.yml");
            this.server.shutdown();
            return;
        } else if (!FileHandler.loadData()) {
            this.logger.severe(permissions.rSela_prefix + "An error occurred while attempting to load data.yml");
            this.server.shutdown();
            return;
        }

        // Register plugin commands
        registerCommands();
        // Register plugin listeners
        registerListeners();

        new FileHandler(this.dataDirectory);

        Lists.toggled_messages = data.getStringList("toggled_messages");

        this.logger.info(permissions.rSela_prefix + "Loaded rSela plugin.");

    }

    @Subscribe
    private void onProxyShutdown(ProxyShutdownEvent event) {

        // Saving yaml data
        data.set("toggled_messages", Lists.toggled_messages);
        FileHandler.saveData();

        logger.info(permissions.rSela_prefix + "Shutting down rSela...");
    }

    private void registerListeners() {

        EventManager eventManager = this.server.getEventManager();

        eventManager.register(this, new Connections());
        eventManager.register(this, new ProxyPing(miniMessage));
    }

    private void registerCommands() {

        CommandManager command = this.server.getCommandManager();

        command.register("rsela", new rSela());

        if (config.getBoolean("Commands.Alert.enabled")) {
            command.register(config.getString("Commands.Alert.command"), new Alert(server));
            List<String> Aliases = config.getStringList("Commands.Alert.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new Alert(server));
            }
        }
        if (config.getBoolean("Commands.Find.enabled")) {
            command.register(config.getString("Commands.Find.command"), new Find(server));
            List<String> Aliases = config.getStringList("Commands.Find.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new Find(server));
            }
        }
        if (config.getBoolean("Commands.Send.enabled")) {
            command.register(config.getString("Commands.Send.command"), new Send(server));
            List<String> Aliases = config.getStringList("Commands.Send.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new Send(server));
            }
        }
        if (config.getBoolean("Commands.ServerSend.enabled")) {
            command.register(config.getString("Commands.ServerSend.command"), new ServerSend(server));
            List<String> Aliases = config.getStringList("Commands.ServerSend.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new ServerSend(server));
            }
        }
        if (config.getBoolean("Commands.Maintenance.enabled")) {
            command.register(config.getString("Commands.Maintenance.command"), new Maintenance());
            List<String> Aliases = config.getStringList("Commands.Maintenance.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new Maintenance());
            }
        }
        if (config.getBoolean("Commands.IP.enabled")) {
            command.register(config.getString("Commands.IP.command"), new IP(server));
            List<String> Aliases = config.getStringList("Commands.IP.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new IP(server));
            }
        }
        if (config.getBoolean("Commands.StaffChat.enabled")) {
            command.register(config.getString("Commands.StaffChat.command"), new StaffChat(server));
            List<String> Aliases = config.getStringList("Commands.StaffChat.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new StaffChat(server));
            }
        }
        if (config.getBoolean("Commands.Message.enabled")) {
            command.register(config.getString("Commands.Message.command"), new Message(server));
            List<String> Aliases = config.getStringList("Commands.Message.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new Message(server));
            }
        }
        if (config.getBoolean("Commands.ClearChat.enabled")) {
            command.register(config.getString("Commands.ClearChat.command"), new ClearChat(server));
            List<String> Aliases = config.getStringList("Commands.ClearChat.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new ClearChat(server));
            }
        }
        if (config.getBoolean("Commands.MuteServer.enabled")) {
            command.register(config.getString("Commands.MuteServer.command"), new MuteServer(server));
            List<String> Aliases = config.getStringList("Commands.MuteServer.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new MuteServer(server));
            }
        }
        if (config.getBoolean("Commands.MessageToggle.enabled")) {
            command.register(config.getString("Commands.MessageToggle.command"), new MessageToggle(server));
            List<String> Aliases = config.getStringList("Commands.MessageToggle.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new MessageToggle(server));
            }
        }
    }
}
