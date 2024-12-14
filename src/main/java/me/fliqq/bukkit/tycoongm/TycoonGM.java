package me.fliqq.bukkit.tycoongm;

import org.bukkit.plugin.java.JavaPlugin;

import me.fliqq.bukkit.tycoongm.generator.manager.GeneratorManager;

public class TycoonGM extends JavaPlugin
{

    private GeneratorManager generatorManager;

    @Override
    public void onEnable(){
        saveDefaultConfig();//SAVE JAR CONFIG IF DO NOT EXIST!
        generatorManager=new GeneratorManager(this);
        generatorManager.loadGenerators();


        
        messages();
    }
        
    private void messages() {
        getLogger().info("***********");
        getLogger().info("TycoonGM 1.0 enabled");
        getLogger().info("Plugin by Fliqqq");
        getLogger().info("***********");
    }
}
