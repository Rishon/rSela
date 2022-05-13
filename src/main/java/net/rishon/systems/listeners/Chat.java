package net.rishon.systems.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import net.md_5.bungee.config.Configuration;
import net.rishon.systems.Main;
import net.rishon.systems.utils.ColorUtil;
import net.rishon.systems.utils.Permissions;

public class Chat {

    private final Main instance;
    private final Configuration config = Main.config;
    private final Permissions permissions;

    public Chat(Main instance) {
        this.instance = instance;
        this.permissions = instance.getPermissions();
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {

        Player player = event.getPlayer();

        if (!player.hasPermission(permissions.rSela_muteserver_bypass)) {
            if (player.getCurrentServer().isEmpty()) event.setResult(PlayerChatEvent.ChatResult.denied());
            if (this.instance.getHandler().getDataManager().mutedServers.contains(player.getCurrentServer().get().getServerInfo().getName())) {
                event.setResult(PlayerChatEvent.ChatResult.denied());
                player.sendMessage(ColorUtil.format(this.config.getString("Commands.MuteServer.muted-chat")));
            }
        }
    }

}
