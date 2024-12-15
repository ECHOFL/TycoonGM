package me.fliqq.bukkit.tycoongm.generator.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import me.fliqq.bukkit.tycoongm.generator.BasicGenerator;
import me.fliqq.bukkit.tycoongm.generator.GeneratorType;
import me.fliqq.bukkit.tycoongm.generator.IGenerator;

public class PlayerGeneratorManager {
    private final Map<UUID, List<IGenerator>> playerGenerators = new HashMap<>();
    private final GeneratorManager generatorManager;
    private final PlayerGeneratorDataManager dataManager;

    public PlayerGeneratorManager(GeneratorManager generatorManager, PlayerGeneratorDataManager dataManager) {
        this.generatorManager = generatorManager;
        this.dataManager = dataManager;
    }

    public void loadPlayerGenerators(UUID playerId) {
        List<String> generatorIds = dataManager.getPlayerGenerators(playerId);
        generatorManager.initializePlayerSequence(playerId, generatorIds);
        for (String generatorId : generatorIds) {
            IGenerator generator = recreateGenerator(generatorId, playerId);
            if (generator != null) {
                addGeneratorWithoutSaving(playerId, generator);
            }
        }
    }

    public void givePlayerGenerator(UUID playerId, String generatorType, int tier, int amount) {
        for (int i = 0; i < amount; i++) {
            IGenerator generator = generatorManager.createGenerator(playerId, generatorType, tier, null);
            addGeneratorWithoutSaving(playerId, generator);
        }
    }

    public void addGenerator(UUID playerId, IGenerator generator) {
        addGeneratorWithoutSaving(playerId, generator);
        savePlayerGenerators(playerId);
    }

    public List<IGenerator> getPlayerGenerators(UUID playerId) {
        return playerGenerators.getOrDefault(playerId, Collections.emptyList());
    }

    public void removeGenerator(UUID playerId, IGenerator generator) {
        List<IGenerator> generators = playerGenerators.get(playerId);
        if (generators != null && generators.remove(generator)) {
            if (generators.isEmpty()) {
                playerGenerators.remove(playerId);
            }
            generatorManager.removeGenerator(generator.getGeneratorId());
            savePlayerGenerators(playerId);
        }
    }


    private IGenerator recreateGenerator(String generatorId, UUID ownerId) {
        String[] parts = generatorId.split("-");
        if (parts.length == 4) {
            String typeId = parts[1];
            int tier = Integer.parseInt(parts[3]);
            GeneratorType type = generatorManager.getGeneratorType(typeId);
            if (type != null) {
                return new BasicGenerator(ownerId, type, type.getTiers().get(tier), null);
            }
        }
        return null;
    }
    public void givePlayerStarterGenerators(UUID playerId) {
        List<IGenerator> existingGenerators = getPlayerGenerators(playerId);
        if (existingGenerators.isEmpty()) {
            givePlayerGenerator(playerId, "diamond", 1, 2);
            givePlayerGenerator(playerId, "emerald", 1, 1);
            savePlayerGenerators(playerId);
        }
    }

    private void addGeneratorWithoutSaving(UUID playerId, IGenerator generator) {
        List<IGenerator> generators = playerGenerators.computeIfAbsent(playerId, k -> new ArrayList<>());
        if (!generators.contains(generator)) {
            generators.add(generator);
        }
    }

    public void savePlayerGenerators(UUID playerId) {
        List<String> generatorIds = getPlayerGenerators(playerId).stream()
            .map(IGenerator::getGeneratorId)
            .distinct() // Ensure no duplicates
            .collect(Collectors.toList());
        dataManager.setPlayerGenerators(playerId, generatorIds);
    }
}