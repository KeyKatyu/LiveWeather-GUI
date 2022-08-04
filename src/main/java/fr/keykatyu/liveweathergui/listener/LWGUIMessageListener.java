package fr.keykatyu.liveweathergui.listener;

import com.google.gson.JsonObject;
import de.tr7zw.nbtapi.NBTEntity;
import de.tr7zw.nbtapi.NBTItem;
import fr.keykatyu.liveweathergui.Main;
import fr.keykatyu.liveweathergui.util.Config;
import fr.keykatyu.liveweathergui.util.ItemBuilder;
import fr.keykatyu.liveweathergui.util.Util;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class LWGUIMessageListener implements Listener {

    private final String playerName;

    public LWGUIMessageListener(String pName) {
        playerName = pName;
    }

    @EventHandler
    public void onLWGUIMessageSend(AsyncPlayerChatEvent e) {
        if(!e.getPlayer().getName().equals(getPlayerName())) {
            return;
        }

        e.setCancelled(true);
        String city = e.getMessage();
        Player player = e.getPlayer();

        player.sendMessage(Config.getString("live-weather.prefix") + " §cYou chose §6§l" + city + "§c city. We're founding your city in the cities of the world, please §lWAIT§c " +
                "and don't press §lESCAPE key§c.");

        try {
            JSONObject response = new JSONObject(IOUtils.toString(new URL("https://geocoding-api.open-meteo.com/v1/search?name=" + city + "&count=50"), StandardCharsets.UTF_8));

            try {
                JSONArray results = response.getJSONArray("results");

                Inventory inv;
                String guiName;
                if(results.length() >= 2) {
                    guiName = "§c§l" + results.length() + " §ccities found";
                    inv = Bukkit.createInventory(null, 54, guiName);
                } else {
                    guiName = "§c§l" + results.length() + " §ccity found";
                    inv = Bukkit.createInventory(null, 54, guiName);
                }

                for(int a = 0; a < results.length(); a++) {
                    JSONObject jsonCity = results.getJSONObject(a);
                    String country = Util.getCountry(jsonCity);
                    ItemStack im = new ItemBuilder(Material.FLOWER_POT).setName("§b" + jsonCity.getString("name"))
                            .setLore("§fClick to select this city !", "",
                                    "§7- Country : §a" + country + " (" + jsonCity.getString("country_code") + ")",
                                    "§7- Coordinates :", "§7x = §e" + jsonCity.getDouble("latitude"),
                                    "§7y = §e" + jsonCity.getDouble("longitude"),
                                    "§7- Timezone : §d" + jsonCity.getString("timezone")).toItemStack();
                    NBTItem nbti = new NBTItem(im);
                    nbti.setString("city", jsonCity.getString("name"));
                    nbti.setString("country", country);
                    nbti.setString("country_code", jsonCity.getString("country_code"));
                    nbti.setDouble("x", jsonCity.getDouble("latitude"));
                    nbti.setDouble("y", jsonCity.getDouble("longitude"));
                    nbti.setString("timezone", jsonCity.getString("timezone"));
                    inv.setItem(a, nbti.getItem());
                }

                Util.completeInventory(inv);
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    player.openInventory(inv);
                    Main.getInstance().getServer().getPluginManager().registerEvents(new CitiesGUIClickListener(guiName), Main.getInstance());
                });
            } catch (JSONException exception) {
                player.sendMessage("§cYour city doesn't exist or you made a mistake ! If it's correct, there was a problem during the API" +
                        " processing, please contact the developer.");
                System.out.println(exception.getLocalizedMessage());
            }
        } catch (IOException ex) {
            player.sendMessage("§cSomething went wrong during the request, please try again...");
        }

        HandlerList.unregisterAll(this);
    }

    public String getPlayerName() {
        return playerName;
    }
}
