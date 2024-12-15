package me.fliqq.bukkit.tycoongm.generator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.bukkit.Location;

public abstract class AbstractGenerator implements IGenerator{
    private static final Map<UUID, AtomicLong> PLAYER_SEQUENCES = new HashMap<>();    
    protected final String generatorId;
    protected final UUID ownerId;
    protected final GeneratorType type;
    protected GeneratorTier currentTier;
    protected Location location;

    public AbstractGenerator(UUID ownerId, GeneratorType type, GeneratorTier tier, Location location){
        this.generatorId=generateUniqueId(ownerId, type.getId());
        this.ownerId=ownerId;
        this.type=type;
        this.currentTier=tier;
        this.location=location;
    }


    private String generateUniqueId(UUID ownerId, String generatorType) {
        AtomicLong sequence = PLAYER_SEQUENCES.computeIfAbsent(ownerId, k -> new AtomicLong(0));
        return ownerId.toString() + "-" + generatorType + "-" + sequence.incrementAndGet();
    }

    public static void initializePlayerSequence(UUID playerId, long maxExistingId) {
        PLAYER_SEQUENCES.computeIfAbsent(playerId, k -> new AtomicLong(0)).set(maxExistingId);
    }
    @Override
    public String getGeneratorId() {
        return generatorId;
    }

    @Override
    public UUID getOwnerId() {
        return ownerId;
    }

    @Override
    public int getTier() {
        return currentTier.getTier();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public GeneratorType getGeneratorType() {
        return type;
    }

    @Override
    public GeneratorTier getCurrentTier() {
        return currentTier;
    }

    @Override
    public boolean canUpgrade() {
        return type.getTiers().containsKey(currentTier.getTier()+1);
    }

    @Override
    public void upgrade() {
        if(canUpgrade()){
            currentTier = type.getTiers().get(currentTier.getTier()+1);
        }
        
    }

    //REMAIN ABSTRACT CAUSE VARY FOR DIFFERENT GENERATORS
    @Override
    public abstract double produce();
    
}
