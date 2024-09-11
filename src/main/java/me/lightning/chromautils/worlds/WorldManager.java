package me.lightning.chromautils.worlds;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;

import me.lightning.chromautils.ChromaUtils;

import org.bukkit.WorldCreator;

public class WorldManager {

    private final Logger log = Bukkit.getLogger();
    private final String newWorldPrefix;

    public WorldManager() {

        FileConfiguration config = ChromaUtils.getInstance().getConfig();

        this.newWorldPrefix = config.getString("worlds.newWorldPrefix", "new_");

        loadWorlds();
    }

    private void loadWorlds() {

        FileConfiguration config = ChromaUtils.getInstance().getConfig();

        List<String> resetWorlds = config.getStringList("worlds.resetWorlds");

        if (resetWorlds.isEmpty()) {
            return;
        }

        for (String world : resetWorlds) {

            if (!deleteWorld(world)) {
                continue;
            }

            File worldFolder = new File(Bukkit.getWorldContainer(), newWorldPrefix + world);

            if (worldFolder.isDirectory()) {
                worldFolder.renameTo(new File(Bukkit.getWorldContainer(), world));
            } else {
                log.warning("Could not find folder: " + worldFolder.getName());
            }
        }

        config.set("worlds.resetWorlds", new ArrayList<>());

        if (config.getBoolean("worlds.resetTimer", true)) {
            config.set("timer.time", 0);
        }

        ChromaUtils.getInstance().saveConfig();
    }

    public boolean deleteWorld(String worldName) {

        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);

        if (!worldName.isBlank()) {
            try {
                FileUtils.deleteDirectory(worldFolder);
                log.info("Deleted world: " + worldName);
                return true;

            } catch (Exception e) {
                log.warning("Failed to delete world: " + worldName);
                e.printStackTrace();
            }
        }

        return false;
    }

    public void createWorld(String worldName, Environment environment, Long seed) {

        WorldCreator worldCreator = new WorldCreator(newWorldPrefix + worldName);

        worldCreator.seed(seed);
        worldCreator.environment(environment);
        worldCreator.createWorld();

        if (!worldName.equals("world")) {
            return;
        }

        File worldFolder = new File(Bukkit.getWorldContainer(), newWorldPrefix + worldName);
        File playerData = new File(worldFolder, "playerdata");

        playerData.mkdirs();
    }
}
