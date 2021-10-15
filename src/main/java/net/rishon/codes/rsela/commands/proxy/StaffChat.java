package net.rishon.codes.rsela.commands.proxy;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.rsela.filemanager.FileHandler;
import net.rishon.codes.rsela.utils.ColorUtil;
import net.rishon.codes.rsela.utils.Permissions;

public class StaffChat implements SimpleCommand {

    private final ProxyServer server;

    public StaffChat(ProxyServer server) {
        this.server = server;
    }

    Configuration config = FileHandler.getConfig();

    @Override
    public void execute(final SimpleCommand.Invocation invocation) {

        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

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

    @Override
    public boolean hasPermission(SimpleCommand.Invocation invocation) {
        if (!config.getBoolean("Commands.StaffChat.require-permission")) return true;
        return invocation.source().hasPermission(Permissions.rSela_staffchat);
    }
}
