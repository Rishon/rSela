package net.rishon.site.rsela.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ColorUtil {

    public static Component format(String message) {
        char translate = '&';
        return LegacyComponentSerializer.legacy(translate).deserialize(message);
    }
}
