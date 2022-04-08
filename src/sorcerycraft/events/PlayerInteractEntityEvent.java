package sorcerycraft.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import sorcerycraft.data.PlayerData;
import sorcerycraft.main.SorceryCraft;


public class PlayerInteractEntityEvent implements Listener {

    private SorceryCraft sc;

    public PlayerInteractEntityEvent(SorceryCraft sc) {
        this.sc = sc;
    }

    @EventHandler
    public void onPlayerEntityInteract(org.bukkit.event.player.PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        PlayerData playerData = sc.playerDataMap.get(player.getUniqueId());
    }
}
