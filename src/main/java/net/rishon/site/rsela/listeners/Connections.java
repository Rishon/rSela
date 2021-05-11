package net.rishon.site.rsela.listeners;

import com.google.common.eventbus.Subscribe;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.md_5.bungee.config.Configuration;
import net.rishon.site.rsela.filemanager.ConfigHandler;
import net.rishon.site.rsela.utils.ColorUtil;
import net.rishon.site.rsela.utils.Globals;

public class Connections {

    @Subscribe
    public void onMaintenanceConnection(LoginEvent event) {

        Configuration config = ConfigHandler.getConfig();

        Player player = event.getPlayer();

        if(player.hasPermission(Globals.rSela_bypass)) return;

        if(config.getBoolean("Maintenance.status")) {
            event.setResult(ResultedEvent.ComponentResult.denied(ColorUtil.format(String.valueOf(config.getStringList("Maintenance.kick-message")))));
        }
    }
}
