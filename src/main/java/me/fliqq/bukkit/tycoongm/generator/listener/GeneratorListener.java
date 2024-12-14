package me.fliqq.bukkit.tycoongm.generator.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.fliqq.bukkit.tycoongm.generator.manager.GeneratorManager;

public class GeneratorListener implements Listener {
    private final GeneratorManager generatorManager;

    public GeneratorListener(GeneratorManager generatorManager) {
        this.generatorManager = generatorManager;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (generatorManager.isGeneratorItem(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
        }
    }
}

