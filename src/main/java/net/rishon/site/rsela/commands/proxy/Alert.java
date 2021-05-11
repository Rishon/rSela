package net.rishon.site.rsela.commands.proxy;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.config.Configuration;
import net.rishon.site.rsela.filemanager.ConfigHandler;
import net.rishon.site.rsela.utils.Globals;

public class Alert implements Command {

    private final ProxyServer server;

    public Alert(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(CommandSource source, String[] args) {

        if (!source.hasPermission(Globals.rSela_alert)) {
            source.sendMessage(Component.text(Globals.noPermission));
            return;
        }

        if (args.length == 0) {
            source.sendMessage(Component.text("§7Usage: /alert <message>"));
            return;
        }

        StringBuilder message = new StringBuilder();
        for (String string : args) {
            message.append(string).append(" ");
        }

        Configuration config = ConfigHandler.getConfig();

        if (!(source instanceof Player)) {
            String chatMessage = ConfigHandler.getConfig().getString("Alert.message").replace("%message%", message.toString()).replace("%executor%", "CONSOLE");
            String titleMessage = ConfigHandler.getConfig().getString("Alert.title").replace("%message%", message.toString()).replace("%executor%", "CONSOLE");
            String subtitleMessage = ConfigHandler.getConfig().getString("Alert.subtitle").replace("%message%", message.toString()).replace("%executor%", "CONSOLE");
            String actionbarMessage = ConfigHandler.getConfig().getString("Alert.actionbar").replace("%message%", message.toString()).replace("%executor%", "CONSOLE");

            Title title = Title.title(Component.text(titleMessage), Component.text(subtitleMessage));

            for (Player server : server.getAllPlayers()) {

                if (config.getBoolean("Alert.title-message")) {
                    server.showTitle(title);
                }

                if (config.getBoolean("Alert.chat-message")) {
                    server.sendMessage(Component.text(chatMessage));
                }
                if (config.getBoolean("Alert.actionbar-message")) {
                    server.sendActionBar(Component.text(actionbarMessage));
                }
            }
        }
    }
}
