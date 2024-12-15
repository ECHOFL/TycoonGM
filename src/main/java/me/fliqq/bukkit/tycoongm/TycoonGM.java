package me.fliqq.bukkit.tycoongm;

import java.io.File;


import org.bukkit.plugin.java.JavaPlugin;

import me.fliqq.bukkit.tycoongm.generator.listener.GeneratorListener;
import me.fliqq.bukkit.tycoongm.generator.listener.PlayerJoinListener;
import me.fliqq.bukkit.tycoongm.generator.listener.PlayerQuitListener;
import me.fliqq.bukkit.tycoongm.generator.manager.GeneratorManager;
import me.fliqq.bukkit.tycoongm.generator.manager.PlayerGeneratorDataManager;

public class TycoonGM extends JavaPlugin
{

    private GeneratorManager generatorManager;
    private PlayerGeneratorDataManager playerGeneratorDataManager;

    @Override
    public void onEnable(){
        saveDefaultConfig();//SAVE JAR CONFIG IF DO NOT EXIST!
        copyDefaultResources("generators");
 

        playerGeneratorDataManager=new PlayerGeneratorDataManager(this);
        generatorManager=new GeneratorManager(this, playerGeneratorDataManager);
        generatorManager.loadGenerators();
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(generatorManager.getPlayerGeneratorManager(), generatorManager, playerGeneratorDataManager), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(generatorManager.getPlayerGeneratorManager()), this);
        getServer().getPluginManager().registerEvents(new GeneratorListener(generatorManager), this);
        
        messages();
    }
        
    private void messages() {
        getLogger().info("***********");
        getLogger().info("TycoonGM 1.0 enabled");
        getLogger().info("Plugin by Fliqqq");
        getLogger().info("***********");
    }

    private void copyDefaultResources(String folderName) {
        File folder = new File(getDataFolder(), folderName);
        if (!folder.exists()) {
            folder.mkdirs(); 
        }
    
        String[] generatorFiles = {
            "diamond.yml",
            "emerald.yml"
        };
    
        for (String fileName : generatorFiles) {
            File targetFile = new File(folder, fileName);
            if (!targetFile.exists()) {
                saveResource(folderName + "/" + fileName, false);
            }
        }
    }
}
