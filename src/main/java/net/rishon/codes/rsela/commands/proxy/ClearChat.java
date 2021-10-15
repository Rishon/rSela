package net.rishon.codes.rsela.commands.proxy;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.rsela.filemanager.FileHandler;
import net.rishon.codes.rsela.utils.ColorUtil;
import net.rishon.codes.rsela.utils.Permissions;

import java.util.Collection;

public class ClearChat implements SimpleCommand {

    private final ProxyServer server;

    public ClearChat(ProxyServer server) {
        this.server = server;
    }

    Configuration config = FileHandler.getConfig();

    @Override
    public void execute(final Invocation invocation) {

        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (config.getBoolean("Commands.ClearChat.require-permission")) {
            if (!source.hasPermission(Permissions.rSela_clearchat)) {
                source.sendMessage(ColorUtil.format(Permissions.noPermission));
                return;
            }
        }

        if (args.length == 0) {
            source.sendMessage(ColorUtil.format(config.getString("Commands.ClearChat.usage")));
            return;
        }

        if (!(source instanceof Player)) {
            String clearedMsg = config.getString("Commands.ClearChat.message").replace("%executor%", "CONSOLE");

            if (args[0].equalsIgnoreCase("all")) {
                for (Player network : server.getAllPlayers()) {
                    for (int i = 0; i < 100; i++) {
                        network.sendMessage(Component.text("\n "));
                    }
                    network.sendMessage(ColorUtil.format(clearedMsg));
                    source.sendMessage(ColorUtil.format("&aYou have cleared the server chat."));
                }
            } else if (args[0].equalsIgnoreCase("server")) {
                source.sendMessage(ColorUtil.format("&cThis command is only available for players."));
            }

        } else {

            Player player = (Player) source;

            String clearedMsg = config.getString("Commands.ClearChat.message").replace("%executor%", player.getUsername());

            if (args[0].equalsIgnoreCase("all")) {

                Collection<Player> networkPlayers = server.getAllPlayers();

                for (Player network : networkPlayers) {
                    for (int i = 0; i < 100; i++) {
                        network.sendMessage(Component.text("\n "));
                    }
                    network.sendMessage(ColorUtil.format(clearedMsg));
                }
            } else if (args[0].equalsIgnoreCase("server")) {

                Collection<Player> serverPlayers = player.getCurrentServer().get().getServer().getPlayersConnected();

                for (Player network : serverPlayers) {
                    for (int i = 0; i < 100; i++) {
                        network.sendMessage(Component.text("\n "));
                    }
                    network.sendMessage(ColorUtil.format(clearedMsg));
                }
            }
        }
    }

    @Override
    public boolean hasPermission(SimpleCommand.Invocation invocation) {
        if (!config.getBoolean("Commands.ClearChat.require-permission")) return true;
        return invocation.source().hasPermission(Permissions.rSela_clearchat);
    }

}
