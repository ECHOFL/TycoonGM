package me.fliqq.bukkit.tycoongm.generator.listener;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import lombok.AllArgsConstructor;
import me.fliqq.bukkit.tycoongm.generator.manager.PlayerGeneratorManager;

@AllArgsConstructor
public class PlayerQuitListener implements Listener {
    private final PlayerGeneratorManager playerGeneratorManager;
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerGeneratorManager.savePlayerGenerators(event.getPlayer().getUniqueId());
    }
}
