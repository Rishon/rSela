package net.rishon.codes.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.filemanager.FileHandler;
import net.rishon.codes.utils.ColorUtil;
import net.rishon.codes.utils.Lists;
import net.rishon.codes.utils.Permissions;

public class Chat {

    private final Configuration config = FileHandler.getConfig();
    private final Permissions permissions = new Permissions();

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {

        Player player = event.getPlayer();

        if (!player.hasPermission(permissions.rSela_muteserver_bypass)) {
            if (Lists.mutedServers.contains(player.getCurrentServer().get().getServerInfo().getName())) {
                event.setResult(PlayerChatEvent.ChatResult.denied());
                player.sendMessage(ColorUtil.format(config.getString("Commands.MuteServer.muted-chat")));
            }
        }
    }

}
