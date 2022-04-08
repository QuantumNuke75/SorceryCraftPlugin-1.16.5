package sorcerycraft.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import sorcerycraft.item.ItemManager;
import sorcerycraft.main.SorceryCraft;

public class ItemDropEvent implements Listener {

    private SorceryCraft sc;

    public ItemDropEvent(SorceryCraft sc) {
        this.sc = sc;
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if(e.getItemDrop().getItemStack().hasItemMeta() && e.getItemDrop().getItemStack().getItemMeta().equals(ItemManager.fireBallItem.getItemMeta())){
            e.setCancelled(true);
        }
    }
}
