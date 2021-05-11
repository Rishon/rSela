package net.rishon.site.rsela.utils;

import net.md_5.bungee.config.Configuration;
import net.rishon.site.rsela.filemanager.ConfigHandler;

public class Globals {

    private static final Configuration config = ConfigHandler.getConfig();

    // PERMISSIONS

    public static String rSela_bypass = config.getString("Maintenance.bypass-permission");
    public static String rSela_maintenance = config.getString("Commands.Maintenance.permission");
    public static String rSela_reload = "rsela.command.reload";
    public static String rSela_alert = config.getString("Commands.Alert.permission");
    public static String rSela_find = config.getString("Commands.Find.permission");
    public static String rSela_send = config.getString("Commands.Send.permission");
    public static String rSela_serversend = config.getString("Commands.ServerSend.permission");
    public static String rSela_ip = config.getString("Commands.IP.permission");
    public static String rSela_staffchat = config.getString("Commands.StaffChat.permission");

    // MESSAGES
    public static String rSela_prefix = "§8[§brSela§8]§r ";
    public static String noPermission = "&cYou do not have permission to execute this command.";

}
