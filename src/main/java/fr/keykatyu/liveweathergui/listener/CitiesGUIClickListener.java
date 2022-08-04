package fr.keykatyu.liveweathergui.listener;

import de.tr7zw.nbtapi.NBTItem;
import fr.keykatyu.liveweathergui.Main;
import fr.keykatyu.liveweathergui.task.LiveWeatherTask;
import fr.keykatyu.liveweathergui.util.Config;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class CitiesGUIClickListener implements Listener {

    private String guiName;
    public CitiesGUIClickListener(String s) {
        this.guiName = s;
    }

    @EventHandler
    public void onCitiesGUIClickEvent(InventoryClickEvent e) {
        if(!e.getView().getTitle().equals(guiName)) {
            return;
        }

        if(e.getCurrentItem() == null) {
            return;
        }

        e.setCancelled(true);

        if(e.getCurrentItem().getType().equals(Material.GRAY_STAINED_GLASS_PANE)) {
            return;
        }

        NBTItem nbti = new NBTItem(e.getCurrentItem());
        Config.setString("live-weather.city.name", nbti.getString("city"));
        Config.setString("live-weather.city.country", nbti.getString("country"));
        Config.setString("live-weather.city.country_code", nbti.getString("country_code"));
        Config.setDouble("live-weather.city.coords.x", nbti.getDouble("x"));
        Config.setDouble("live-weather.city.coords.y", nbti.getDouble("y"));
        Config.setString("live-weather.city.timezone", nbti.getString("timezone"));

        Player player = (Player) e.getWhoClicked();
        player.closeInventory();
        player.sendMessage(Config.getString("live-weather.prefix") +" §aYou have correctly selected §6§l" +
                nbti.getString("city") + " §ain " + "§6§l" + nbti.getString("country") + " §afor the LiveWeather system !");
        new LiveWeatherTask().run();
        HandlerList.unregisterAll(this);
    }
}
