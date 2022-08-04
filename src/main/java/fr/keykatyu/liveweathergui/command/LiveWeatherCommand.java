package fr.keykatyu.liveweathergui.command;

import fr.keykatyu.liveweathergui.Main;
import fr.keykatyu.liveweathergui.listener.LWGUIClickListener;
import fr.keykatyu.liveweathergui.util.Config;
import fr.keykatyu.liveweathergui.util.ItemBuilder;
import fr.keykatyu.liveweathergui.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LiveWeatherCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cYou can't use this command if you're not a player !");
            return false;
        }

        Inventory inv = Bukkit.createInventory(null, 9, "§c§lLiveWeather GUI");
        Player player = (Player) sender;

        if(Config.getBoolean("live-weather.is-active")) {
            inv.setItem(3, new ItemBuilder(Material.GREEN_WOOL)
                    .setName("§aActive").setLore("§7LiveWeather system is active on your server !").toItemStack());
        } else {
            inv.setItem(3, new ItemBuilder(Material.RED_WOOL)
                    .setName("§cInactive").setLore("§7LiveWeather system isn't active on your server !").toItemStack());
        }

        inv.setItem(4, new ItemBuilder(Material.FILLED_MAP)
                .setName("§eCity")
                .setLore("§fClick to edit your current city !", "§f(§ckeep chat open !§f)",
                        "", "§7- City : §b" + Config.getString("live-weather.city.name"),
                        "§7- Country : §a" + Config.getString("live-weather.city.country") + " (" + Config.getString("live-weather.city.country_code") + ")",
                        "§7- Coordinates :", "§7x = §e" + Config.getDouble("live-weather.city.coords.x"),
                        "§7y = §e" + Config.getDouble("live-weather.city.coords.y"),
                        "§7- Timezone : §d" + Config.getString("live-weather.city.timezone")).toItemStack());

        inv.setItem(8, new ItemBuilder(Material.GRASS_BLOCK)
                .setName("§aReload")
                .setLore("§fReload the plugin and the LiveWeather system")
                .toItemStack());
        Util.completeInventory(inv);
        player.openInventory(inv);
        Main.getInstance().getServer().getPluginManager().registerEvents(new LWGUIClickListener(), Main.getInstance());
        return false;
    }
}
