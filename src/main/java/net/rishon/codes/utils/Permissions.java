package net.rishon.codes.utils;

import net.md_5.bungee.config.Configuration;
import net.rishon.codes.Main;

public class Permissions {

    private Configuration config;
    private Main instance;

    public Permissions(Main instance, Configuration config) {
        this.instance = instance;
        this.config = config;
    }

    // MESSAGES
    public String rSela_prefix = "§8[§brSela§8]§r ";
    public String noPermission = "&cYou do not have permission to execute this command.";

    // PERMISSIONS
    public String rSela_maintenance_bypass = this.config.getString("Maintenance.bypass-permission");
    public String rSela_maintenance = this.config.getString("Commands.Maintenance.permission");
    public String rSela_reload = this.config.getString("rsela.command.reload");
    public String rSela_alert = this.config.getString("Commands.Alert.permission");
    public String rSela_message = this.config.getString("Commands.Message.permission");
    public String rSela_messagetoggle = this.config.getString("Commands.MessageToggle.permission");
    public String rSela_messagetoggle_bypass = this.config.getString("Commands.MessageToggle.bypass-permission");
    public String rSela_find = this.config.getString("Commands.Find.permission");
    public String rSela_clearchat = this.config.getString("Commands.ClearChat.permission");
    public String rSela_send = this.config.getString("Commands.Send.permission");
    public String rSela_serversend = this.config.getString("Commands.ServerSend.permission");
    public String rSela_ip = this.config.getString("Commands.IP.permission");
    public String rSela_staffchat = this.config.getString("Commands.StaffChat.permission");
    public String rSela_muteserver = this.config.getString("Commands.MuteServer.permission");
    public String rSela_muteserver_bypass = this.config.getString("Commands.MuteServer.bypass-permission");

}
