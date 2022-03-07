package net.rishon.codes.commands.messages;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.filemanager.DataHandler;
import net.rishon.codes.filemanager.FileHandler;
import net.rishon.codes.utils.ColorUtil;
import net.rishon.codes.utils.Permissions;

public class MessageToggle implements SimpleCommand {

    private final ProxyServer server;

    public MessageToggle(ProxyServer server) {
        this.server = server;
    }

    private final Configuration config = FileHandler.getConfig();
    private final Permissions permissions = new Permissions();

    @Override
    public void execute(final Invocation invocation) {

        CommandSource source = invocation.source();

        if (!(source instanceof Player)) {
            source.sendMessage(ColorUtil.format("&cOnly players may execute this command."));
            return;
        }

        if (config.getBoolean("Commands.MessageToggle.require-permission")) {
            if (!source.hasPermission(permissions.rSela_message)) {
                source.sendMessage(ColorUtil.format(permissions.noPermission));
                return;
            }
        }

        Player player = (Player) source;


        DataHandler dataHandler = new DataHandler(player.getUniqueId());
        if (dataHandler.getTPM()) {
            player.sendMessage(ColorUtil.format(config.getString("Commands.MessageToggle.toggled-on")));
            dataHandler.setTPM(false);
        } else if (!dataHandler.getTPM()) {
            player.sendMessage(ColorUtil.format(config.getString("Commands.MessageToggle.toggled-off")));
            dataHandler.setTPM(true);
        }

    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        if (!config.getBoolean("Commands.MessageToggle.require-permission")) return true;
        return invocation.source().hasPermission(permissions.rSela_messagetoggle);
    }
}
