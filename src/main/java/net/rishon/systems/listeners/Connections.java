package net.rishon.systems.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.md_5.bungee.config.Configuration;
import net.rishon.systems.Main;
import net.rishon.systems.handler.MainHandler;
import net.rishon.systems.utils.ColorUtil;
import net.rishon.systems.utils.Permissions;

public class Connections {

    private final MainHandler handler;
    private final Configuration config = Main.config;
    private final Permissions permissions;

    public Connections(Main instance) {
        this.handler = instance.getHandler();
        this.permissions = instance.getPermissions();
    }

    @Subscribe(order = PostOrder.FIRST)
    void onLogin(LoginEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission(permissions.rSela_maintenance_bypass))
            if (this.config.getBoolean("Maintenance.status"))
                event.setResult(ResultedEvent.ComponentResult.denied(ColorUtil.format(this.config.getString("Maintenance.kick-message"))));
    }

    @Subscribe
    void onDisconnect(DisconnectEvent event) {
        this.handler.getData().set("toggled_messages", this.handler.getDataManager().toggled_messages);
    }
}
