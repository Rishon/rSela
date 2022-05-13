package net.rishon.systems.commands.proxy;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.md_5.bungee.config.Configuration;
import net.rishon.systems.Main;
import net.rishon.systems.utils.ColorUtil;
import net.rishon.systems.utils.Permissions;

public class StaffChat implements SimpleCommand {

    private final Main instance;
    private final Configuration config = Main.config;
    private final Permissions permissions;

    public StaffChat(Main instance) {
        this.instance = instance;
        this.permissions = instance.getPermissions();
    }

    @Override
    public void execute(final SimpleCommand.Invocation invocation) {

        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (this.config.getBoolean("Commands.StaffChat.require-permission")) {
            if (!source.hasPermission(permissions.rSela_staffchat)) {
                source.sendMessage(ColorUtil.format(permissions.noPermission));
                return;
            }
        }

        if (args.length == 0) {
            source.sendMessage(ColorUtil.format(this.config.getString("Commands.StaffChat.usage")));
            return;
        }

        StringBuilder message = new StringBuilder();
        for (String string : args) {
            message.append(string).append(" ");
        }

        if (!(source instanceof Player)) {

            String staffChatFormat = this.config.getString("Commands.StaffChat.staff-format").replace("%executor%", "CONSOLE").replace("%message%", message).replace("%server%", "NONE");

            for (Player staff : this.instance.getServer().getAllPlayers()) {
                if (staff.hasPermission(permissions.rSela_staffchat)) {
                    staff.sendMessage(ColorUtil.format(staffChatFormat));
                }
            }

        } else {

            Player player = (Player) source;
            if (player.getCurrentServer().isEmpty()) {
                player.sendMessage(ColorUtil.format("&cAn error occurred while attempting to send your message."));
                return;
            }

            String staffChatFormat = this.config.getString("Commands.StaffChat.staff-format").replace("%executor%", player.getUsername()).replace("%message%", message).replace("%server%", player.getCurrentServer().get().getServerInfo().getName());

            for (Player staff : this.instance.getServer().getAllPlayers()) {
                if (staff.hasPermission(permissions.rSela_staffchat)) {
                    staff.sendMessage(ColorUtil.format(staffChatFormat));
                }
            }
        }
    }

    @Override
    public boolean hasPermission(SimpleCommand.Invocation invocation) {
        if (!this.config.getBoolean("Commands.StaffChat.require-permission")) return true;
        return invocation.source().hasPermission(permissions.rSela_staffchat);
    }
}
