package me.fliqq.bukkit.tycoongm.generator;

import java.util.UUID;

import org.bukkit.Location;

public abstract class AGenerator implements IGenerator{
    protected final String generatorId;
    protected final UUID ownerId;
    protected final Location location;
    protected int tier;
    protected String resourceType;
    protected double baseProductionRate;
    public AGenerator(UUID ownerId, String generatorId, Location location, int tier, String resourceType) {
        this.ownerId = ownerId;
        this.generatorId = generatorId;
        this.location = location;
        this.tier = tier;
        this.resourceType = resourceType;
        this.baseProductionRate=calculateBaseProductionRate();
    }
    @Override
    public UUID getOwnerId() {
        return ownerId;
    }
    @Override
    public Location geLocation() {
        return location;
}
    @Override
    public int getTier() {
        return tier;
    }
    public String getResourceType() {
        return resourceType;
    }
    @Override
    public double getProductionRate() {
        return baseProductionRate * Math.pow(1.5, tier-1);
    }

    @Override
    public void upgrade() {
        tier++;
    }
    @Override
    public double produce() {
        return getProductionRate();
    }
    @Override
    public String getGeneratorId() {
        return generatorId;
    }

    protected abstract double calculateBaseProductionRate();
    
}
