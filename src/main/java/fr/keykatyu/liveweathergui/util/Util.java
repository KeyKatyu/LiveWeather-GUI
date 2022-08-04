package fr.keykatyu.liveweathergui.util;

import fr.keykatyu.liveweathergui.Main;
import fr.keykatyu.liveweathergui.listener.CitiesGUIClickListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONObject;

public class Util {

    public static void completeInventory(Inventory inventory) {
        for(int i = 0; i < inventory.getSize(); i++) {
            if(inventory.getItem(i) == null) {
                inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                        .setName("§8-*-*-").toItemStack());
            }
        }
    }

    public static String getCountry(JSONObject jsonObject) {
        if(!jsonObject.has("country")) {
            if(!jsonObject.has("admin1")) {
                return "Country or region not found";
            } else {
                return jsonObject.getString("admin1");
            }
        } else {
            return jsonObject.getString("country");
        }
    }

    public static void setMCTime(int hour, World world) {
        long h = RealHoursToMCHours.valueOf("H_" + hour).getTime();
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            world.setFullTime(h);
        });
        Bukkit.getConsoleSender().sendMessage(Config.getString("live-weather.prefix") + ChatColor.DARK_GREEN + " Time set to " +h + ".");
    }
}
