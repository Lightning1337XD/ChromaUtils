package me.lightning.chromautils;

import me.lightning.chromautils.commands.ResetCommand;
import me.lightning.chromautils.commands.TimerCommand;
import me.lightning.chromautils.timer.Timer;
import me.lightning.chromautils.worlds.WorldManager;

import org.bukkit.plugin.java.JavaPlugin;

public final class ChromaUtils extends JavaPlugin {

    private static ChromaUtils instance;
    
    private Timer timer;
    private WorldManager worldManager;

    @Override
    public void onLoad() {
        instance = this;

        worldManager = new WorldManager();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        registerManagers();
        registerCommands();
    }

    @Override
    public void onDisable() {
        reloadConfig();
        
        timer.save();

        saveConfig();
    }

    public static ChromaUtils getInstance() {
        return instance;
    }

    public Timer getTimer() {
        return timer;
    }

    private void registerManagers() {
        timer = new Timer();
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    private void registerCommands() {
        getCommand("reset").setExecutor(new ResetCommand());
        getCommand("timer").setExecutor(new TimerCommand());
    }
}
