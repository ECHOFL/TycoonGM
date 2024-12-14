package me.fliqq.bukkit.tycoongm.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Location;

public class GeneratorManager {
    private final Map<UUID, Map<String, AGenerator>> playerGenerators;
    public GeneratorManager(){
        this.playerGenerators=new HashMap<>();
    }

    public AGenerator createGenerator(UUID ownerId, String generatorId, Location location, int tier, String resourceType){
        AGenerator generator = new BasicGenerator(ownerId, generatorId, location, tier, resourceType);
        playerGenerators.computeIfAbsent(ownerId, k -> new HashMap<>()).put(generatorId, generator);
        return generator;
    }

    public void removeGenerator(UUID ownerId, String generatorId){
        Map<String, AGenerator> ownersGenerator = playerGenerators.get(ownerId);
        if(ownersGenerator != null ){
            ownersGenerator.remove(generatorId);
            if(ownersGenerator.isEmpty()){
                playerGenerators.remove(ownerId);
            }
        }
    }
    @Nullable
    public AGenerator getGenerator(UUID ownerId, String generatorId){
        Map<String, AGenerator> ownersGenerator = playerGenerators.get(ownerId);
        return ownersGenerator != null ? ownersGenerator.get(generatorId) : null;
    }
    public List<AGenerator> getPlayerGenerators(UUID ownerId){
        Map<String, AGenerator> ownersGenerators = playerGenerators.get(ownerId);
        return ownersGenerators != null ? new ArrayList<>(ownersGenerators.values()) : new ArrayList<>();
    }
    
    public void upgradeGenerator(UUID ownerId, String generatorId){
        AGenerator generator = getGenerator(ownerId, generatorId);
        if(generator != null){
            generator.upgrade();
        }
    }
    public void processAllGenerators(){
        for(Map<String, AGenerator> ownersGenerators : playerGenerators.values()){
            for(AGenerator generator : ownersGenerators.values()){
                double production = generator.produce();
                //TODO: add produced resource to player inventory or balance
            }
        }
    }


    //SAVE AND LOAD METHOD
    
}