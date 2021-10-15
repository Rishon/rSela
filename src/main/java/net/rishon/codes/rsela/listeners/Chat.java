package net.rishon.codes.rsela.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import net.rishon.codes.rsela.utils.ColorUtil;
import net.rishon.codes.rsela.utils.Lists;

public class Chat {

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {

        Player player = event.getPlayer();

        if (!player.hasPermission("")) {
            if (Lists.mutedServers.contains(player.getCurrentServer().get().getServerInfo().getName())) {
                event.setResult(PlayerChatEvent.ChatResult.denied());
                player.sendMessage(ColorUtil.format(""));
            }
        }
    }

}
