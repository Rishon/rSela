package net.rishon.systems.utils;

import net.rishon.systems.handler.MainHandler;

public class Permissions {

    public MainHandler handler;
    // MESSAGES
    public String rSela_prefix = "§8[§brSela§8]§r ";
    public String noPermission = "&cYou do not have permission to execute this command.";
    // PERMISSIONS
    public String rSela_maintenance_bypass = this.handler.getConfig().getString("Maintenance.bypass-permission");
    public String rSela_maintenance = this.handler.getConfig().getString("Commands.Maintenance.permission");
    public String rSela_reload = this.handler.getConfig().getString("rsela.command.reload");
    public String rSela_alert = this.handler.getConfig().getString("Commands.Alert.permission");
    public String rSela_message = this.handler.getConfig().getString("Commands.Message.permission");
    public String rSela_messagetoggle = this.handler.getConfig().getString("Commands.MessageToggle.permission");
    public String rSela_messagetoggle_bypass = this.handler.getConfig().getString("Commands.MessageToggle.bypass-permission");
    public String rSela_find = this.handler.getConfig().getString("Commands.Find.permission");
    public String rSela_clearchat = this.handler.getConfig().getString("Commands.ClearChat.permission");
    public String rSela_send = this.handler.getConfig().getString("Commands.Send.permission");
    public String rSela_serversend = this.handler.getConfig().getString("Commands.ServerSend.permission");
    public String rSela_ip = this.handler.getConfig().getString("Commands.IP.permission");
    public String rSela_staffchat = this.handler.getConfig().getString("Commands.StaffChat.permission");
    public String rSela_muteserver = this.handler.getConfig().getString("Commands.MuteServer.permission");
    public String rSela_muteserver_bypass = this.handler.getConfig().getString("Commands.MuteServer.bypass-permission");
    public Permissions(MainHandler handler) {
        this.handler = handler;
    }

}
