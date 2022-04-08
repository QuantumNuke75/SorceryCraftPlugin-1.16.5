package sorcerycraft.events;

import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import sorcerycraft.data.PlayerData;
import sorcerycraft.item.ItemManager;
import sorcerycraft.main.SorceryCraft;

public class PlayerRespawnEvent implements Listener {

    private SorceryCraft sc;

    public PlayerRespawnEvent(SorceryCraft sc) {
        this.sc = sc;
    }

    @EventHandler
    public void onPlayerRespawn(org.bukkit.event.player.PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        PlayerData playerData = sc.playerDataMap.get(player.getUniqueId());

        //Resets player's hotbar depending on player's selected items
        for(int i = 0; i < 9; i++){
            if(!(playerData.hotbarItems[i] == -1)){
                player.getInventory().setItem(i, ItemManager.getItemStackFromID(playerData.hotbarItems[i]));
            }
        }
    }
}

