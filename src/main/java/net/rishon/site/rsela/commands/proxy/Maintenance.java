package net.rishon.site.rsela.commands.proxy;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.md_5.bungee.config.Configuration;
import net.rishon.site.rsela.filemanager.ConfigHandler;
import net.rishon.site.rsela.utils.ColorUtil;
import net.rishon.site.rsela.utils.Globals;

public class Maintenance implements Command {

    Configuration config = ConfigHandler.getConfig();

    @Override
    public void execute(CommandSource source, String[] args) {

        if (!source.hasPermission(Globals.rSela_maintenance)) {
            source.sendMessage(ColorUtil.format(Globals.noPermission));
            return;
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
}