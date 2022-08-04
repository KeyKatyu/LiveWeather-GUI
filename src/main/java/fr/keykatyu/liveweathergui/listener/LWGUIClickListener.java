package fr.keykatyu.liveweathergui.listener;

import fr.keykatyu.liveweathergui.Main;
import fr.keykatyu.liveweathergui.task.LiveWeatherTask;
import fr.keykatyu.liveweathergui.util.Config;
import fr.keykatyu.liveweathergui.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LWGUIClickListener implements Listener {

    @EventHandler
    public void onLWGUIItemClickEvent(InventoryClickEvent e) {
        if(!e.getView().getTitle().equals("§c§lLiveWeather GUI")) {
            return;
        }

        if(e.getCurrentItem() == null) {
            return;
        }

        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();

        switch(e.getCurrentItem().getType())
        {
            case GREEN_WOOL:
                Config.setBoolean("live-weather.is-active", false);
                e.getClickedInventory().setItem(3, new ItemBuilder(Material.RED_WOOL)
                        .setName("§cInactive").setLore("§7LiveWeather system isn't active on your server !").toItemStack());
                break;
            case RED_WOOL:
                Config.setBoolean("live-weather.is-active", true);
                e.getClickedInventory().setItem(3, new ItemBuilder(Material.GREEN_WOOL)
                        .setName("§aActive").setLore("§7LiveWeather system is active on your server !").toItemStack());
                break;
            case FILLED_MAP:
                player.closeInventory();
                player.sendMessage(Config.getString("live-weather.prefix") + " §cPlease send us, in the chat, the name of your city !");
                Main.getInstance().getServer().getPluginManager().registerEvents(new LWGUIMessageListener(player.getName()), Main.getInstance());
                break;
            case GRASS_BLOCK:
                player.closeInventory();
                Main.getInstance().saveDefaultConfig();
                Main.getInstance().saveConfig();
                Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new LiveWeatherTask());
                player.sendMessage(Config.getString("live-weather.prefix") + " Plugin & LiveWeather system reloaded !");
            default:
                break;
        }
    }

    @EventHandler
    public void onLWGUICloseEvent(InventoryCloseEvent e) {
        if(!e.getView().getTitle().equals("§c§lLiveWeather GUI")) {
            return;
        }

        HandlerList.unregisterAll(this);
    }
}
