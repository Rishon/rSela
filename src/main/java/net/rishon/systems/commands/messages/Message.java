package net.rishon.systems.commands.messages;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.md_5.bungee.config.Configuration;
import net.rishon.systems.Main;
import net.rishon.systems.filemanager.DataHandler;
import net.rishon.systems.utils.ColorUtil;
import net.rishon.systems.utils.Permissions;

import java.util.Optional;

public class Message implements SimpleCommand {

    private final Main instance;
    private final Configuration config;
    private final Permissions permissions;

    public Message(Main instance) {
        this.instance = instance;
        this.config = instance.config;
        this.permissions = instance.getPermissions();
    }

    @Override
    public void execute(final Invocation invocation) {

        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (this.config.getBoolean("Commands.Message.require-permission")) {
            if (!source.hasPermission(permissions.rSela_message)) {
                source.sendMessage(ColorUtil.format(permissions.noPermission));
                return;
            }
        }

        if (args.length <= 1) {
            source.sendMessage(ColorUtil.format(this.config.getString("Commands.Message.usage")));
            return;
        }

        if (!(source instanceof Player)) {

            Optional<Player> target = this.instance.getServer().getPlayer(args[0]);
            if (target.isEmpty()) {
                String offlineMessage = this.config.getString("Commands.Message.player-offline").replace("%target%", args[0]);
                source.sendMessage(ColorUtil.format(offlineMessage));
                return;
            }

            Player onlineTarget = target.get();

            StringBuilder message = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                message.append(args[i]).append(" ");
            }

            String msg = this.config.getString("Commands.Message.message-format").replace("%message%", message).replace("%sender%", "CONSOLE").replace("%target%", onlineTarget.getUsername());
            onlineTarget.sendMessage(ColorUtil.format(msg));
            source.sendMessage(ColorUtil.format(msg));

        } else {

            Player player = (Player) source;
            Optional<Player> target = this.instance.getServer().getPlayer(args[0]);
            if (target.isEmpty()) {
                String offlineMessage = this.config.getString("Commands.Message.player-offline").replace("%target%", args[0]);
                player.sendMessage(ColorUtil.format(offlineMessage));
                return;
            }

            Player onlineTarget = target.get();
            DataHandler dataHandler = new DataHandler(this.instance, onlineTarget.getUniqueId());

            if (dataHandler.getTPM() && !player.hasPermission(permissions.rSela_messagetoggle_bypass)) {
                String blocked_pm = this.config.getString("Commands.MessageToggle.send-fail").replace("%target%", onlineTarget.getUsername());
                player.sendMessage(ColorUtil.format(blocked_pm));
                return;
            }


            StringBuilder message = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                message.append(args[i]).append(" ");
            }

            String msg = this.config.getString("Commands.Message.message-format").replace("%message%", message).replace("%sender%", player.getUsername()).replace("%target%", onlineTarget.getUsername());
            onlineTarget.sendMessage(ColorUtil.format(msg));
            player.sendMessage(ColorUtil.format(msg));

        }
    }

    @Override
    public boolean hasPermission(SimpleCommand.Invocation invocation) {
        if (!this.config.getBoolean("Commands.Message.require-permission")) return true;
        return invocation.source().hasPermission(permissions.rSela_message);
    }
}
