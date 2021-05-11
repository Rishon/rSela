package net.rishon.site.rsela.commands.proxy;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import net.md_5.bungee.config.Configuration;
import net.rishon.site.rsela.filemanager.ConfigHandler;
import net.rishon.site.rsela.utils.ColorUtil;
import net.rishon.site.rsela.utils.Globals;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Send implements Command {

    private final ProxyServer server;

    public Send(ProxyServer server) {
        this.server = server;
    }

    Configuration config = ConfigHandler.getConfig();

    @Override
    public void execute(CommandSource source, String[] args) {

        if (!source.hasPermission(Globals.rSela_send)) {
            source.sendMessage(ColorUtil.format(Globals.noPermission));
            return;
        }

        if (args.length < 2) {
            source.sendMessage(ColorUtil.format(config.getString("Send.usage")));
            return;
        }

        String invalidServer = ConfigHandler.getConfig().getString("Send.invalid-server").replace("%server%", args[1]);
        String sentAll = ConfigHandler.getConfig().getString("Send.sent-all").replace("%server%", args[1]);

        if (args[0].equalsIgnoreCase("all")) {

            source.sendMessage(ColorUtil.format(sentAll));
            for (Player network : server.getAllPlayers()) {
                if (network != source) {

                    ServerInfo si = server.getServer(args[1]).get().getServerInfo();
                    if (si == null) {
                        source.sendMessage(ColorUtil.format(invalidServer));
                        return;
                    }

                    Optional<RegisteredServer> targetedServer = server.getServer(si.getName());
                    if (!targetedServer.isPresent()) {
                        source.sendMessage(ColorUtil.format(invalidServer));
                        return;
                    }

                    network.createConnectionRequest(targetedServer.get()).fireAndForget();
                }
            }
            return;
        }

        Optional<Player> player = server.getPlayer(args[0]);

        String offlineMessage = ConfigHandler.getConfig().getString("Send.player-offline").replace("%target%", args[0]);

        if (!player.isPresent()) {
            source.sendMessage(ColorUtil.format(offlineMessage));
            return;
        }

        String alreadyIn = ConfigHandler.getConfig().getString("Send.already-in-server").replace("%target%", args[0]).replace("%server%", args[1]);
        String sentPlayer = ConfigHandler.getConfig().getString("Send.sent-player").replace("%target%", args[0]).replace("%server%", args[1]);

        Optional<RegisteredServer> Connection = server.getServer(args[1]);
        if (!Connection.isPresent()) {
            source.sendMessage(ColorUtil.format(invalidServer));
            return;
        }

        ServerInfo si = server.getServer(args[1]).get().getServerInfo();
        if (si == null) {
            source.sendMessage(ColorUtil.format(invalidServer));
            return;
        }

        if (player.get().getCurrentServer().get().getServerInfo().equals(Connection.get().getServerInfo().getName())) {
            source.sendMessage(ColorUtil.format(alreadyIn));
            return;
        }

        source.sendMessage(ColorUtil.format(sentPlayer));

        player.get().createConnectionRequest(Connection.get()).fireAndForget();
    }

    @Override
    public List<String> suggest(CommandSource source, String[] currentArgs) {

        Player player = (Player) source;

        Stream<String> playerFind = server.getAllPlayers().stream()
                .map(rs -> rs.getGameProfile().getName());

        Stream<String> serverFind = server.getAllServers().stream()
                .map(rs -> rs.getServerInfo().getName());

        if (currentArgs.length == 0 && player.hasPermission(Globals.rSela_send)) {
            return playerFind.collect(Collectors.toList());
        } else if (currentArgs.length == 1 && player.hasPermission(Globals.rSela_send)) {
            return playerFind
                    .filter(name -> name.regionMatches(true, 0, currentArgs[0], 0, currentArgs[0].length()))
                    .collect(Collectors.toList());
        } else if (currentArgs.length == 2 && player.hasPermission(Globals.rSela_send)) {
            return serverFind
                    .filter(name -> name.regionMatches(true, 0, currentArgs[1], 0, currentArgs[1].length()))
                    .collect(Collectors.toList());
        } else {
            return ImmutableList.of();
        }
    }
}