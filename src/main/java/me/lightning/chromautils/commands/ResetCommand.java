package me.lightning.chromautils.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import me.lightning.chromautils.ChromaUtils;

public class ResetCommand implements TabExecutor {

    private final Random random = new Random();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 1) {
            return false;
        }

        Long seed;

        if (args.length == 1) {
            try {
                seed = Long.valueOf(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage("Invalid seed!");
                return true;
            }
        } else {
            seed = random.nextLong();
        }
        
        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer("Resetting world..."));

        List<String> worlds = new ArrayList<>();

        for (World world : Bukkit.getWorlds()) {
            ChromaUtils.getInstance().getWorldManager().createWorld(world.getName(), world.getEnvironment(), seed);
            worlds.add(world.getName());
        }

        ChromaUtils.getInstance().getConfig().set("worlds.resetWorlds", worlds);
        ChromaUtils.getInstance().saveConfig();

        Bukkit.spigot().restart();

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }
}
