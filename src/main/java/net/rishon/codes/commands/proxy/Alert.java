package net.rishon.codes.commands.proxy;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.Main;
import net.rishon.codes.utils.ColorUtil;
import net.rishon.codes.utils.Permissions;

public class Alert implements SimpleCommand {

    private final ProxyServer server;
    private final Configuration config = Main.getInstance().config;
    private final Permissions permissions = new Permissions();

    public Alert(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(final SimpleCommand.Invocation invocation) {

        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (this.config.getBoolean("Commands.Alert.require-permission")) {
            if (!source.hasPermission(permissions.rSela_alert)) {
                source.sendMessage(ColorUtil.format(permissions.noPermission));
                return;
            }
        }


        if (args.length == 0) {
            source.sendMessage(ColorUtil.format(this.config.getString("Commands.Alert.usage")));
            return;
        }

        StringBuilder message = new StringBuilder();
        for (String string : args) {
            message.append(string).append(" ");
        }

        if (!(source instanceof Player)) {
            String chatMessage = this.config.getString("Commands.Alert.message").replace("%message%", message.toString()).replace("%executor%", "CONSOLE");
            String titleMessage = this.config.getString("Commands.Alert.title").replace("%message%", message.toString()).replace("%executor%", "CONSOLE");
            String subtitleMessage = this.config.getString("Commands.Alert.subtitle").replace("%message%", message.toString()).replace("%executor%", "CONSOLE");
            String actionbarMessage = this.config.getString("Commands.Alert.actionbar").replace("%message%", message.toString()).replace("%executor%", "CONSOLE");

            Title title = Title.title(ColorUtil.format(titleMessage), ColorUtil.format(subtitleMessage));

            for (Player server : server.getAllPlayers()) {

                if (this.config.getBoolean("Commands.Alert.title-message")) {
                    server.showTitle(title);
                }
                if (this.config.getBoolean("Commands.Alert.chat-message")) {
                    server.sendMessage(ColorUtil.format(chatMessage));
                }
                if (this.config.getBoolean("Commands.Alert.actionbar-message")) {
                    server.sendActionBar(ColorUtil.format(actionbarMessage));
                }
            }

        } else {

            Player player = (Player) source;

            String chatMessage = this.config.getString("Commands.Alert.message").replace("%message%", message.toString()).replace("%executor%", player.getUsername());
            String titleMessage = this.config.getString("Commands.Alert.title").replace("%message%", message.toString()).replace("%executor%", player.getUsername());
            String subtitleMessage = this.config.getString("Commands.Alert.subtitle").replace("%message%", message.toString()).replace("%executor%", player.getUsername());
            String actionbarMessage = this.config.getString("Commands.Alert.actionbar").replace("%message%", message.toString()).replace("%executor%", player.getUsername());

            Title title = Title.title(ColorUtil.format(titleMessage), ColorUtil.format(subtitleMessage));

            for (Player server : server.getAllPlayers()) {

                if (this.config.getBoolean("Commands.Alert.title-message")) {
                    server.showTitle(title);
                }
                if (this.config.getBoolean("Commands.Alert.chat-message")) {
                    server.sendMessage(ColorUtil.format(chatMessage));
                }
                if (this.config.getBoolean("Commands.Alert.actionbar-message")) {
                    server.sendActionBar(ColorUtil.format(actionbarMessage));
                }
            }
        }
    }

    @Override
    public boolean hasPermission(SimpleCommand.Invocation invocation) {
        if (!this.config.getBoolean("Commands.Alert.require-permission")) return true;
        return invocation.source().hasPermission(permissions.rSela_alert);
    }
}
