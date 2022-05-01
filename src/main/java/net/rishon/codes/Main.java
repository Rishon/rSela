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
import net.rishon.codes.datamanager.dataManager;
import net.rishon.codes.filemanager.FileHandler;
import net.rishon.codes.listeners.Connections;
import net.rishon.codes.listeners.ProxyPing;
import net.rishon.codes.utils.Permissions;

import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Plugin(id = "rsela", name = "rSela", version = "1.0.4", description = "Your all-in one plugin for a Velocity network server.", url = "rishon.codes", authors = {"Rishon"})
public class Main {

    public Main instance;
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
    // DataManager
    private dataManager dataManager;

    @Inject
    public Main(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    private void onProxyInitialization(ProxyInitializeEvent event) {

        this.instance = this;

        // Load dataDirectory
        this.fileHandler = new FileHandler(this, this.dataDirectory);

        if (!this.fileHandler.loadConfig()) {
            this.getLogger().severe(permissions.rSela_prefix + "An error occurred while attempting to load config.yml");
            this.getServer().shutdown();
            return;
        }
        if (!this.fileHandler.loadData()) {
            this.getLogger().severe(permissions.rSela_prefix + "An error occurred while attempting to load data.yml");
            this.getServer().shutdown();
            return;
        }

        // TO:DO Connect to rishon.codes web-server to receive live-verison of the plugin.
        this.config.set("version", "1.0.3");

        // Register plugin commands
        registerCommands();
        // Register plugin listeners
        registerListeners();

        // Load Permissions
        this.permissions = new Permissions(this, getConfig());

        // Load dataManager
        this.dataManager = new dataManager();
        this.dataManager.toggled_messages = this.config.getStringList("toggled_messages");

        this.logger.log(Level.INFO, permissions.rSela_prefix + "Loaded rSela plugin.");

    }

    @Subscribe
    private void onProxyShutdown(ProxyShutdownEvent event) {

        // Saving yaml data
        this.data.set("toggled_messages", this.getDataManager().toggled_messages);
        this.fileHandler.saveData();

        this.instance = null;

        this.logger.log(Level.INFO, permissions.rSela_prefix + "Shutting down rSela...");
    }

    private void registerListeners() {
        EventManager eventManager = this.getServer().getEventManager();
        try {
            this.logger.log(Level.INFO, "Loading rSela listeners...");
            eventManager.register(this, new Connections(this));
            eventManager.register(this, new ProxyPing(this, this.miniMessage));
        } catch (Exception e) {
            e.printStackTrace();
            this.getServer().shutdown();
        } finally {
            this.logger.log(Level.INFO, "Successfully loaded rSela listeners.");
        }
    }

    private void registerCommands() {
        CommandManager command = this.getServer().getCommandManager();
        try {
            this.logger.log(Level.INFO, "Loading rSela commands...");
            command.register("rsela", new rSela(this));
            if (this.config.getBoolean("Commands.Alert.enabled")) {
                command.register(this.config.getString("Commands.Alert.command"), new Alert(this));
                List<String> Aliases = this.config.getStringList("Commands.Alert.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new Alert(this));
                }
            }
            if (this.config.getBoolean("Commands.Find.enabled")) {
                command.register(this.config.getString("Commands.Find.command"), new Find(this));
                List<String> Aliases = this.config.getStringList("Commands.Find.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new Find(this));
                }
            }
            if (this.config.getBoolean("Commands.Send.enabled")) {
                command.register(this.config.getString("Commands.Send.command"), new Send(this));
                List<String> Aliases = this.config.getStringList("Commands.Send.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new Send(this));
                }
            }
            if (this.config.getBoolean("Commands.ServerSend.enabled")) {
                command.register(this.config.getString("Commands.ServerSend.command"), new ServerSend(this));
                List<String> Aliases = this.config.getStringList("Commands.ServerSend.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new ServerSend(this));
                }
            }
            if (this.config.getBoolean("Commands.Maintenance.enabled")) {
                command.register(this.config.getString("Commands.Maintenance.command"), new Maintenance(this));
                List<String> Aliases = this.config.getStringList("Commands.Maintenance.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new Maintenance(this));
                }
            }
            if (this.config.getBoolean("Commands.IP.enabled")) {
                command.register(this.config.getString("Commands.IP.command"), new IP(this));
                List<String> Aliases = this.config.getStringList("Commands.IP.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new IP(this));
                }
            }
            if (this.config.getBoolean("Commands.StaffChat.enabled")) {
                command.register(this.config.getString("Commands.StaffChat.command"), new StaffChat(this));
                List<String> Aliases = this.config.getStringList("Commands.StaffChat.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new StaffChat(this));
                }
            }
            if (this.config.getBoolean("Commands.Message.enabled")) {
                command.register(this.config.getString("Commands.Message.command"), new Message(this));
                List<String> Aliases = this.config.getStringList("Commands.Message.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new Message(this));
                }
            }
            if (this.config.getBoolean("Commands.ClearChat.enabled")) {
                command.register(this.config.getString("Commands.ClearChat.command"), new ClearChat(this));
                List<String> Aliases = this.config.getStringList("Commands.ClearChat.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new ClearChat(this));
                }
            }
            if (this.config.getBoolean("Commands.MuteServer.enabled")) {
                command.register(this.config.getString("Commands.MuteServer.command"), new MuteServer(this));
                List<String> Aliases = this.config.getStringList("Commands.MuteServer.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new MuteServer(this));
                }
            }
            if (this.config.getBoolean("Commands.MessageToggle.enabled")) {
                command.register(this.config.getString("Commands.MessageToggle.command"), new MessageToggle(this));
                List<String> Aliases = this.config.getStringList("Commands.MessageToggle.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new MessageToggle(this));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.getServer().shutdown();
        } finally {
            this.getLogger().log(Level.INFO, "Successfully loaded rSela commands.");
        }
    }

    public ProxyServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }

    public Configuration getConfig() {
        return config;
    }

    public Configuration getData() {
        return data;
    }

    public dataManager getDataManager() {
        return dataManager;
    }

    public Permissions getPermissions() {
        return permissions;
    }
}
