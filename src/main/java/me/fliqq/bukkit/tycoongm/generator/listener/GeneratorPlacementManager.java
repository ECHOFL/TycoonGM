package me.fliqq.bukkit.tycoongm.generator.listener;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import me.fliqq.bukkit.tycoongm.TycoonGM;
import me.fliqq.bukkit.tycoongm.generator.IGenerator;
import me.fliqq.bukkit.tycoongm.generator.manager.GeneratorManager;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;

public class GeneratorPlacementManager implements Listener {
    private final TycoonGM plugin;
    private final BentoBox bentoBox;
    private final GeneratorManager generatorManager;

    public GeneratorPlacementManager(TycoonGM plugin, BentoBox bentoBox, GeneratorManager generatorManager) {
        this.plugin = plugin;
        this.bentoBox = bentoBox;
        this.generatorManager = generatorManager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Location location = event.getBlock().getLocation();

        if (generatorManager.isGeneratorItem(event.getItemInHand())) {
            if (canPlaceGenerator(player, location)) {
                placeGenerator(player, location, event.getItemInHand());
                event.setCancelled(true); // Cancel the vanilla block placement
            } else {
                player.sendMessage("You can only place generators on your own island!");
                event.setCancelled(true);
            }
        }
    }

    private boolean canPlaceGenerator(Player player, Location location) {
        User user = User.getInstance(player);
        Optional<Island> optionalIsland = bentoBox.getIslands().getIslandAt(location);
        return optionalIsland.isPresent() && optionalIsland.get().onIsland(location) && 
               optionalIsland.get().getMemberSet().contains(user.getUniqueId());
    }

    private void placeGenerator(Player player, Location location, ItemStack item) {
        String generatorId = generatorManager.getGeneratorIdFromItem(item);
        IGenerator generator = generatorManager.createGenerator(player.getUniqueId(), generatorId, location);
        generatorManager.placeGenerator(generator);
        player.sendMessage("Generator placed successfully!");
    }
}
