package me.fliqq.bukkit.tycoongm.generator;

import java.util.UUID;

import org.bukkit.Location;

public class BasicGenerator extends AGenerator{
    public BasicGenerator(UUID ownerId,String generatorId, Location location, int tier, String resourceType){
        super(ownerId,generatorId, location, tier, resourceType);
    }

    @Override
    protected double calculateBaseProductionRate() {
        return 10.0;
    }

}


