package me.fliqq.bukkit.tycoongm.generator.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;

import me.fliqq.bukkit.tycoongm.TycoonGM;
import me.fliqq.bukkit.tycoongm.generator.BasicGenerator;
import me.fliqq.bukkit.tycoongm.generator.GeneratorType;
import me.fliqq.bukkit.tycoongm.generator.IGenerator;

public class GeneratorManager {
    private final Map<String, GeneratorType> generatorTypes;
    private final PlayerGeneratorManager playerGeneratorManager;
    private final GeneratorLoader generatorLoader;

    public GeneratorManager(TycoonGM plugin) {
        this.generatorTypes = new HashMap<>();
        this.playerGeneratorManager = new PlayerGeneratorManager();
        this.generatorLoader = new GeneratorLoader(plugin);
    }

    public void loadGenerators() {
        this.generatorTypes.putAll(generatorLoader.loadGeneratorTypes());
    }

    public GeneratorType getGeneratorType(String typeId) {
        return generatorTypes.get(typeId);
    }

    public IGenerator createGenerator(UUID ownerId, String typeId, int tier, Location location) {
        GeneratorType type = getGeneratorType(typeId);
        if (type == null) {
            throw new IllegalArgumentException("Invalid generator type: " + typeId);
        }
        IGenerator generator = new BasicGenerator(ownerId, type, type.getTiers().get(tier), location);
        playerGeneratorManager.addGenerator(ownerId, generator);
        return generator;
    }
}
