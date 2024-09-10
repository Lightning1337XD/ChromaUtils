package me.lightning.chromautils;

import me.lightning.chromautils.commands.TimerCommand;
import me.lightning.chromautils.timer.Timer;

import org.bukkit.plugin.java.JavaPlugin;

public final class ChromaUtils extends JavaPlugin {

    private static ChromaUtils instance;
    
    private Timer timer;

    @Override
    public void onLoad() {
        instance = this;
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

    private void registerCommands() {
        getCommand("timer").setExecutor(new TimerCommand());
    }
}
