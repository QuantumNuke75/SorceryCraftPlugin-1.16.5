package sorcerycraft.main;

import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLManager {

    /**
     * The current instance of the running plugin.
     */
    private SorceryCraft sc = SorceryCraft.getPlugin(SorceryCraft.class);

    public Connection connection;

    private final int DATA_SYNC_TIME = 6000;
    private final int CONNECTION_CHECK_TIME = 20;

    public SQLManager(){}

    /**
     * Connects to the SQL database to allow for mass server-side storage.
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void connectSQL(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        final String JDBC_DRIVER = "";
        final String DB_URL = "";

        final String USER = "";
        final String PASS = "";

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves all PlayerData and GuildData files to the appropriate SQL database table.
     */
    public void startDataSync(){
        sc.getServer().getScheduler().runTaskLater(sc, () -> {
            for (int i = 0; i < sc.getServer().getOnlinePlayers().size(); i++){
                Player player = (Player)sc.getServer().getOnlinePlayers().toArray()[i];
                sc.getServer().getScheduler().runTaskLater(sc, () -> {
                    sc.playerDataMap.get(player.getUniqueId()).saveDataToSQL();
                }, i * 5L);
            }
            sc.getLogger().info("PlayerData synced.");
            startDataSync();
        }, DATA_SYNC_TIME);
    }

    public void shutdownSaveData(){
        //Sync all player data on shutdown
        for (Player player : sc.getServer().getOnlinePlayers()){
            sc.playerDataMap.get(player.getUniqueId()).saveDataToSQL();
            sc.getLogger().info(player.getName() + "'s data has been synced.");
        }
    }

}
