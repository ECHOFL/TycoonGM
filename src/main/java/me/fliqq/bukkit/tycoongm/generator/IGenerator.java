package me.fliqq.bukkit.tycoongm.generator;

import java.util.UUID;

import org.bukkit.Location;

public interface IGenerator {
    UUID getOwnerId();
    String getGeneratorId();
    Location geLocation();
    int getTier();
    double getProductionRate();
    void upgrade();
    double produce();

}
