package net.rishon.codes.rsela.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.rsela.filemanager.FileHandler;
import net.rishon.codes.rsela.utils.ColorUtil;
import net.rishon.codes.rsela.utils.Lists;
import net.rishon.codes.rsela.utils.Permissions;

public class Chat {

    Configuration config = FileHandler.getConfig();

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {

        Player player = event.getPlayer();

        if (!player.hasPermission(Permissions.rSela_muteserver_bypass)) {
            if (Lists.mutedServers.contains(player.getCurrentServer().get().getServerInfo().getName())) {
                event.setResult(PlayerChatEvent.ChatResult.denied());
                player.sendMessage(ColorUtil.format(config.getString("Commands.MuteServer.muted-chat")));
            }
        }
    }

}
