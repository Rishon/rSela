package net.rishon.site.rsela.commands.proxy;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.rishon.site.rsela.filemanager.ConfigHandler;
import net.rishon.site.rsela.utils.ColorUtil;
import net.rishon.site.rsela.utils.Permissions;

public class StaffChat implements Command {

    private final ProxyServer server;

    public StaffChat(ProxyServer server) {
        this.server = server;
    }

    Configuration config = ConfigHandler.getConfig();

    @Override
    public void execute(CommandSource source, String[] args) {

        if (config.getBoolean("Commands.StaffChat.require-permission")) {
            if (!source.hasPermission(Permissions.rSela_staffchat)) {
                source.sendMessage(ColorUtil.format(Permissions.noPermission));
                return;
            }
        }

        if (args.length == 0) {
            source.sendMessage(ColorUtil.format(config.getString("Commands.StaffChat.usage")));
            return;
        }

        StringBuilder message = new StringBuilder();
        for (String string : args) {
            message.append(string).append(" ");
        }

        if (!(source instanceof Player)) {

            String staffChatFormat = config.getString("Commands.StaffChat.staff-format").replace("%executor%", "CONSOLE").replace("%message%", message).replace("%server%", "NONE");

            for (Player staff : server.getAllPlayers()) {
                if (staff.hasPermission(Permissions.rSela_staffchat)) {
                    staff.sendMessage(ColorUtil.format(staffChatFormat));
                }
            }

        } else {

            Player player = (Player) source;

            String staffChatFormat = config.getString("Commands.StaffChat.staff-format").replace("%executor%", player.getUsername()).replace("%message%", message).replace("%server%", player.getCurrentServer().get().getServerInfo().getName());

            for (Player staff : server.getAllPlayers()) {
                if (staff.hasPermission(Permissions.rSela_staffchat)) {
                    staff.sendMessage(ColorUtil.format(staffChatFormat));
                }
            }
        }
    }
}
