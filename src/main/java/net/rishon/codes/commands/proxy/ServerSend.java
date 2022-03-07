package net.rishon.codes.commands.proxy;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.Main;
import net.rishon.codes.utils.ColorUtil;
import net.rishon.codes.utils.Permissions;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerSend implements SimpleCommand {

    private final ProxyServer server;
    private final Configuration config = Main.getInstance().config;
    private final Permissions permissions = new Permissions();

    public ServerSend(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(final SimpleCommand.Invocation invocation) {

        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (this.config.getBoolean("Commands.ServerSend.require-permission")) {
            if (!source.hasPermission(permissions.rSela_serversend)) {
                source.sendMessage(ColorUtil.format(permissions.noPermission));
                return;
            }
        }

        if (args.length < 2) {
            source.sendMessage(ColorUtil.format(this.config.getString("Commands.ServerSend.usage")));
            return;
        }

        String invalidFirst = this.config.getString("Commands.ServerSend.invalid-server").replace("%server%", args[0]);

        Optional<RegisteredServer> serverFrom = server.getServer(args[0]);
        if (!serverFrom.isPresent()) {
            source.sendMessage(ColorUtil.format(invalidFirst));
            return;
        }

        String invalidSecondary = this.config.getString("Commands.ServerSend.invalid-server").replace("%server%", args[1]);

        Optional<RegisteredServer> serverTo = server.getServer(args[1]);
        if (!serverTo.isPresent()) {
            source.sendMessage(ColorUtil.format(invalidSecondary));
            return;
        }

        if (serverFrom == serverTo) {
            source.sendMessage(ColorUtil.format(this.config.getString("Commands.ServerSend.same-target")));
            return;
        }

        String sentPlayers = this.config.getString("Commands.ServerSend.sent-players").replace("%firstServer%", args[0]).replace("%secondaryServer%", args[1]);

        for (Player target : serverFrom.get().getPlayersConnected()) {
            Optional<RegisteredServer> connection = server.getServer(args[1]);
            if (!connection.isPresent()) {
                source.sendMessage(ColorUtil.format(invalidSecondary));
                return;
            }
            target.createConnectionRequest(connection.get()).fireAndForget();
            source.sendMessage(ColorUtil.format(sentPlayers));
        }
    }

    @Override
    public List<String> suggest(final SimpleCommand.Invocation invocation) {

        CommandSource source = invocation.source();
        String[] currentArgs = invocation.arguments();

        Stream<String> serverPossibilities = server.getAllServers().stream()
                .map(rs -> rs.getServerInfo().getName());

        if (currentArgs.length == 0 && source.hasPermission(permissions.rSela_serversend)) {
            return serverPossibilities.collect(Collectors.toList());
        } else if (currentArgs.length == 1 && source.hasPermission(permissions.rSela_serversend)) {
            return serverPossibilities
                    .filter(name -> name.regionMatches(true, 0, currentArgs[0], 0, currentArgs[0].length()))
                    .collect(Collectors.toList());
        } else if (currentArgs.length == 2 && source.hasPermission(permissions.rSela_serversend)) {
            return serverPossibilities
                    .filter(name -> name.regionMatches(true, 0, currentArgs[1], 0, currentArgs[1].length()))
                    .collect(Collectors.toList());
        } else {
            return ImmutableList.of();
        }
    }

    @Override
    public boolean hasPermission(SimpleCommand.Invocation invocation) {
        if (!this.config.getBoolean("Commands.ServerSend.require-permission")) return true;
        return invocation.source().hasPermission(permissions.rSela_serversend);
    }

}
