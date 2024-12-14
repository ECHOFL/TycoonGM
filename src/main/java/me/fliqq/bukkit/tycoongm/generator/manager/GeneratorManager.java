package me.fliqq.bukkit.tycoongm.generator.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.fliqq.bukkit.tycoongm.TycoonGM;
import me.fliqq.bukkit.tycoongm.generator.BasicGenerator;
import me.fliqq.bukkit.tycoongm.generator.GeneratorType;
import me.fliqq.bukkit.tycoongm.generator.IGenerator;

public class GeneratorManager {
    private final TycoonGM plugin;
    private final Map<String, GeneratorType> generatorTypes;
    private final PlayerGeneratorManager playerGeneratorManager;
    private final GeneratorLoader generatorLoader;

    public GeneratorManager(TycoonGM plugin) {
        this.plugin=plugin;
        this.generatorTypes = new HashMap<>();
        this.playerGeneratorManager = new PlayerGeneratorManager(this);
        this.generatorLoader = new GeneratorLoader(plugin);
    }
    public PlayerGeneratorManager getPlayerGeneratorManager(){
        return playerGeneratorManager;
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
}
