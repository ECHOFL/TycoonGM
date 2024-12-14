public class GeneratorManager {
    // ... existing fields and methods ...

    public IGenerator createGenerator(UUID ownerId, String typeId, int tier, Location location) {
        GeneratorType type = getGeneratorType(typeId);
        if (type == null) {
            throw new IllegalArgumentException("Invalid generator type: " + typeId);
        }
        IGenerator generator = new BasicGenerator(ownerId, type, type.getTiers().get(tier), location);
        return generator;
    }

    public ItemStack createGeneratorItem(IGenerator generator) {
        ItemStack item = new ItemStack(generator.getGeneratorType().getBlockType());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(generator.getGeneratorType().getDisplayName());
        meta.setLore(generator.getGeneratorType().getGenLore());
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        
        // Add NBT data to make the item unique and undropable
        NamespacedKey key = new NamespacedKey(plugin, "generator-id");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, generator.getGeneratorId());
        
        item.setItemMeta(meta);
        return item;
    }

    public boolean isGeneratorItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        NamespacedKey key = new NamespacedKey(plugin, "generator-id");
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }
}
