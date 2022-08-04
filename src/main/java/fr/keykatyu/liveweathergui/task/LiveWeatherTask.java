package fr.keykatyu.liveweathergui.task;

import fr.keykatyu.liveweathergui.Main;
import fr.keykatyu.liveweathergui.util.Config;
import fr.keykatyu.liveweathergui.util.Util;
import fr.keykatyu.liveweathergui.util.WeatherCodes;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

public class LiveWeatherTask implements Runnable {

    @Override
    public void run() {
        if(!Config.getBoolean("live-weather.is-active")) {
            return;
        }

        if(Config.getDouble("live-weather.city.coords.x") == 0.0 && Config.getDouble("live-weather.city.coords.y") == 0.0) {
            Main.getInstance().getServer().broadcastMessage(Config.getString("live-weather.prefix") + " §cHi ! You have actived the LiveWeather" +
                    "system but actually there's no city configured. If you want to setup a city or desactive the system, just run §l/liveweather§c.");
            return;
        }

        World mainWorld = Bukkit.getWorld(Config.getString("settings.world-name"));
        mainWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);

        try {
            JSONObject response = new JSONObject(IOUtils.toString(
                    new URL("https://api.open-meteo.com/v1/forecast?latitude=" + Config.getDouble("live-weather.city.coords.x")
                            + "&longitude=" + Config.getDouble("live-weather.city.coords.y") +
                            "&current_weather=true&timezone=" + Config.getString("live-weather.city.timezone")),
                    StandardCharsets.UTF_8));

            JSONObject currentWeather = response.getJSONObject("current_weather");
            double weatherCode = currentWeather.getDouble("weathercode");
            String time = currentWeather.getString("time");
            int weatherIntCode = (int) weatherCode;

            WeatherCodes weather = WeatherCodes.fromInt(weatherIntCode);
            if(weather == WeatherCodes.CLEAR) {
                mainWorld.setClearWeatherDuration(6000);
                Bukkit.getConsoleSender().sendMessage(Config.getString("live-weather.prefix") + ChatColor.GREEN + " Weather changed to CLEAR.");
            } else if(weather == WeatherCodes.RAIN) {
                mainWorld.setWeatherDuration(6000);
                Bukkit.getConsoleSender().sendMessage(Config.getString("live-weather.prefix") + ChatColor.GREEN + " Weather changed to RAIN.");
            } else if(weather == WeatherCodes.THUNDER) {
                mainWorld.setThundering(true);
                Bukkit.getConsoleSender().sendMessage(Config.getString("live-weather.prefix") + ChatColor.GREEN + " Weather changed to THUNDER.");
            } else if(weather == WeatherCodes.NO_MC_WEATHER_EXIST){
                System.out.println(Config.getString("live-weather.prefix") + ChatColor.RED + " There is no weather-compatibility between your city and Minecraft game, so the weather has not been changed.");
            }

            TemporalAccessor ta = DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(time);
            LocalDateTime localDateTime = LocalDateTime.from(ta);
            Util.setMCTime(localDateTime.getHour(), mainWorld);
        } catch (IOException e) {
            Bukkit.getServer().broadcastMessage(Config.getString("live-weather.prefix") + " §cThere was a problem in the LiveWeather system," +
                    " please check console and contact the developer.");
            System.out.println(e.getLocalizedMessage());
        }
    }
}
