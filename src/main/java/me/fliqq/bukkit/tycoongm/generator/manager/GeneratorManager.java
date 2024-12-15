package me.fliqq.bukkit.tycoongm.generator.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.fliqq.bukkit.tycoongm.TycoonGM;
import me.fliqq.bukkit.tycoongm.generator.AbstractGenerator;
import me.fliqq.bukkit.tycoongm.generator.BasicGenerator;
import me.fliqq.bukkit.tycoongm.generator.GeneratorType;
import me.fliqq.bukkit.tycoongm.generator.IGenerator;
import world.bentobox.bentobox.BentoBox;

public class GeneratorManager {
    private final TycoonGM plugin;
    private final Map<String, GeneratorType> generatorTypes;
    private final PlayerGeneratorManager playerGeneratorManager;
    private final GeneratorLoader generatorLoader;
    private final Map<String, IGenerator> allGenerators;
    private final BentoBox bentoBox;

    public GeneratorManager(TycoonGM plugin, PlayerGeneratorDataManager playerGeneratorDataManager, BentoBox bentoBox) {
        this.plugin=plugin;
        this.bentoBox=bentoBox;
        this.generatorTypes = new HashMap<>();
        this.allGenerators=new HashMap<>();
        this.playerGeneratorManager = new PlayerGeneratorManager(this, playerGeneratorDataManager);
        this.generatorLoader = new GeneratorLoader(plugin);
    }
    public PlayerGeneratorManager getPlayerGeneratorManager(){
        return playerGeneratorManager;
    }

    public void initializePlayerSequence(UUID playerId, List<String> generatorIds) {
    long maxId = generatorIds.stream()
        .map(id -> id.split("-"))
        .filter(parts -> parts.length == 5)
        .mapToLong(parts -> Long.parseLong(parts[4], 16))
        .max()
        .orElse(0L);
    
    AbstractGenerator.initializePlayerSequence(playerId, maxId);
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
        allGenerators.put(generator.getGeneratorId(), generator);
        return generator;
    }


    public ItemStack createGeneratorItem(IGenerator generator){
        ItemStack item = new ItemStack(generator.getGeneratorType().getBlockType());
        ItemMeta meta = item.getItemMeta();


        meta.displayName(generator.getGeneratorType().getGenDisplayName());
        meta.lore(generator.getGeneratorType().getGenLore());
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        NamespacedKey key = new NamespacedKey(plugin, "generator-id");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, generator.getGeneratorId());
        item.setItemMeta(meta);
        return item;
    }

    public boolean isGeneratorItem(ItemStack item){
        if(item==null || !item.hasItemMeta()) return false;
        NamespacedKey key = new NamespacedKey(plugin, "generator-id");
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }
    public IGenerator getGeneratorFromId(String generatorId) {
        return allGenerators.get(generatorId);
    }
    public void removeGenerator(String generatorId){
        allGenerators.remove(generatorId);
    }

    public String getGeneratorIdFromItem(ItemStack item) {
        if (!isGeneratorItem(item)) return null;
        NamespacedKey key = new NamespacedKey(plugin, "generator-type");
        return item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    public IGenerator createGenerator(UUID ownerId, String generatorType, Location location) {
        GeneratorType type = getGeneratorType(generatorType);
        if (type == null) {
            throw new IllegalArgumentException("Invalid generator type: " + generatorType);
        }
        return new BasicGenerator(ownerId, type, type.getTiers().get(1), location);
    }

    public void placeGenerator(IGenerator generator) {
        // Add the generator to the player's generators
        playerGeneratorManager.addGenerator(generator.getOwnerId(), generator);
        // Place the generator block in the world
        generator.getLocation().getBlock().setType(generator.getGeneratorType().getBlockType());
        // You might want to add more logic here, like setting up holograms
    }

}
