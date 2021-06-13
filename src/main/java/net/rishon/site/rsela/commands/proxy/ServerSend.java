package net.rishon.site.rsela.commands.proxy;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.md_5.bungee.config.Configuration;
import net.rishon.site.rsela.filemanager.ConfigHandler;
import net.rishon.site.rsela.utils.ColorUtil;
import net.rishon.site.rsela.utils.Globals;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerSend implements Command {

    private final ProxyServer server;

    public ServerSend(ProxyServer server) {
        this.server = server;
    }

    Configuration config = ConfigHandler.getConfig();

    @Override
    public void execute(CommandSource source, String[] args) {

        if (!source.hasPermission(Globals.rSela_serversend)) {
            source.sendMessage(ColorUtil.format(Globals.noPermission));
            return;
        }

        if (args.length < 2) {
            source.sendMessage(ColorUtil.format(config.getString("Commands.ServerSend.usage")));
            return;
        }

        String invalidFirst = config.getString("Commands.ServerSend.invalid-server").replace("%server%", args[0]);

        Optional<RegisteredServer> serverFrom = server.getServer(args[0]);
        if (!serverFrom.isPresent()) {
            source.sendMessage(ColorUtil.format(invalidFirst));
            return;
        }

        String invalidSecondary = config.getString("Commands.ServerSend.invalid-server").replace("%server%", args[1]);

        Optional<RegisteredServer> serverTo = server.getServer(args[1]);
        if (!serverTo.isPresent()) {
            source.sendMessage(ColorUtil.format(invalidSecondary));
            return;
        }

        if (serverFrom == serverTo) {
            source.sendMessage(ColorUtil.format(config.getString("Commands.ServerSend.same-target")));
            return;
        }

        String sentPlayers = config.getString("Commands.ServerSend.sent-players").replace("%firstServer%", args[0]).replace("%secondaryServer%", args[1]);

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
    public List<String> suggest(CommandSource source, String[] currentArgs) {

        Stream<String> serverPossibilities = server.getAllServers().stream()
                .map(rs -> rs.getServerInfo().getName());

        if (currentArgs.length == 0 && source.hasPermission(Globals.rSela_serversend)) {
            return serverPossibilities.collect(Collectors.toList());
        } else if (currentArgs.length == 1 && source.hasPermission(Globals.rSela_serversend)) {
            return serverPossibilities
                    .filter(name -> name.regionMatches(true, 0, currentArgs[0], 0, currentArgs[0].length()))
                    .collect(Collectors.toList());
        } else if (currentArgs.length == 2 && source.hasPermission(Globals.rSela_serversend)) {
            return serverPossibilities
                    .filter(name -> name.regionMatches(true, 0, currentArgs[1], 0, currentArgs[1].length()))
                    .collect(Collectors.toList());
        } else {
            return ImmutableList.of();
        }
    }
}
