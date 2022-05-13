package net.rishon.systems.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ColorUtil {
    /**
     * @return - Returning the formatted message
     * @message - The message to be colored
     */
    public static Component format(String message) {
        char translate = '&';
        return LegacyComponentSerializer.legacy(translate).deserialize(message);
    }
}
