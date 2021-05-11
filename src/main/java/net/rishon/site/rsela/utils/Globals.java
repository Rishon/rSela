package net.rishon.site.rsela.utils;

import net.md_5.bungee.config.Configuration;
import net.rishon.site.rsela.filemanager.ConfigHandler;

public class Globals {

    // PERMISSIONS

    private static final Configuration config = ConfigHandler.getConfig();

    public static String rSela_reload = "rsela.command.reload";
    public static String rSela_alert = config.getString("Alert.permission");
    public static String rSela_find = config.getString("Find.permission");
    public static String rSela_send = config.getString("Send.permission");
    public static String rSela_serversend = config.getString("ServerSend.permission");

    // MESSAGES
    public static String rSela_prefix = "§8[§brSela§8]§r ";
    public static String noPermission = "&cYou do not have permission to execute this command.";

}
