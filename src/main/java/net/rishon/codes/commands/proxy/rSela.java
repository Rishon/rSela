package net.rishon.codes.commands.proxy;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.filemanager.FileHandler;
import net.rishon.codes.utils.ColorUtil;
import net.rishon.codes.utils.Permissions;

public class rSela implements SimpleCommand {

    private final Configuration config = FileHandler.getConfig();
    private final Permissions permissions = new Permissions();

    @Override
    public void execute(final Invocation invocation) {

        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!source.hasPermission(permissions.rSela_reload)) {
            source.sendMessage(ColorUtil.format(permissions.noPermission));
            return;
        }

        if (args.length == 0) {
            source.sendMessage(ColorUtil.format("&7Usage: /rsela <reload/version>"));
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (FileHandler.loadConfig()) {
                source.sendMessage(ColorUtil.format(permissions.rSela_prefix + "&aconfig.yml &7has been reloaded."));
            } else {
                source.sendMessage(ColorUtil.format("&cFailed to reload &aconfig.yml"));
            }
        } else if (args[0].equalsIgnoreCase("version")) {
            source.sendMessage(ColorUtil.format(permissions.rSela_prefix + "&8• &fYou are running rSela version: &e" + config.getString("version") + " &8• &fAuthor: &bRishon"));
        }
    }
}