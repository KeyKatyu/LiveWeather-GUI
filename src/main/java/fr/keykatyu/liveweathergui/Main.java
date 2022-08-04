package fr.keykatyu.liveweathergui;

import fr.keykatyu.liveweathergui.command.LiveWeatherCommand;
import fr.keykatyu.liveweathergui.listener.LWGUIClickListener;
import fr.keykatyu.liveweathergui.task.LiveWeatherTask;
import fr.keykatyu.liveweathergui.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public final class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        Bukkit.getConsoleSender().sendMessage(Config.getString("live-weather.prefix") + " " +
                ChatColor.GREEN + "Enabling LiveWeather GUI v" + this.getDescription().getVersion() + " by " + ChatColor.GOLD + "KeyKatyu");

        getCommand("liveweather").setExecutor(new LiveWeatherCommand());

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new LiveWeatherTask(), 20L, 12000L);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Config.getString("live-weather.prefix") + " " +
                ChatColor.RED + "Disabling LiveWeather GUI v" + this.getDescription().getVersion() + " by " + ChatColor.GOLD + "KeyKatyu");
        this.saveConfig();
    }

    public static Main getInstance() {
        return instance;
    }
}