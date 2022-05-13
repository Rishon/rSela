package net.rishon.systems.handler;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.rishon.systems.Main;
import net.rishon.systems.commands.messages.Message;
import net.rishon.systems.commands.messages.MessageToggle;
import net.rishon.systems.commands.proxy.*;
import net.rishon.systems.datamanager.dataManager;
import net.rishon.systems.listeners.Chat;
import net.rishon.systems.listeners.Connections;
import net.rishon.systems.listeners.ProxyPing;
import net.rishon.systems.utils.Permissions;

import java.util.List;
import java.util.logging.Level;

@Getter
public class MainHandler {

    // Main plugin instance
    private final Main instance;

    // Configuration
    private Configuration config, data;

    public MainHandler(Main instance) {
        this.instance = instance;
    }

    public void init() {

        this.config = this.getInstance().getConfig();
        this.data = this.getInstance().getData();

        // Register plugin commands
        registerCommands();
        // Register plugin listeners
        registerListeners();

        // Load Permissions
        this.getInstance().setPermissions(new Permissions(this));
        // Load dataManager
        this.getInstance().setDataManager(new dataManager());
        this.getInstance().getDataManager().setToggled_messages(this.getConfig().getStringList("toggled_messages"));
    }

    public void stop() {
        // Saving yaml data
        this.getData().set("toggled_messages", this.getInstance().getDataManager().toggled_messages);
        this.getInstance().getFileHandler().saveData();
    }

    void registerListeners() {
        EventManager eventManager = this.getInstance().getServer().getEventManager();
        try {
            this.getInstance().getLogger().log(Level.INFO, "Loading rSela listeners...");
            eventManager.register(this, new Connections(this.getInstance()));
            eventManager.register(this, new Chat(this.getInstance()));
            eventManager.register(this, new ProxyPing(this.getInstance(), this.getInstance().getMiniMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            this.getInstance().getServer().shutdown();
        } finally {
            this.getInstance().getLogger().log(Level.INFO, "Successfully loaded rSela listeners.");
        }
    }

    void registerCommands() {
        CommandManager command = this.getInstance().getServer().getCommandManager();
        try {
            this.getInstance().getLogger().log(Level.INFO, "Loading rSela commands...");
            command.register("rsela", new rSela(this.getInstance()));
            if (this.config.getBoolean("Commands.Alert.enabled")) {
                command.register(this.config.getString("Commands.Alert.command"), new Alert(this.getInstance()));
                List<String> Aliases = this.config.getStringList("Commands.Alert.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new Alert(this.getInstance()));
                }
            }
            if (this.config.getBoolean("Commands.Find.enabled")) {
                command.register(this.config.getString("Commands.Find.command"), new Find(this.getInstance()));
                List<String> Aliases = this.config.getStringList("Commands.Find.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new Find(this.getInstance()));
                }
            }
            if (this.config.getBoolean("Commands.Send.enabled")) {
                command.register(this.config.getString("Commands.Send.command"), new Send(this.getInstance()));
                List<String> Aliases = this.config.getStringList("Commands.Send.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new Send(this.getInstance()));
                }
            }
            if (this.config.getBoolean("Commands.ServerSend.enabled")) {
                command.register(this.config.getString("Commands.ServerSend.command"), new ServerSend(this.getInstance()));
                List<String> Aliases = this.config.getStringList("Commands.ServerSend.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new ServerSend(this.getInstance()));
                }
            }
            if (this.config.getBoolean("Commands.Maintenance.enabled")) {
                command.register(this.config.getString("Commands.Maintenance.command"), new Maintenance(this.getInstance()));
                List<String> Aliases = this.config.getStringList("Commands.Maintenance.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new Maintenance(this.getInstance()));
                }
            }
            if (this.config.getBoolean("Commands.IP.enabled")) {
                command.register(this.config.getString("Commands.IP.command"), new IP(this.getInstance()));
                List<String> Aliases = this.config.getStringList("Commands.IP.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new IP(this.getInstance()));
                }
            }
            if (this.config.getBoolean("Commands.StaffChat.enabled")) {
                command.register(this.config.getString("Commands.StaffChat.command"), new StaffChat(this.getInstance()));
                List<String> Aliases = this.config.getStringList("Commands.StaffChat.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new StaffChat(this.getInstance()));
                }
            }
            if (this.config.getBoolean("Commands.Message.enabled")) {
                command.register(this.config.getString("Commands.Message.command"), new Message(this.getInstance()));
                List<String> Aliases = this.config.getStringList("Commands.Message.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new Message(this.getInstance()));
                }
            }
            if (this.config.getBoolean("Commands.ClearChat.enabled")) {
                command.register(this.config.getString("Commands.ClearChat.command"), new ClearChat(this.getInstance()));
                List<String> Aliases = this.config.getStringList("Commands.ClearChat.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new ClearChat(this.getInstance()));
                }
            }
            if (this.config.getBoolean("Commands.MuteServer.enabled")) {
                command.register(this.config.getString("Commands.MuteServer.command"), new MuteServer(this.getInstance()));
                List<String> Aliases = this.config.getStringList("Commands.MuteServer.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new MuteServer(this.getInstance()));
                }
            }
            if (this.config.getBoolean("Commands.MessageToggle.enabled")) {
                command.register(this.config.getString("Commands.MessageToggle.command"), new MessageToggle(this.getInstance()));
                List<String> Aliases = this.config.getStringList("Commands.MessageToggle.aliases");
                for (String cmd : Aliases) {
                    command.register(cmd, new MessageToggle(this.getInstance()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.getInstance().getServer().shutdown();
        } finally {
            this.getInstance().getLogger().log(Level.INFO, "Successfully loaded rSela commands.");
        }
    }

}
