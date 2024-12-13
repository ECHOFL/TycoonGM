package me.fliqq.tycoongm;

import org.bukkit.plugin.java.JavaPlugin;

public class TycoonGM extends JavaPlugin
{
    @Override
    public void onEnable(){
        messages();
    }
        
    private void messages() {
        getLogger().info("***********");
        getLogger().info("TycoonGM 1.0 enabled");
        getLogger().info("Plugin by Fliqqq");
        getLogger().info("***********");
    }
}
