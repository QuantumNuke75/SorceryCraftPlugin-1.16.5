package sorcerycraft.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sorcerycraft.data.PlayerData;
import sorcerycraft.main.SorceryCraft;

import java.sql.SQLException;

public class PlayerLeaveEvent implements Listener {

    private SorceryCraft sc;

    public PlayerLeaveEvent(SorceryCraft sc) {
        this.sc = sc;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if(sc.playerDataMap.get(player.getUniqueId()) != null){

            //Get the player data from player data map
            PlayerData playerData = sc.playerDataMap.get(player.getUniqueId());

            //Save data to SQL
            playerData.saveDataToSQL();

            //Remove player from player data map
            sc.playerDataMap.remove(player.getUniqueId());
        }
    }
}
