package net.rishon.codes.rsela.commands.messages;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.rsela.filemanager.DataHandler;
import net.rishon.codes.rsela.filemanager.FileHandler;
import net.rishon.codes.rsela.utils.ColorUtil;
import net.rishon.codes.rsela.utils.Permissions;

public class MessageToggle implements SimpleCommand {

    private final ProxyServer server;

    public MessageToggle(ProxyServer server) {
        this.server = server;
    }

    Configuration config = FileHandler.getConfig();

    @Override
    public void execute(final Invocation invocation) {

        CommandSource source = invocation.source();

        if (!(source instanceof Player)) {
            source.sendMessage(ColorUtil.format("&cOnly players may execute this command."));
            return;
        }

        if (config.getBoolean("Commands.MessageToggle.require-permission")) {
            if (!source.hasPermission(Permissions.rSela_message)) {
                source.sendMessage(ColorUtil.format(Permissions.noPermission));
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
        return invocation.source().hasPermission(Permissions.rSela_messagetoggle);
    }
}
