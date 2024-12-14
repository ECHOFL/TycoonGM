public class GeneratorManager {
    private final Map<UUID, Map<String, AbstractGenerator>> playerGenerators;

    public GeneratorManager() {
        this.playerGenerators = new HashMap<>();
    }

    public AbstractGenerator createGenerator(String generatorId, UUID ownerId, Location location, int tier) {
        AbstractGenerator generator = new ConcreteGenerator(generatorId, ownerId, location, tier);
        playerGenerators
            .computeIfAbsent(ownerId, k -> new HashMap<>())
            .put(generatorId, generator);
        return generator;
    }

    public void removeGenerator(UUID ownerId, String generatorId) {
        Map<String, AbstractGenerator> ownersGenerators = playerGenerators.get(ownerId);
        if (ownersGenerators != null) {
            ownersGenerators.remove(generatorId);
            if (ownersGenerators.isEmpty()) {
                playerGenerators.remove(ownerId);
            }
        }
    }

    public AbstractGenerator getGenerator(UUID ownerId, String generatorId) {
        Map<String, AbstractGenerator> ownersGenerators = playerGenerators.get(ownerId);
        return ownersGenerators != null ? ownersGenerators.get(generatorId) : null;
    }

    public List<AbstractGenerator> getPlayerGenerators(UUID ownerId) {
        Map<String, AbstractGenerator> ownersGenerators = playerGenerators.get(ownerId);
        return ownersGenerators != null ? new ArrayList<>(ownersGenerators.values()) : new ArrayList<>();
    }

    public void upgradeGenerator(UUID ownerId, String generatorId) {
        AbstractGenerator generator = getGenerator(ownerId, generatorId);
        if (generator != null) {
            generator.upgrade();
        }
    }

    public void processAllGenerators() {
        for (Map<String, AbstractGenerator> ownersGenerators : playerGenerators.values()) {
            for (AbstractGenerator generator : ownersGenerators.values()) {
                double production = generator.produce();
                // TODO: Add produced resources to player's inventory or balance
            }
        }
    }

    // TODO: Implement save and load methods
}

