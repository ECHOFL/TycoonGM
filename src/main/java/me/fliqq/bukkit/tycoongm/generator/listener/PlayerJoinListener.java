package me.fliqq.bukkit.tycoongm.generator.listener;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import lombok.AllArgsConstructor;
import me.fliqq.bukkit.tycoongm.generator.IGenerator;
import me.fliqq.bukkit.tycoongm.generator.manager.GeneratorManager;
import me.fliqq.bukkit.tycoongm.generator.manager.PlayerGeneratorManager;

@AllArgsConstructor
public class PlayerJoinListener implements Listener {
    private final PlayerGeneratorManager playerGeneratorManager;
    private final GeneratorManager generatorManager;
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        //MODIFY TO CHECK IF HE IS IN THE PLAYERDATA FILE
       // if(!player.hasPlayedBefore()){
            playerGeneratorManager.givePlayerStarterGenerators(player.getUniqueId());
            givePlayerStarterGeneratorItems(player);
       // }
    }

    private void givePlayerStarterGeneratorItems(Player player){
        List<IGenerator> generators=playerGeneratorManager.getPlayerGenerators(player.getUniqueId());
        for(IGenerator generator : generators){
            ItemStack item = generatorManager.createGeneratorItem(generator);

            //TODO: ADD CHECK OF INVENTORY
            player.getInventory().addItem(item);
        }
    }
}
