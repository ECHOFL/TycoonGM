package me.fliqq.bukkit.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ComponentUtil {

    public static Component parseComponent(String text){
        return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
    }
}
