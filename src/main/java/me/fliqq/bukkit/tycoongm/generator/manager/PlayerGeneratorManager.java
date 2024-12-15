package me.fliqq.bukkit.tycoongm.generator.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import me.fliqq.bukkit.tycoongm.generator.IGenerator;

public class PlayerGeneratorManager {
    private final Map<UUID, List<IGenerator>> playerGenerators = new HashMap<>();
    private final GeneratorManager generatorManager;
    private final PlayerGeneratorDataManager dataManager;

    public PlayerGeneratorManager(GeneratorManager generatorManager, PlayerGeneratorDataManager dataManager) {
        this.generatorManager = generatorManager;
        this.dataManager = dataManager;
        loadAllPlayerGenerators();
    }

    private void loadAllPlayerGenerators() {
        Map<UUID, List<String>> allPlayerGeneratorIds = dataManager.getAllPlayerGenerators();
        for (Map.Entry<UUID, List<String>> entry : allPlayerGeneratorIds.entrySet()) {
            UUID playerId = entry.getKey();
            for (String generatorId : entry.getValue()) {
                IGenerator generator = generatorManager.getGeneratorFromId(generatorId);
                if (generator != null) {
                    addGenerator(playerId, generator);
                }
            }
        }
    }
    public void loadPlayerGenerators(UUID playerId){
        List<String> generatorsId = dataManager.getPlayerGenerators(playerId);
        for(String generatorId : generatorsId){
            IGenerator generator = generatorManager.getGeneratorFromId(generatorId);
            if(generator!= null)
                addGenerator(playerId, generator);
        }
    }
    public void savePlayerGenerators(UUID playerId){
        List<String> generatorsId = getPlayerGenerators(playerId).stream()
            .map(IGenerator::getGeneratorId)
            .collect(Collectors.toList());
        dataManager.setPlayerGenerators(playerId, generatorsId);
    }

    public void givePlayerStarterGenerators(UUID playerId) {
        givePlayerGenerator(playerId, "diamond", 1, 2);
        givePlayerGenerator(playerId, "emerald", 1, 1);
    }

    public void givePlayerGenerator(UUID playerId, String generatorType, int tier, int amount) {
        for (int i = 0; i < amount; i++) {
            IGenerator generator = generatorManager.createGenerator(playerId, generatorType, tier, null);
            addGenerator(playerId, generator);
            dataManager.addGeneratorToPlayer(playerId, generator.getGeneratorId());
        }
    }

    public void addGenerator(UUID playerId, IGenerator generator) {
        playerGenerators.computeIfAbsent(playerId, k -> new ArrayList<>()).add(generator);
        savePlayerGenerators(playerId);
    }

    public List<IGenerator> getPlayerGenerators(UUID playerId) {
        return playerGenerators.getOrDefault(playerId, Collections.emptyList());
    }

    public void removeGenerator(UUID playerId, IGenerator generator) {
        List<IGenerator> generators = playerGenerators.get(playerId);
        if (generators != null) {
            generators.remove(generator);
            if (generators.isEmpty()) {
                playerGenerators.remove(playerId);
            }
            generatorManager.removeGenerator(generator.getGeneratorId());
            savePlayerGenerators(playerId);
        }
    }

    public IGenerator getGeneratorFromId(UUID playerId, String generatorId) {
        List<IGenerator> generators = playerGenerators.get(playerId);
        if (generators != null) {
            for (IGenerator generator : generators) {
                if (generator.getGeneratorId().equals(generatorId)) {
                    return generator;
                }
            }
        }
        return null;
    }
}
