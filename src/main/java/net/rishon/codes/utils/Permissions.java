package net.rishon.codes.utils;

import net.md_5.bungee.config.Configuration;
import net.rishon.codes.filemanager.FileHandler;

public class Permissions {

    private final Configuration config = FileHandler.getConfig();

    // PERMISSIONS

    public String rSela_maintenance_bypass = config.getString("Maintenance.bypass-permission");
    public String rSela_maintenance = config.getString("Commands.Maintenance.permission");
    public String rSela_reload = config.getString("rsela.command.reload");
    public String rSela_alert = config.getString("Commands.Alert.permission");
    public String rSela_message = config.getString("Commands.Message.permission");
    public String rSela_messagetoggle = config.getString("Commands.MessageToggle.permission");
    public String rSela_messagetoggle_bypass = config.getString("Commands.MessageToggle.bypass-permission");
    public String rSela_find = config.getString("Commands.Find.permission");
    public String rSela_clearchat = config.getString("Commands.ClearChat.permission");
    public String rSela_send = config.getString("Commands.Send.permission");
    public String rSela_serversend = config.getString("Commands.ServerSend.permission");
    public String rSela_ip = config.getString("Commands.IP.permission");
    public String rSela_staffchat = config.getString("Commands.StaffChat.permission");
    public String rSela_muteserver = config.getString("Commands.MuteServer.permission");
    public String rSela_muteserver_bypass = config.getString("Commands.MuteServer.bypass-permission");

    // MESSAGES
    public String rSela_prefix = "§8[§brSela§8]§r ";
    public String noPermission = "&cYou do not have permission to execute this command.";

}
