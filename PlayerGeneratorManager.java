public class PlayerGeneratorManager {
    // ... existing fields ...

    public void loadPlayerGenerators(UUID playerId) {
        List<String> generatorIds = dataManager.getPlayerGenerators(playerId);
        for (String generatorId : generatorIds) {
            IGenerator generator = generatorManager.getGeneratorFromId(generatorId);
            if (generator != null) {
                addGenerator(playerId, generator);
            }
        }
    }

    public void savePlayerGenerators(UUID playerId) {
        List<String> generatorIds = getPlayerGenerators(playerId).stream()
            .map(IGenerator::getGeneratorId)
            .collect(Collectors.toList());
        dataManager.setPlayerGenerators(playerId, generatorIds);
    }

    // ... existing methods ...

    public void addGenerator(UUID playerId, IGenerator generator) {
        playerGenerators.computeIfAbsent(playerId, k -> new ArrayList<>()).add(generator);
        savePlayerGenerators(playerId);
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
}
