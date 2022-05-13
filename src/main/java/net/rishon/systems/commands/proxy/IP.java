package net.rishon.systems.commands.proxy;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.md_5.bungee.config.Configuration;
import net.rishon.systems.Main;
import net.rishon.systems.utils.ColorUtil;
import net.rishon.systems.utils.Permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IP implements SimpleCommand {

    private final Main instance;
    private final Configuration config = Main.config;
    private final Permissions permissions;

    public IP(Main instance) {
        this.instance = instance;
        this.permissions = instance.getPermissions();
    }

    @Override
    public void execute(final Invocation invocation) {

        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (this.config.getBoolean("Commands.IP.require-permission")) {
            if (!source.hasPermission(permissions.rSela_ip)) {
                source.sendMessage(ColorUtil.format(permissions.noPermission));
                return;
            }
        }

        if (args.length == 0) {
            source.sendMessage(ColorUtil.format("§7Usage: /ip <player>"));
            return;
        }

        String offlineMessage = this.config.getString("Commands.IP.offline-message").replace("%target%", args[0]);

        Optional<Player> player = this.instance.getServer().getPlayer(args[0]);
        if (player.isEmpty()) {
            source.sendMessage(ColorUtil.format(offlineMessage));
            return;
        }

        String foundMessage = this.config.getString("Commands.IP.online-message").replace("%target%", args[0]).replace("%ip%", player.get().getRemoteAddress().getAddress().toString());

        source.sendMessage(ColorUtil.format(foundMessage));
    }

    @Override
    public List<String> suggest(final SimpleCommand.Invocation invocation) {

        CommandSource source = invocation.source();
        String[] currentArgs = invocation.arguments();
        List<String> arg = new ArrayList<>();

        if (currentArgs.length == 1 && source.hasPermission(permissions.rSela_ip)) {
            for (Player player : this.instance.getServer().getAllPlayers()) {
                arg.add(player.getUsername());
            }
        }
        return arg;
    }
}
