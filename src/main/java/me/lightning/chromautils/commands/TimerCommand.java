package me.lightning.chromautils.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import me.lightning.chromautils.ChromaUtils;
import me.lightning.chromautils.timer.Timer;
import net.md_5.bungee.api.ChatColor;

public class TimerCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 1 || args.length > 2) {
            return false;
        }

        Timer timer = ChromaUtils.getInstance().getTimer();

        switch (args[0]) {
            case "start":

                if (timer.isRunning()) {
                    sender.sendMessage("Timer is already running!");
                    return true;
                }

                if (args.length == 2) {
                    try {
                        int time = Integer.parseInt(args[1]);
                        timer.setTime(time);
                        timer.setCountdown(true);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "Invalid number!");
                        return true;
                    }
                }

                timer.setRunning(true);
                break;

            case "toggle":

                timer.setRunning(!timer.isRunning());
                break;

            case "reset":

                timer.setRunning(false);
                timer.setCountdown(false);
                timer.setTime(0);
                break;

            case "set":

                if (args.length < 2) {
                    return false;
                }

                try {
                    int time = Integer.parseInt(args[1]);
                    timer.setTime(time);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid number!");
                    return true;
                }

                break;

            default:
                return false;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> states = new ArrayList<>(List.of("start", "toggle", "reset", "set"));
        List<String> results = new ArrayList<>();

        if (args.length != 1) {
            return new ArrayList<>();
        }

        for (String state : states) {
            if (state.toLowerCase().startsWith(args[0].toLowerCase())) {
                results.add(state);
            }
        }

        return results;
    }

}
