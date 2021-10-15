package net.rishon.codes.rsela.commands.proxy;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.rsela.filemanager.FileHandler;
import net.rishon.codes.rsela.utils.ColorUtil;
import net.rishon.codes.rsela.utils.Permissions;

public class Alert implements SimpleCommand {

    private final ProxyServer server;

    public Alert(ProxyServer server) {
        this.server = server;
    }

    Configuration config = FileHandler.getConfig();

    @Override
    public void execute(final SimpleCommand.Invocation invocation) {

        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (config.getBoolean("Commands.Alert.require-permission")) {
            if (!source.hasPermission(Permissions.rSela_alert)) {
                source.sendMessage(ColorUtil.format(Permissions.noPermission));
                return;
            }
        }


        if (args.length == 0) {
            source.sendMessage(ColorUtil.format(config.getString("Commands.Alert.usage")));
            return;
        }

        StringBuilder message = new StringBuilder();
        for (String string : args) {
            message.append(string).append(" ");
        }

        if (!(source instanceof Player)) {
            String chatMessage = config.getString("Commands.Alert.message").replace("%message%", message.toString()).replace("%executor%", "CONSOLE");
            String titleMessage = config.getString("Commands.Alert.title").replace("%message%", message.toString()).replace("%executor%", "CONSOLE");
            String subtitleMessage = config.getString("Commands.Alert.subtitle").replace("%message%", message.toString()).replace("%executor%", "CONSOLE");
            String actionbarMessage = config.getString("Commands.Alert.actionbar").replace("%message%", message.toString()).replace("%executor%", "CONSOLE");

            Title title = Title.title(ColorUtil.format(titleMessage), ColorUtil.format(subtitleMessage));

            for (Player server : server.getAllPlayers()) {

                if (config.getBoolean("Commands.Alert.title-message")) {
                    server.showTitle(title);
                }
                if (config.getBoolean("Commands.Alert.chat-message")) {
                    server.sendMessage(ColorUtil.format(chatMessage));
                }
                if (config.getBoolean("Commands.Alert.actionbar-message")) {
                    server.sendActionBar(ColorUtil.format(actionbarMessage));
                }
            }

        } else {

            Player player = (Player) source;

            String chatMessage = config.getString("Commands.Alert.message").replace("%message%", message.toString()).replace("%executor%", player.getUsername());
            String titleMessage = config.getString("Commands.Alert.title").replace("%message%", message.toString()).replace("%executor%", player.getUsername());
            String subtitleMessage = config.getString("Commands.Alert.subtitle").replace("%message%", message.toString()).replace("%executor%", player.getUsername());
            String actionbarMessage = config.getString("Commands.Alert.actionbar").replace("%message%", message.toString()).replace("%executor%", player.getUsername());

            Title title = Title.title(ColorUtil.format(titleMessage), ColorUtil.format(subtitleMessage));

            for (Player server : server.getAllPlayers()) {

                if (config.getBoolean("Commands.Alert.title-message")) {
                    server.showTitle(title);
                }
                if (config.getBoolean("Commands.Alert.chat-message")) {
                    server.sendMessage(ColorUtil.format(chatMessage));
                }
                if (config.getBoolean("Commands.Alert.actionbar-message")) {
                    server.sendActionBar(ColorUtil.format(actionbarMessage));
                }
            }
        }
    }

    @Override
    public boolean hasPermission(SimpleCommand.Invocation invocation) {
        if (!config.getBoolean("Commands.Alert.require-permission")) return true;
        return invocation.source().hasPermission(Permissions.rSela_alert);
    }
}
