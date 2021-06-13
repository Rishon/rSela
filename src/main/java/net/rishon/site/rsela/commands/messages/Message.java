package net.rishon.site.rsela.commands.messages;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.rishon.site.rsela.filemanager.ConfigHandler;
import net.rishon.site.rsela.utils.ColorUtil;
import net.rishon.site.rsela.utils.Permissions;

import java.util.Optional;

public class Message implements Command {

    private final ProxyServer server;

    public Message(ProxyServer server) {
        this.server = server;
    }

    Configuration config = ConfigHandler.getConfig();

    @Override
    public void execute(CommandSource source, String[] args) {

        if (!source.hasPermission(Permissions.rSela_message)) {
            source.sendMessage(ColorUtil.format(Permissions.noPermission));
            return;
        }

        if (args.length <= 1) {
            source.sendMessage(ColorUtil.format(config.getString("Commands.Message.usage")));
            return;
        }


        if (!(source instanceof Player)) {

            Optional<Player> target = server.getPlayer(args[0]);
            if(!target.isPresent()) {
                String offlineMessage = config.getString("Commands.Message.player-offline").replace("%target%", args[0]);
                source.sendMessage(ColorUtil.format(offlineMessage));
                return;
            }

            Player onlineTarget = target.get();

            StringBuilder message = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                message.append(args[i]).append(" ");
            }

            String msg = config.getString("Commands.Message.message-format").replace("%message%", message).replace("%sender%", "CONSOLE").replace("%target%", onlineTarget.getUsername());
            onlineTarget.sendMessage(ColorUtil.format(msg));
            source.sendMessage(ColorUtil.format(msg));

        } else {

            Player player = (Player) source;
            Optional<Player> target = server.getPlayer(args[0]);
            if(!target.isPresent()) {
                String offlineMessage = config.getString("Commands.Message.player-offline").replace("%target%", args[0]);
                player.sendMessage(ColorUtil.format(offlineMessage));
                return;
            }

            Player onlineTarget = target.get();

            StringBuilder message = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                message.append(args[i]).append(" ");
            }

            String msg = config.getString("Commands.Message.message-format").replace("%message%", message).replace("%sender%", player.getUsername()).replace("%target%", onlineTarget.getUsername());
            onlineTarget.sendMessage(ColorUtil.format(msg));
            player.sendMessage(ColorUtil.format(msg));

        }
    }
}
