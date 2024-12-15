package me.fliqq.bukkit.tycoongm.generator;

import java.util.UUID;

import org.bukkit.Location;

public interface IGenerator {
    String getGeneratorId();
    UUID getOwnerId();
    int getTier();
    void upgrade();
    double produce();
    Location getLocation();
    GeneratorType getGeneratorType();
    GeneratorTier getCurrentTier();
    boolean canUpgrade();
}
