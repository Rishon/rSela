package net.rishon.site.rsela.commands.proxy;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.config.Configuration;
import net.rishon.site.rsela.filemanager.ConfigHandler;
import net.rishon.site.rsela.utils.ColorUtil;
import net.rishon.site.rsela.utils.Globals;

public class rSela implements Command {

    @Override
    public void execute(CommandSource source, String[] args) {

        if (!source.hasPermission(Globals.rSela_reload)) {
            source.sendMessage(Component.text(Globals.noPermission));
            return;
        }

        if (args.length == 0) {
            source.sendMessage(Component.text("§7Usage: /rsela <reload/version>"));
            return;
        }

        Configuration config = ConfigHandler.getConfig();

        if (args[0].equalsIgnoreCase("reload")) {
            if (ConfigHandler.loadConfig()) {
                source.sendMessage(ColorUtil.format(Globals.rSela_prefix + "&aconfig.yml &7has been reloaded."));
            } else {
                source.sendMessage(ColorUtil.format("&cFailed to reload &aconfig.yml"));
            }
        } else if (args[0].equalsIgnoreCase("version")) {
            source.sendMessage(ColorUtil.format(Globals.rSela_prefix + "&8• &fYou are running rSela version: &e" + config.getString("version") + " &8• &fAuthor: &bRishon"));
        }
    }
}