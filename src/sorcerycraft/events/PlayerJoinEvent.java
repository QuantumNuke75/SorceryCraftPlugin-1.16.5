package sorcerycraft.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import sorcerycraft.data.PlayerData;
import sorcerycraft.main.SorceryCraft;

import java.sql.SQLException;

public class PlayerJoinEvent implements Listener {

    private SorceryCraft sc;

    public PlayerJoinEvent(SorceryCraft sc) {
        this.sc = sc;
    }

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        PlayerData playerData = new PlayerData(player, sc);

        try {
            playerData.firstTimeSetup();
            playerData.loadDataFromSQL();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        sc.playerDataMap.put(player.getUniqueId(), playerData);
    }
}
