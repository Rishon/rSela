package net.rishon.systems.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.config.Configuration;
import net.rishon.systems.Main;

public class ProxyPing {

    private final Configuration config = Main.config;
    private final MiniMessage miniMessage;

    public ProxyPing(Main instance, MiniMessage miniMessage) {
        this.miniMessage = miniMessage;
    }

    @Subscribe
    void serverPing(ProxyPingEvent event) {
        ServerPing.Builder pingBuilder = event.getPing().asBuilder();
        String getMOTD = this.config.getString("MOTD.description");
        if (getMOTD != null) {
            Component component = this.miniMessage.deserialize(getMOTD);
            pingBuilder.description(component);
        }
        event.setPing(pingBuilder.build());
    }
}
