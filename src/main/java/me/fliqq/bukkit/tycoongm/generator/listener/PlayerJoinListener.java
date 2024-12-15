package me.fliqq.bukkit.tycoongm.generator.listener;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import lombok.AllArgsConstructor;
import me.fliqq.bukkit.tycoongm.generator.IGenerator;
import me.fliqq.bukkit.tycoongm.generator.manager.GeneratorManager;
import me.fliqq.bukkit.tycoongm.generator.manager.PlayerGeneratorManager;
import me.fliqq.bukkit.tycoongm.generator.manager.PlayerGeneratorDataManager;

@AllArgsConstructor
public class PlayerJoinListener implements Listener {
    private final PlayerGeneratorManager playerGeneratorManager;
    private final GeneratorManager generatorManager;
    private final PlayerGeneratorDataManager dataManager;
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        if (!dataManager.hasPlayerData(playerId)) {
            playerGeneratorManager.givePlayerStarterGenerators(playerId);
            givePlayerStarterGeneratorItems(player);
        } else {
            playerGeneratorManager.loadPlayerGenerators(playerId);
        }
    }
    private void givePlayerStarterGeneratorItems(Player player) {
        List<IGenerator> generators = playerGeneratorManager.getPlayerGenerators(player.getUniqueId());
        for (IGenerator generator : generators) {
            ItemStack item = generatorManager.createGeneratorItem(generator);
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(item);
            } else {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
        }
    }
}
