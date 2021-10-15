package net.rishon.codes.rsela.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.rsela.filemanager.FileHandler;

public class ProxyPing {

    private final MiniMessage miniMessage = MiniMessage.get();

    Configuration config = FileHandler.getConfig();

    @Subscribe
    public void serverPing(ProxyPingEvent event) {

        ServerPing.Builder pingBuilder = event.getPing().asBuilder();

        String getMOTD = config.getString("MOTD.description");
        if (getMOTD != null) {
            Component component = this.miniMessage.parse(getMOTD);
            pingBuilder.description(component);
        }
        event.setPing(pingBuilder.build());
    }
}
