package net.rishon.systems.commands.proxy;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.md_5.bungee.config.Configuration;
import net.rishon.systems.Main;
import net.rishon.systems.utils.ColorUtil;
import net.rishon.systems.utils.Permissions;

public class MuteServer implements SimpleCommand {

    private final Main instance;
    private final Configuration config = Main.config;
    private final Permissions permissions;

    public MuteServer(Main instance) {
        this.instance = instance;
        this.permissions = instance.getPermissions();
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

        if (!this.instance.getHandler().getDataManager().mutedServers.contains(server_name)) {
            player.sendMessage(ColorUtil.format(muted_message));
            if (this.config.getBoolean("Commands.MuteServer.chat-notify")) {
                for (Player server : this.instance.getServer().getAllPlayers()) {
                    server.sendMessage(ColorUtil.format(muted_chat_notification));
                }
            }
            this.instance.getHandler().getDataManager().mutedServers.add(server_name);
        } else if (this.instance.getHandler().getDataManager().mutedServers.contains(player.getCurrentServer().get().getServerInfo().getName())) {
            player.sendMessage(ColorUtil.format(un_muted_message));
            if (this.config.getBoolean("Commands.MuteServer.chat-notify")) {
                for (Player server : this.instance.getServer().getAllPlayers()) {
                    server.sendMessage(ColorUtil.format(un_muted_chat_notification));
                }
            }
            this.instance.getHandler().getDataManager().mutedServers.remove(server_name);
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        if (!this.config.getBoolean("Commands.MuteServer.require-permission")) return true;
        return invocation.source().hasPermission(permissions.rSela_muteserver);
    }
}
