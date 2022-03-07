package net.rishon.codes.listeners;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.Main;
import net.rishon.codes.filemanager.FileHandler;
import net.rishon.codes.utils.ColorUtil;
import net.rishon.codes.utils.Permissions;

public class Connections {

    private final Configuration config = Main.getInstance().config;
    private final Permissions permissions = new Permissions();

    @Subscribe
    public void onLogin(LoginEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission(permissions.rSela_maintenance_bypass)) {
            if (this.config.getBoolean("Maintenance.status")) {
                event.setResult(ResultedEvent.ComponentResult.denied(ColorUtil.format(this.config.getString("Maintenance.kick-message"))));
            }
        }
    }

    @Subscribe
    public void onDisconnection(DisconnectEvent event) {

        Player player = event.getPlayer();
    }
}
