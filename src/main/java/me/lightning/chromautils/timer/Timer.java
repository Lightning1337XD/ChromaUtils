package me.lightning.chromautils.timer;

import java.time.Duration;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.lightning.chromautils.ChromaUtils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Timer {

    private boolean running;
    private boolean countdown;
    private final String format;
    private final String idleMessage;
    private Duration time;

    public Timer() {
        
        FileConfiguration config = ChromaUtils.getInstance().getConfig();
        
        this.running = false;
        this.countdown = config.getBoolean("timer.isCountdown", false);
        this.idleMessage = config.getString("timer.idleMessage", "idle");
        this.format = config.getString("timer.format", "[d'd 'h'h ']m'm ' s's'");
        this.time = Duration.ofSeconds(config.getLong("timer.time", 0));
        
        start();
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isCountdown() {
        return countdown;
    }

    public void setCountdown(boolean countdown) {
        this.countdown = countdown;
    }

    public long getTime() {
        return time.toSeconds();
    }

    public void setTime(long time) {
        this.time = Duration.ofSeconds(time);
    }

    public void save() {

        FileConfiguration config = ChromaUtils.getInstance().getConfig();

        config.set("timer.time", time.toSeconds());
        config.set("timer.isCountdown", countdown);

        ChromaUtils.getInstance().saveConfig();
    }

    private void displayTimer() {

        String text = "";
        
        if (isRunning()) {
            text = DurationFormatUtils.formatDuration(time.toMillis(), format);
        } else {
            text = idleMessage;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.LIGHT_PURPLE + text));
        }
    }

    public void start() {

        new BukkitRunnable() {

            @Override
            public void run() {

                displayTimer();

                if (!isRunning()) {
                    return;
                }

                if (getTime() <= 0 && isCountdown()) {
                    setRunning(false);
                    setCountdown(false);
                    return;
                }

                if (isCountdown()) {
                    setTime(getTime() - 1);
                } else {
                    setTime(getTime() + 1);
                }
            }

        }.runTaskTimerAsynchronously(ChromaUtils.getInstance(), 20, 20);
    }
}
