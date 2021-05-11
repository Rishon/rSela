package net.rishon.site.rsela.commands.proxy;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.config.Configuration;
import net.rishon.site.rsela.filemanager.ConfigHandler;
import net.rishon.site.rsela.utils.ColorUtil;
import net.rishon.site.rsela.utils.Globals;

public class Alert implements Command {

    private final ProxyServer server;

    public Alert(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(CommandSource source, String[] args) {

        if (!source.hasPermission(Globals.rSela_alert)) {
            source.sendMessage(ColorUtil.format(Globals.noPermission));
            return;
        }

        Configuration config = ConfigHandler.getConfig();

        if (args.length == 0) {
            source.sendMessage(ColorUtil.format(config.getString("Alert.usage")));
            return;
        }

        StringBuilder message = new StringBuilder();
        for (String string : args) {
            message.append(string).append(" ");
        }

        if (!(source instanceof Player)) {
            String chatMessage = ConfigHandler.getConfig().getString("Alert.message").replace("%message%", message.toString()).replace("%executor%", "CONSOLE");
            String titleMessage = ConfigHandler.getConfig().getString("Alert.title").replace("%message%", message.toString()).replace("%executor%", "CONSOLE");
            String subtitleMessage = ConfigHandler.getConfig().getString("Alert.subtitle").replace("%message%", message.toString()).replace("%executor%", "CONSOLE");
            String actionbarMessage = ConfigHandler.getConfig().getString("Alert.actionbar").replace("%message%", message.toString()).replace("%executor%", "CONSOLE");

            Title title = Title.title(ColorUtil.format(titleMessage), ColorUtil.format(subtitleMessage));

            for (Player server : server.getAllPlayers()) {

                if (config.getBoolean("Alert.title-message")) {
                    server.showTitle(title);
                }
                if (config.getBoolean("Alert.chat-message")) {
                    server.sendMessage(ColorUtil.format(chatMessage));
                }
                if (config.getBoolean("Alert.actionbar-message")) {
                    server.sendActionBar(ColorUtil.format(actionbarMessage));
                }
            }

        } else {

            Player player = (Player) source;

            String chatMessage = ConfigHandler.getConfig().getString("Alert.message").replace("%message%", message.toString()).replace("%executor%", player.getUsername());
            String titleMessage = ConfigHandler.getConfig().getString("Alert.title").replace("%message%", message.toString()).replace("%executor%", player.getUsername());
            String subtitleMessage = ConfigHandler.getConfig().getString("Alert.subtitle").replace("%message%", message.toString()).replace("%executor%", player.getUsername());
            String actionbarMessage = ConfigHandler.getConfig().getString("Alert.actionbar").replace("%message%", message.toString()).replace("%executor%", player.getUsername());

            Title title = Title.title(ColorUtil.format(titleMessage), ColorUtil.format(subtitleMessage));

            for (Player server : server.getAllPlayers()) {

                if (config.getBoolean("Alert.title-message")) {
                    server.showTitle(title);
                }
                if (config.getBoolean("Alert.chat-message")) {
                    server.sendMessage(ColorUtil.format(chatMessage));
                }
                if (config.getBoolean("Alert.actionbar-message")) {
                    server.sendActionBar(ColorUtil.format(actionbarMessage));
                }
            }
        }
    }
}
