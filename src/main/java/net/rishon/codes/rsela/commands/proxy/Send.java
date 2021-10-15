package net.rishon.codes.rsela.commands.proxy;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.rsela.filemanager.FileHandler;
import net.rishon.codes.rsela.utils.ColorUtil;
import net.rishon.codes.rsela.utils.Permissions;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Send implements SimpleCommand {

    private final ProxyServer server;

    public Send(ProxyServer server) {
        this.server = server;
    }

    Configuration config = FileHandler.getConfig();

    @Override
    public void execute(final SimpleCommand.Invocation invocation) {

        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (config.getBoolean("Commands.Send.require-permission")) {
            if (!source.hasPermission(Permissions.rSela_send)) {
                source.sendMessage(ColorUtil.format(Permissions.noPermission));
                return;
            }
        }

        if (args.length < 2) {
            source.sendMessage(ColorUtil.format(config.getString("Commands.Send.usage")));
            return;
        }

        String invalidServer = config.getString("Commands.Send.invalid-server").replace("%server%", args[1]);
        String sentAll = config.getString("Commands.Send.sent-all").replace("%server%", args[1]);

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

        String offlineMessage = config.getString("Commands.Send.player-offline").replace("%target%", args[0]);

        if (!player.isPresent()) {
            source.sendMessage(ColorUtil.format(offlineMessage));
            return;
        }

        String alreadyIn = config.getString("Commands.Send.already-in-server").replace("%target%", args[0]).replace("%server%", args[1]);
        String sentPlayer = config.getString("Commands.Send.sent-player").replace("%target%", args[0]).replace("%server%", args[1]);

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
    public List<String> suggest(final SimpleCommand.Invocation invocation) {

        CommandSource source = invocation.source();
        String[] currentArgs = invocation.arguments();

        Stream<String> playerFind = server.getAllPlayers().stream()
                .map(rs -> rs.getGameProfile().getName());

        Stream<String> serverFind = server.getAllServers().stream()
                .map(rs -> rs.getServerInfo().getName());

        if (currentArgs.length == 0 && source.hasPermission(Permissions.rSela_send)) {
            return playerFind.collect(Collectors.toList());
        } else if (currentArgs.length == 1 && source.hasPermission(Permissions.rSela_send)) {
            return playerFind
                    .filter(name -> name.regionMatches(true, 0, currentArgs[0], 0, currentArgs[0].length()))
                    .collect(Collectors.toList());
        } else if (currentArgs.length == 2 && source.hasPermission(Permissions.rSela_send)) {
            return serverFind
                    .filter(name -> name.regionMatches(true, 0, currentArgs[1], 0, currentArgs[1].length()))
                    .collect(Collectors.toList());
        } else {
            return ImmutableList.of();
        }
    }

    @Override
    public boolean hasPermission(SimpleCommand.Invocation invocation) {
        if (!config.getBoolean("Commands.Send.require-permission")) return true;
        return invocation.source().hasPermission(Permissions.rSela_send);
    }

}
