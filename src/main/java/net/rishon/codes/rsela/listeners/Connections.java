package net.rishon.codes.rsela.listeners;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.rsela.filemanager.FileHandler;
import net.rishon.codes.rsela.utils.ColorUtil;
import net.rishon.codes.rsela.utils.Permissions;

public class Connections {

    Configuration config = FileHandler.getConfig();

    @Subscribe
    public void onLogin(LoginEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission(Permissions.rSela_maintenance_bypass)) {
            if (config.getBoolean("Maintenance.status")) {
                event.setResult(ResultedEvent.ComponentResult.denied(ColorUtil.format(config.getString("Maintenance.kick-message"))));
            }
        }
    }

    @Subscribe
    public void onDisconnection(DisconnectEvent event) {

        Player player = event.getPlayer();
    }
}
