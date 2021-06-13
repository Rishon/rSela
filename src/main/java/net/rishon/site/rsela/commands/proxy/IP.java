package net.rishon.site.rsela.commands.proxy;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.rishon.site.rsela.filemanager.ConfigHandler;
import net.rishon.site.rsela.utils.ColorUtil;
import net.rishon.site.rsela.utils.Globals;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IP implements Command {

    private final ProxyServer server;

    public IP(ProxyServer server) {
        this.server = server;
    }

    Configuration config = ConfigHandler.getConfig();

    @Override
    public void execute(CommandSource source, String[] args) {

        if (config.getBoolean("Commands.IP.require-permission")) {
            if (!source.hasPermission(Globals.rSela_ip)) {
                source.sendMessage(ColorUtil.format(Globals.noPermission));
                return;
            }
        }

        if (args.length == 0) {
            source.sendMessage(ColorUtil.format("§7Usage: /ip <player>"));
            return;
        }

        String offlineMessage = config.getString("Commands.IP.offline-message").replace("%target%", args[0]);

        Optional<Player> player = server.getPlayer(args[0]);
        if (!player.isPresent()) {
            source.sendMessage(ColorUtil.format(offlineMessage));
            return;
        }

        String foundMessage = config.getString("Commands.IP.online-message").replace("%target%", args[0]).replace("%ip%", player.get().getRemoteAddress().getAddress().toString());

        source.sendMessage(ColorUtil.format(foundMessage));
    }

    @Override
    public List<String> suggest(CommandSource source, @NonNull String[] currentArgs) {
        List<String> arg = new ArrayList<>();
        if (currentArgs.length == 1 && source.hasPermission(Globals.rSela_ip)) {
            for (Player player : server.getAllPlayers()) {
                arg.add(player.getUsername());
            }
        }
        return arg;
    }
}
