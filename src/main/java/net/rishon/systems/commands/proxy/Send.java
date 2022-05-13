package net.rishon.systems.commands.proxy;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import net.md_5.bungee.config.Configuration;
import net.rishon.systems.Main;
import net.rishon.systems.utils.ColorUtil;
import net.rishon.systems.utils.Permissions;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Send implements SimpleCommand {

    private final Main instance;
    private final Configuration config = Main.config;
    private final Permissions permissions;

    public Send(Main instance) {
        this.instance = instance;
        this.permissions = instance.getPermissions();
    }

    @Override
    public void execute(final SimpleCommand.Invocation invocation) {

        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (this.config.getBoolean("Commands.Send.require-permission")) {
            if (!source.hasPermission(permissions.rSela_send)) {
                source.sendMessage(ColorUtil.format(permissions.noPermission));
                return;
            }
        }

        if (args.length < 2) {
            source.sendMessage(ColorUtil.format(this.config.getString("Commands.Send.usage")));
            return;
        }

        String invalidServer = this.config.getString("Commands.Send.invalid-server").replace("%server%", args[1]);
        String sentAll = this.config.getString("Commands.Send.sent-all").replace("%server%", args[1]);

        if (args[0].equalsIgnoreCase("all")) {

            source.sendMessage(ColorUtil.format(sentAll));
            for (Player network : this.instance.getServer().getAllPlayers()) {
                if (network != source) {
                    Optional<RegisteredServer> server = this.instance.getServer().getServer(args[1]);

                    if (server.isEmpty()) {
                        source.sendMessage(ColorUtil.format(invalidServer));
                        return;
                    }

                    ServerInfo si = server.get().getServerInfo();
                    if (si == null) {
                        source.sendMessage(ColorUtil.format(invalidServer));
                        return;
                    }

                    Optional<RegisteredServer> targetedServer = this.instance.getServer().getServer(si.getName());
                    if (targetedServer.isEmpty()) {
                        source.sendMessage(ColorUtil.format(invalidServer));
                        return;
                    }

                    network.createConnectionRequest(targetedServer.get()).fireAndForget();
                }
            }
            return;
        }

        Optional<Player> player = this.instance.getServer().getPlayer(args[0]);

        String offlineMessage = this.config.getString("Commands.Send.player-offline").replace("%target%", args[0]);

        if (player.isEmpty()) {
            source.sendMessage(ColorUtil.format(offlineMessage));
            return;
        }

        String alreadyIn = this.config.getString("Commands.Send.already-in-server").replace("%target%", args[0]).replace("%server%", args[1]);
        String sentPlayer = this.config.getString("Commands.Send.sent-player").replace("%target%", args[0]).replace("%server%", args[1]);

        Optional<RegisteredServer> Connection = this.instance.getServer().getServer(args[1]);
        if (Connection.isEmpty()) {
            source.sendMessage(ColorUtil.format(invalidServer));
            return;
        }

        Optional<RegisteredServer> server = this.instance.getServer().getServer(args[1]);

        if (server.isEmpty()) {
            source.sendMessage(ColorUtil.format(invalidServer));
            return;
        }

        ServerInfo si = server.get().getServerInfo();
        if (si == null) {
            source.sendMessage(ColorUtil.format(invalidServer));
            return;
        } else if (player.get().getCurrentServer().isEmpty()) {
            source.sendMessage(ColorUtil.format(invalidServer));
            return;
        }

        if (player.get().getCurrentServer().get().getServerInfo().getName().equals(Connection.get().getServerInfo().getName())) {
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

        Stream<String> playerFind = this.instance.getServer().getAllPlayers().stream()
                .map(rs -> rs.getGameProfile().getName());

        Stream<String> serverFind = this.instance.getServer().getAllServers().stream()
                .map(rs -> rs.getServerInfo().getName());

        if (currentArgs.length == 0 && source.hasPermission(permissions.rSela_send)) {
            return playerFind.collect(Collectors.toList());
        } else if (currentArgs.length == 1 && source.hasPermission(permissions.rSela_send)) {
            return playerFind
                    .filter(name -> name.regionMatches(true, 0, currentArgs[0], 0, currentArgs[0].length()))
                    .collect(Collectors.toList());
        } else if (currentArgs.length == 2 && source.hasPermission(permissions.rSela_send)) {
            return serverFind
                    .filter(name -> name.regionMatches(true, 0, currentArgs[1], 0, currentArgs[1].length()))
                    .collect(Collectors.toList());
        } else {
            return ImmutableList.of();
        }
    }

    @Override
    public boolean hasPermission(SimpleCommand.Invocation invocation) {
        if (!this.config.getBoolean("Commands.Send.require-permission")) return true;
        return invocation.source().hasPermission(permissions.rSela_send);
    }

}
