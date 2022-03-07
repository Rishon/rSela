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
import java.util.logging.Level;
import java.util.logging.Logger;

@Plugin(id = "rsela", name = "rSela", version = "1.0.3", description = "Your all-in one plugin for a Velocity network server.", url = "rishon.codes", authors = {"Rishon"})
public class Main {

    private static Main instance;
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    // MiniMessage Library
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    // File Handlers
    public FileHandler fileHandler;
    public Configuration config, data;
    // Permissions
    private Permissions permissions;

    @Inject
    public Main(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    public static Main getInstance() {
        return instance;
    }

    @Subscribe
    private void onProxyInitialization(ProxyInitializeEvent event) {

        instance = this;

        // Load dataDirectory
        this.fileHandler = new FileHandler(this.dataDirectory);

        if (!this.fileHandler.loadConfig()) {
            this.logger.severe(permissions.rSela_prefix + "An error occurred while attempting to load config.yml");
            this.server.shutdown();
            return;
        } else if (!this.fileHandler.loadData()) {
            this.logger.severe(permissions.rSela_prefix + "An error occurred while attempting to load data.yml");
            this.server.shutdown();
            return;
        }

        this.config.set("version", "1.0.3");

        // Load permissions
        this.permissions = new Permissions();

        // Register plugin commands
        registerCommands();
        // Register plugin listeners
        registerListeners();

        Lists.toggled_messages = this.config.getStringList("toggled_messages");

        this.logger.log(Level.INFO, permissions.rSela_prefix + "Loaded rSela plugin.");

    }

    @Subscribe
    private void onProxyShutdown(ProxyShutdownEvent event) {

        // Saving yaml data
        this.data.set("toggled_messages", Lists.toggled_messages);
        this.fileHandler.saveData();

        instance = null;

        this.logger.log(Level.INFO, permissions.rSela_prefix + "Shutting down rSela...");
    }

    private void registerListeners() {
        EventManager eventManager = this.server.getEventManager();
        eventManager.register(this, new Connections());
        eventManager.register(this, new ProxyPing(this.miniMessage));
        this.logger.log(Level.INFO, "Successfully loaded rSela listeners.");
    }

    private void registerCommands() {

        CommandManager command = this.server.getCommandManager();

        this.logger.log(Level.INFO, "Loading rSela commands...");

        command.register("rsela", new rSela());

        if (this.config.getBoolean("Commands.Alert.enabled")) {
            command.register(this.config.getString("Commands.Alert.command"), new Alert(this.server));
            List<String> Aliases = this.config.getStringList("Commands.Alert.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new Alert(this.server));
            }
        }
        if (this.config.getBoolean("Commands.Find.enabled")) {
            command.register(this.config.getString("Commands.Find.command"), new Find(this.server));
            List<String> Aliases = this.config.getStringList("Commands.Find.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new Find(this.server));
            }
        }
        if (this.config.getBoolean("Commands.Send.enabled")) {
            command.register(this.config.getString("Commands.Send.command"), new Send(this.server));
            List<String> Aliases = this.config.getStringList("Commands.Send.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new Send(this.server));
            }
        }
        if (this.config.getBoolean("Commands.ServerSend.enabled")) {
            command.register(this.config.getString("Commands.ServerSend.command"), new ServerSend(this.server));
            List<String> Aliases = this.config.getStringList("Commands.ServerSend.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new ServerSend(this.server));
            }
        }
        if (this.config.getBoolean("Commands.Maintenance.enabled")) {
            command.register(this.config.getString("Commands.Maintenance.command"), new Maintenance());
            List<String> Aliases = this.config.getStringList("Commands.Maintenance.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new Maintenance());
            }
        }
        if (this.config.getBoolean("Commands.IP.enabled")) {
            command.register(this.config.getString("Commands.IP.command"), new IP(this.server));
            List<String> Aliases = this.config.getStringList("Commands.IP.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new IP(this.server));
            }
        }
        if (this.config.getBoolean("Commands.StaffChat.enabled")) {
            command.register(this.config.getString("Commands.StaffChat.command"), new StaffChat(this.server));
            List<String> Aliases = this.config.getStringList("Commands.StaffChat.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new StaffChat(this.server));
            }
        }
        if (this.config.getBoolean("Commands.Message.enabled")) {
            command.register(this.config.getString("Commands.Message.command"), new Message(this.server));
            List<String> Aliases = this.config.getStringList("Commands.Message.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new Message(this.server));
            }
        }
        if (this.config.getBoolean("Commands.ClearChat.enabled")) {
            command.register(this.config.getString("Commands.ClearChat.command"), new ClearChat(this.server));
            List<String> Aliases = this.config.getStringList("Commands.ClearChat.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new ClearChat(this.server));
            }
        }
        if (this.config.getBoolean("Commands.MuteServer.enabled")) {
            command.register(this.config.getString("Commands.MuteServer.command"), new MuteServer(this.server));
            List<String> Aliases = this.config.getStringList("Commands.MuteServer.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new MuteServer(this.server));
            }
        }
        if (this.config.getBoolean("Commands.MessageToggle.enabled")) {
            command.register(this.config.getString("Commands.MessageToggle.command"), new MessageToggle());
            List<String> Aliases = this.config.getStringList("Commands.MessageToggle.aliases");
            for (String cmd : Aliases) {
                command.register(cmd, new MessageToggle());
            }
        }
        this.logger.log(Level.INFO, "Successfully loaded rSela commands.");
    }
}
