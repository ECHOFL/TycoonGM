package me.fliqq.bukkit.tycoongm.generator.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.fliqq.bukkit.tycoongm.generator.IGenerator;

public class PlayerGeneratorManager {
    private final Map<UUID, List<IGenerator>> playerGenerators = new HashMap<>();

    public void addGenerator(UUID playerId, IGenerator generator) {
        playerGenerators.computeIfAbsent(playerId, k -> new ArrayList<>()).add(generator);
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
        }
    }
}
