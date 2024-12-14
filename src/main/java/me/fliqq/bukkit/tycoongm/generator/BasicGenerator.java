package me.fliqq.bukkit.tycoongm.generator;

import java.util.UUID;

import org.bukkit.Location;

public class BasicGenerator extends AbstractGenerator{

    public BasicGenerator(UUID ownerId, GeneratorType type, GeneratorTier tier, Location location){
        super(ownerId, type, tier, location);
    }

    @Override
    public double produce() {
        double produced = currentTier.getProductionRate();
        //TODO ADD LOGIC TO UPDATE HOLOGRAM, SPAWN ITEMS, ETC!
        return produced;
    }
}
