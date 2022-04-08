package sorcerycraft.inventory;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import sorcerycraft.item.ItemManager;
import sorcerycraft.main.SorceryCraft;

public class GuildSelectionInventory {

    public static String name = "" + ChatColor.WHITE + "Guild Selection Menu";

    private SorceryCraft sc = SorceryCraft.getPlugin(SorceryCraft.class);

    public void createInventory(Player player) {
        Inventory i = sc.getServer().createInventory(null, 9, name);

        i.setItem(0, ItemManager.emptyItem);
        i.setItem(1, ItemManager.fireItem);
        i.setItem(2, ItemManager.emptyItem);
        i.setItem(3, ItemManager.waterItem);
        i.setItem(4, ItemManager.emptyItem);
        i.setItem(5, ItemManager.airItem);
        i.setItem(6, ItemManager.emptyItem);
        i.setItem(7, ItemManager.earthItem);
        i.setItem(8, ItemManager.emptyItem);

        player.openInventory(i);
    }

}
