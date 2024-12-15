package me.fliqq.bukkit.tycoongm.generator.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.fliqq.bukkit.tycoongm.TycoonGM;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerGeneratorDataManager {
    private final TycoonGM plugin;
    private final File dataFile;
    private FileConfiguration dataConfig;

    public PlayerGeneratorDataManager(TycoonGM plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "players_generators.yml");
        loadData();
    }

    private void loadData() {
        if (!dataFile.exists()) {
            plugin.saveResource("players_generators.yml", false);
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void saveData() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save data to " + dataFile);
            e.printStackTrace();
        }
    }

    public void setPlayerGenerators(UUID playerId, List<String> generatorIds) {
        // Remove duplicates before saving
        List<String> uniqueGeneratorIds = new ArrayList<>(new LinkedHashSet<>(generatorIds));
        dataConfig.set(playerId.toString(), uniqueGeneratorIds);
        saveData();
    }

    public void addGeneratorToPlayer(UUID playerId, String generatorId) {
        List<String> generatorIds = getPlayerGenerators(playerId);
        if (!generatorIds.contains(generatorId)) {
            generatorIds.add(generatorId);
            setPlayerGenerators(playerId, generatorIds);
        }
    }

    public List<String> getPlayerGenerators(UUID playerId) {
        return dataConfig.getStringList(playerId.toString());
    }

    public void removeGeneratorFromPlayer(UUID playerId, String generatorId) {
        List<String> generators = getPlayerGenerators(playerId);
        generators.remove(generatorId);
        setPlayerGenerators(playerId, generators);
    }

    public Map<UUID, List<String>> getAllPlayerGenerators() {
        Map<UUID, List<String>> allGenerators = new HashMap<>();
        for (String key : dataConfig.getKeys(false)) {
            UUID playerId = UUID.fromString(key);
            List<String> generators = dataConfig.getStringList(key);
            allGenerators.put(playerId, generators);
        }
        return allGenerators;
    }
    public boolean hasPlayerData(UUID playerId) {
        return dataConfig.contains(playerId.toString());
    }
}

