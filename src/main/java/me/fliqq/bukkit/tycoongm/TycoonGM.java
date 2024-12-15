package me.fliqq.bukkit.tycoongm;

import java.io.File;


import org.bukkit.plugin.java.JavaPlugin;

import me.fliqq.bukkit.tycoongm.generator.listener.GeneratorListener;
import me.fliqq.bukkit.tycoongm.generator.listener.PlayerJoinListener;
import me.fliqq.bukkit.tycoongm.generator.manager.GeneratorManager;

public class TycoonGM extends JavaPlugin
{

    private GeneratorManager generatorManager;

    @Override
    public void onEnable(){
        saveDefaultConfig();//SAVE JAR CONFIG IF DO NOT EXIST!
        copyDefaultResources("generators");


        generatorManager=new GeneratorManager(this);
        generatorManager.loadGenerators();
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(generatorManager.getPlayerGeneratorManager(), generatorManager), this);
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
