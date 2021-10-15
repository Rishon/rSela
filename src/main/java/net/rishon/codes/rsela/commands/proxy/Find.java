package net.rishon.codes.rsela.commands.proxy;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.rsela.filemanager.FileHandler;
import net.rishon.codes.rsela.utils.ColorUtil;
import net.rishon.codes.rsela.utils.Permissions;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Find implements SimpleCommand {

    private final ProxyServer server;

    public Find(ProxyServer server) {
        this.server = server;
    }

    Configuration config = FileHandler.getConfig();

    @Override
    public void execute(final Invocation invocation) {

        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (config.getBoolean("Commands.Find.require-permission")) {
            if (!source.hasPermission(Permissions.rSela_find)) {
                source.sendMessage(ColorUtil.format(Permissions.noPermission));
                return;
            }
        }

        if (args.length == 0) {
            source.sendMessage(ColorUtil.format(config.getString("Commands.Find.usage")));
            return;
        }

        Optional<Player> player = server.getPlayer(args[0]);

        String offlineMessage = config.getString("Commands.Find.offline-message").replace("%target%", args[0]);

        if (!player.isPresent()) {
            source.sendMessage(ColorUtil.format(offlineMessage));
            return;
        }

        String foundMessage = config.getString("Commands.Find.online-message").replace("%target%", args[0]).replace("%server%", player.get().getCurrentServer().get().getServerInfo().getName());

        source.sendMessage(ColorUtil.format(foundMessage));
    }

    @Override
    public List<String> suggest(final SimpleCommand.Invocation invocation) {

        CommandSource source = invocation.source();
        String[] currentArgs = invocation.arguments();

        Stream<String> possibilities = server.getAllPlayers().stream().map(rs -> rs.getGameProfile().getName());

        if (currentArgs.length == 0 && source.hasPermission(Permissions.rSela_find)) {
            return possibilities.collect(Collectors.toList());
        } else if (currentArgs.length == 1 && source.hasPermission(Permissions.rSela_find)) {
            return possibilities
                    .filter(name -> name.regionMatches(true, 0, currentArgs[0], 0, currentArgs[0].length()))
                    .collect(Collectors.toList());
        } else {
            return ImmutableList.of();
        }
    }

    @Override
    public boolean hasPermission(SimpleCommand.Invocation invocation) {
        if (!config.getBoolean("Commands.Find.require-permission")) return true;
        return invocation.source().hasPermission(Permissions.rSela_find);
    }

}
