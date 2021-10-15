package net.rishon.codes.rsela.commands.proxy;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.rsela.utils.ColorUtil;
import net.rishon.codes.rsela.filemanager.ConfigHandler;
import net.rishon.codes.rsela.utils.Permissions;

public class Maintenance implements SimpleCommand {

    Configuration config = ConfigHandler.getConfig();

    @Override
    public void execute(final Invocation invocation) {

        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (config.getBoolean("Commands.Maintenance.require-permission")) {
            if (!source.hasPermission(Permissions.rSela_maintenance)) {
                source.sendMessage(ColorUtil.format(Permissions.noPermission));
                return;
            }
        }

        String status = "";
        if (config.getBoolean("Maintenance.status")) {
            status = "enabled";
        } else {
            status = "disabled";
        }

        String usageMessage = config.getString("Commands.Maintenance.usage").replace("%status%", status);

        if (args.length == 0) {
            source.sendMessage(ColorUtil.format(usageMessage));
            return;
        }

        if (args[0].equalsIgnoreCase("on")) {
            if (config.getBoolean("Maintenance.status")) {
                source.sendMessage(ColorUtil.format(config.getString("Commands.Maintenance.already-on-message")));
                return;
            }
            source.sendMessage(ColorUtil.format(config.getString("Commands.Maintenance.on-message")));
            config.set("Maintenance.status", true);
            ConfigHandler.saveConfig();
        } else if (args[0].equalsIgnoreCase("off")) {
            if (!config.getBoolean("Maintenance.status")) {
                source.sendMessage(ColorUtil.format(config.getString("Commands.Maintenance.already-off-message")));
                return;
            }
            source.sendMessage(ColorUtil.format(config.getString("Commands.Maintenance.off-message")));
            config.set("Maintenance.status", false);
            ConfigHandler.saveConfig();
        }
    }

    @Override
    public boolean hasPermission(SimpleCommand.Invocation invocation) {
        if (!config.getBoolean("Commands.Maintenance.require-permission")) return true;
        return invocation.source().hasPermission(Permissions.rSela_maintenance);
    }
}