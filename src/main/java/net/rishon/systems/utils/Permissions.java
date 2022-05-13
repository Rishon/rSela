package net.rishon.systems.utils;

import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.rishon.systems.Main;

@Getter
public class Permissions {

    private final Configuration config = Main.config;

    // MESSAGES
    public String rSela_prefix = "§8[§brSela§8]§r ";
    public String noPermission = "&cYou do not have permission to execute this command.";
    // PERMISSIONS
    public String rSela_maintenance_bypass = this.getConfig().getString("Maintenance.bypass-permission");
    public String rSela_maintenance = this.getConfig().getString("Commands.Maintenance.permission");
    public String rSela_reload = this.getConfig().getString("rsela.command.reload");
    public String rSela_alert = this.getConfig().getString("Commands.Alert.permission");
    public String rSela_message = this.getConfig().getString("Commands.Message.permission");
    public String rSela_messagetoggle = this.getConfig().getString("Commands.MessageToggle.permission");
    public String rSela_messagetoggle_bypass = this.getConfig().getString("Commands.MessageToggle.bypass-permission");
    public String rSela_find = this.getConfig().getString("Commands.Find.permission");
    public String rSela_clearchat = this.getConfig().getString("Commands.ClearChat.permission");
    public String rSela_send = this.getConfig().getString("Commands.Send.permission");
    public String rSela_serversend = this.getConfig().getString("Commands.ServerSend.permission");
    public String rSela_ip = this.getConfig().getString("Commands.IP.permission");
    public String rSela_staffchat = this.getConfig().getString("Commands.StaffChat.permission");
    public String rSela_muteserver = this.getConfig().getString("Commands.MuteServer.permission");
    public String rSela_muteserver_bypass = this.getConfig().getString("Commands.MuteServer.bypass-permission");

}
