package me.fliqq.bukkit.tycoongm.generator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GeneratorTier {
    private final int tier;
    private final double price;
    private final int productionRate;
    private final int productionInterval;
}
