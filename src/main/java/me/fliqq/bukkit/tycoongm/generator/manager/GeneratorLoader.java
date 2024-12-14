package me.fliqq.bukkit.tycoongm.generator.manager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import me.fliqq.bukkit.tycoongm.TycoonGM;
import me.fliqq.bukkit.tycoongm.generator.GeneratorTier;
import me.fliqq.bukkit.tycoongm.generator.GeneratorType;
import me.fliqq.bukkit.util.ComponentUtil;
import net.kyori.adventure.text.Component;

public class GeneratorLoader {
    private final TycoonGM plugin;

    public GeneratorLoader(TycoonGM plugin) {
        this.plugin = plugin;
    }

    public Map<String, GeneratorType> loadGeneratorTypes() {
        Map<String, GeneratorType> generatorTypes = new HashMap<>();
        File generatorsFolder = new File(plugin.getDataFolder(), "generators");
        if (!generatorsFolder.exists() || !generatorsFolder.isDirectory()) {
            plugin.getLogger().warning("Generators folder not found or is not a directory");
            return generatorTypes;
        }
        for (File file : generatorsFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"))) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            String genId = file.getName().replace(".yml", "");
            GeneratorType generatorType = loadGeneratorType(genId, config);
            generatorTypes.put(genId, generatorType);
        }
        return generatorTypes;
    }

    private GeneratorType loadGeneratorType(String genId, YamlConfiguration config) {
        Component displayName = ComponentUtil.parseComponent(config.getString("displayName"));
        Material item = Material.valueOf(config.getString("item"));
        Material blockType = Material.valueOf(config.getString("blockType"));
        Component itemDisplayName = ComponentUtil.parseComponent(config.getString("itemDisplayName"));
        double sellPrice = config.getDouble("sellPrice");
        List<Component> hologramLines = config.getStringList("hologramLines").stream()
            .map(ComponentUtil::parseComponent)
            .collect(Collectors.toList());
        List<Component> genLore = config.getStringList("generatorLore").stream()
            .map(ComponentUtil::parseComponent)
            .collect(Collectors.toList());
        List<Component> itemLore = config.getStringList("itemLore").stream()
            .map(ComponentUtil::parseComponent)
            .collect(Collectors.toList());
        
        Map<Integer, GeneratorTier> tiers = loadTiers(config.getConfigurationSection("tiers"));
    
        GeneratorType generatorType = new GeneratorType(genId, displayName, item, blockType, 
            itemDisplayName, sellPrice, hologramLines, genLore, itemLore, tiers);
        return generatorType;
    }
    
    private Map<Integer, GeneratorTier> loadTiers(ConfigurationSection tiersSection) {
        Map<Integer, GeneratorTier> tiers = new TreeMap<>();
        if (tiersSection != null) {
            for (String tierKey : tiersSection.getKeys(false)) {
                int tierId = Integer.parseInt(tierKey);
                ConfigurationSection tierSection = tiersSection.getConfigurationSection(tierKey);
                if (tierSection != null) {
                    double price = tierSection.getDouble("price");
                    int productionRate = tierSection.getInt("productionRate");
                    int productionInterval = tierSection.getInt("productionInterval");
                    tiers.put(tierId, new GeneratorTier(tierId, price, productionRate, productionInterval));
                }
            }
        }
        return tiers;
    }
}

