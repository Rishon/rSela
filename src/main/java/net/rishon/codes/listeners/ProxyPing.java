package net.rishon.codes.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.config.Configuration;
import net.rishon.codes.filemanager.FileHandler;
import net.rishon.codes.utils.Permissions;

public class ProxyPing {

    private MiniMessage miniMessage;

    public ProxyPing(MiniMessage miniMessage) {
        this.miniMessage = miniMessage;
    }

    private final Configuration config = FileHandler.getConfig();
    private final Permissions permissions = new Permissions();

    @Subscribe
    public void serverPing(ProxyPingEvent event) {

        ServerPing.Builder pingBuilder = event.getPing().asBuilder();

        String getMOTD = config.getString("MOTD.description");
        if (getMOTD != null) {
            Component component = this.miniMessage.deserialize(getMOTD);
            pingBuilder.description(component);
        }
        event.setPing(pingBuilder.build());
    }
}
