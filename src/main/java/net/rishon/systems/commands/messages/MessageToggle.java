package net.rishon.systems.commands.messages;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.md_5.bungee.config.Configuration;
import net.rishon.systems.Main;
import net.rishon.systems.filemanager.DataHandler;
import net.rishon.systems.utils.ColorUtil;
import net.rishon.systems.utils.Permissions;

public class MessageToggle implements SimpleCommand {

    private final Main instance;
    private final Configuration data = Main.data;
    private final Permissions permissions;

    public MessageToggle(Main instance) {
        this.instance = instance;
        this.permissions = instance.getPermissions();
    }

    @Override
    public void execute(final Invocation invocation) {

        CommandSource source = invocation.source();

        if (!(source instanceof Player)) {
            source.sendMessage(ColorUtil.format("&cOnly players may execute this command."));
            return;
        }

        if (this.data.getBoolean("Commands.MessageToggle.require-permission")) {
            if (!source.hasPermission(permissions.rSela_message)) {
                source.sendMessage(ColorUtil.format(permissions.noPermission));
                return;
            }
        }

        Player player = (Player) source;

        DataHandler dataHandler = new DataHandler(this.instance, player.getUniqueId());
        if (dataHandler.getTPM()) {
            player.sendMessage(ColorUtil.format(this.data.getString("Commands.MessageToggle.toggled-on")));
            dataHandler.setTPM(false);
        } else if (!dataHandler.getTPM()) {
            player.sendMessage(ColorUtil.format(this.data.getString("Commands.MessageToggle.toggled-off")));
            dataHandler.setTPM(true);
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        if (!this.data.getBoolean("Commands.MessageToggle.require-permission")) return true;
        return invocation.source().hasPermission(permissions.rSela_messagetoggle);
    }
}
