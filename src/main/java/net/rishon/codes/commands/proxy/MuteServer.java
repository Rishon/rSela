package net.rishon.codes.commands.proxy;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.Main;
import net.rishon.codes.utils.ColorUtil;
import net.rishon.codes.utils.Lists;
import net.rishon.codes.utils.Permissions;

public class MuteServer implements SimpleCommand {

    private final ProxyServer server;
    private final Configuration config = Main.getInstance().config;
    private final Permissions permissions = new Permissions();
    public MuteServer(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(final Invocation invocation) {

        CommandSource source = invocation.source();

        if (!(source instanceof Player)) {
            source.sendMessage(ColorUtil.format("&cOnly players may execute this command."));
            return;
        }

        if (this.config.getBoolean("Commands.MuteServer.require-permission")) {
            if (!source.hasPermission(permissions.rSela_muteserver)) {
                source.sendMessage(ColorUtil.format(permissions.noPermission));
                return;
            }
        }

        Player player = (Player) source;

        String server_name = player.getCurrentServer().get().getServerInfo().getName();

        String muted_chat_notification = this.config.getString("Commands.MuteServer.chat-muted-msg").replace("%executor%", player.getUsername()).replace("%server%", server_name);
        String muted_message = this.config.getString("Commands.MuteServer.muted-message").replace("%executor%", player.getUsername()).replace("%server%", server_name);

        String un_muted_chat_notification = this.config.getString("Commands.MuteServer.chat-un-muted-msg").replace("%executor%", player.getUsername()).replace("%server%", server_name);
        String un_muted_message = this.config.getString("Commands.MuteServer.un-muted-message").replace("%executor%", player.getUsername()).replace("%server%", server_name);

        if (!Lists.mutedServers.contains(server_name)) {
            player.sendMessage(ColorUtil.format(muted_message));
            if (this.config.getBoolean("Commands.MuteServer.chat-notify")) {
                for (Player server : server.getAllPlayers()) {
                    server.sendMessage(ColorUtil.format(muted_chat_notification));
                }
            }
            Lists.mutedServers.add(server_name);
        } else if (Lists.mutedServers.contains(player.getCurrentServer().get().getServerInfo().getName())) {
            player.sendMessage(ColorUtil.format(un_muted_message));
            if (this.config.getBoolean("Commands.MuteServer.chat-notify")) {
                for (Player server : server.getAllPlayers()) {
                    server.sendMessage(ColorUtil.format(un_muted_chat_notification));
                }
            }
            Lists.mutedServers.remove(server_name);
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        if (!this.config.getBoolean("Commands.MuteServer.require-permission")) return true;
        return invocation.source().hasPermission(permissions.rSela_muteserver);
    }
}
