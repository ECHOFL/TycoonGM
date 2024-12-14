package me.fliqq.bukkit.tycoongm.generator;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;

@Getter
@Setter
@AllArgsConstructor
public class GeneratorType {
    private final String id;
    private final Component genDisplayName;
    private final Material item;
    private final Material blockType;
    private final Component itemDisplayName;
    private final double sellPrice;
    private final List<Component> hologramLines;
    private final List<Component> genLore;
    private final List<Component> itemLore;
    private final Map<Integer, GeneratorTier> tiers; //available tiers of this type of gen.
}
