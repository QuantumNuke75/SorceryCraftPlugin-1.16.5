package sorcerycraft.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import sorcerycraft.main.SorceryCraft;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerData {

    /**
     * The player the data belongs to.
     */
    public Player player;

    /**
     * The current instance of the running plugin.
     */
    private SorceryCraft sc;

    /**
     * The overall experience of the player.
     */
    public long exp = 0;

    /**
     * The total water experience of the player.
     */
    public long waterexp = 0;

    /**
     * The total fire experience of the player.
     */
    public long fireexp = 0;

    /**
     * The total air experience of the player.
     */
    public long airexp = 0;

    /**
     * The total earth experience of the player.
     */
    public long earthexp = 0;

    /**
     * The player's current ability setup within their hotbar.
     */
    public int[] hotbarItems = new int[9];

    /**
     * The guild that the player belongs to, "none" be default
     */
    public String guild = "none";

    /**
     * All the cooldowns for the abilities.
     *
     *      Fire guild cooldowns
     */
    public float cooldownFireBall = 0;
    public float cooldownFlameThrow = 0;

    /**
     *      Water guild cooldowns
     */
    public float cooldownIceWall = 0;
    public float cooldownWaterBlast = 0;

    /**
     *      Air guild cooldowns
     */
    public float cooldownWindPush = 0;
    public float cooldownSuffocate = 0;

    /**
     *      Earth guild cooldowns
     */
    public float cooldownRockWall = 0;
    public float cooldownRockThrow = 0;

    public PlayerData(Player player, SorceryCraft sc){
        this.player = player;
        this.sc = sc;

        decreaseCooldowns();
    }

    /**
     *
     */
    public void loadDataFromSQL(){

        checkSQLConnection();

        try {
            exp = getExp();
            waterexp = getElementExp("waterexp");
            fireexp = getElementExp("fireexp");
            airexp = getElementExp("airexp");
            earthexp = getElementExp("earthexp");

            //Get the guild
            getGuild();

            //Gets hotbar items
            getHotBarItems();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void saveDataToSQL(){

        checkSQLConnection();

        try {
            setExp();
            setElementExp("waterexp", waterexp);
            setElementExp("fireexp", fireexp);
            setElementExp("airexp", airexp);
            setElementExp("earthexp", earthexp);

            //Saves hotbar items
            for(int i = 0; i < 9; i ++){
                setHotbarItem(hotbarItems[i], i);
            }

            setGuild();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @throws SQLException
     */
    public void firstTimeSetup() throws SQLException {

        checkSQLConnection();

        //Check for main player daya
        PreparedStatement statement = sc.sqlManager.connection.prepareStatement("SELECT * FROM playerdata WHERE uuid = ?");
        statement.setString(1, player.getUniqueId().toString());
        ResultSet resultset = statement.executeQuery();
        boolean doesPlayerHaveData = resultset.next();
        resultset.close();

        if (doesPlayerHaveData) {
            Bukkit.getServer().getLogger().info("Player has data.");
        } else {
                PreparedStatement preparedStatement = sc.sqlManager.connection.prepareStatement(
                        "INSERT INTO playerdata (uuid, exp, waterexp, fireexp, airexp, earthexp, guild) VALUES (?, 0, 0, 0, 0, 0, 'none')");
                preparedStatement.setString(1, player.getUniqueId().toString());
                preparedStatement.executeUpdate();
                preparedStatement.close();
        }
        statement.close();

        //Check for hotbar data
        statement = sc.sqlManager.connection.prepareStatement("SELECT * FROM hotbaritems WHERE uuid = ?");
        statement.setString(1, player.getUniqueId().toString());
        resultset = statement.executeQuery();
        doesPlayerHaveData = resultset.next();
        resultset.close();

        if (doesPlayerHaveData) {
            Bukkit.getServer().getLogger().info("Player has hotbar data.");
        } else {
            PreparedStatement preparedStatement = sc.sqlManager.connection.prepareStatement(
                    "INSERT INTO hotbaritems (uuid, slot0, slot1, slot2, slot3, slot4, slot5, slot6, slot7, slot8) VALUES (?, -1, -1, -1, -1, -1, -1, -1, -1, -1)");
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        statement.close();
    }

    /**
     *
     * @param element
     * @return
     * @throws SQLException
     */
    public long getElementExp(String element) throws SQLException {

        checkSQLConnection();

        long experience = 0;
        PreparedStatement stat = sc.sqlManager.connection.prepareStatement("SELECT " + element + " FROM playerdata WHERE uuid = ?");
        stat.setString(1, player.getUniqueId().toString());
        ResultSet result = stat.executeQuery();
        while (result.next()) {
            experience = result.getLong(element);
        }
        stat.close();
        return experience;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public long getExp() throws SQLException{

        checkSQLConnection();

        long experience = 0;
        PreparedStatement stat = sc.sqlManager.connection.prepareStatement("SELECT " + "exp" + " FROM playerdata WHERE uuid = ?");
        stat.setString(1, player.getUniqueId().toString());
        ResultSet result = stat.executeQuery();
        while (result.next()) {
            experience = result.getLong("exp");
        }
        stat.close();
        return experience;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public void getGuild() throws SQLException {

        checkSQLConnection();

        PreparedStatement stat = sc.sqlManager.connection.prepareStatement("SELECT " + "guild" + " FROM playerdata WHERE uuid = ?");
        stat.setString(1, player.getUniqueId().toString());
        ResultSet result = stat.executeQuery();
        while (result.next()) {
            this.guild = result.getString("guild");
        }
        stat.close();
    }

    /**
     *
     * @param element
     * @param experience
     * @throws SQLException
     */
    public void setElementExp(String element, long experience) throws SQLException {

        checkSQLConnection();

        PreparedStatement stat = sc.sqlManager.connection.prepareStatement("UPDATE playerdata SET " + element + " = ? WHERE uuid = ?");
        stat.setString(2, player.getUniqueId().toString());
        stat.setLong(1, experience);
        stat.executeUpdate();
        stat.close();
    }

    /**
     *
     * @throws SQLException
     */
    public void setExp() throws SQLException {

        checkSQLConnection();

        PreparedStatement stat = sc.sqlManager.connection.prepareStatement("UPDATE playerdata SET " + "exp" + " = ? WHERE uuid = ?");
        stat.setString(2, player.getUniqueId().toString());
        stat.setLong(1, exp);
        stat.executeUpdate();
        stat.close();
    }

    /**
     *
     * @throws SQLException
     */
    public void setGuild() throws SQLException {

        checkSQLConnection();

        PreparedStatement stat = sc.sqlManager.connection.prepareStatement("UPDATE playerdata SET " + "guild" + " = ? WHERE uuid = ?");
        stat.setString(2, player.getUniqueId().toString());
        stat.setString(1, guild);
        stat.executeUpdate();
        stat.close();
    }

    /**
     *
     * @throws SQLException
     */
    public void getHotBarItems() throws SQLException {

        checkSQLConnection();

        for(int i = 0; i < 9; i ++){
            int item = -1;
            PreparedStatement stat = sc.sqlManager.connection.prepareStatement("SELECT slot" + i + " FROM hotbaritems WHERE uuid = ?");
            stat.setString(1, player.getUniqueId().toString());
            ResultSet result = stat.executeQuery();
            while (result.next()) {
                item = result.getInt("slot" + i);
            }
            stat.close();
            hotbarItems[i] = item;
        }
    }

    /**
     *
     * @param id
     * @param slot
     * @throws SQLException
     */
    public void setHotbarItem(int id, int slot) throws SQLException {

        checkSQLConnection();

        PreparedStatement stat = sc.sqlManager.connection.prepareStatement("UPDATE hotbaritems SET slot" + slot + " = ? WHERE uuid = ?");
        stat.setString(2, player.getUniqueId().toString());
        stat.setInt(1, id);
        stat.executeUpdate();
        stat.close();
    }

    /**
     *
     * @param amount
     */
    public void addExp(long amount){
        this.exp += amount;
    }

    /**
     *
     * @return
     */
    public long[] getExpLevel(){
        long currentLevel = (long) Math.floor(-0.5 + Math.sqrt(1 + ((4*exp)/25))/2);
        long expForNextLevel = 25*((currentLevel+1)*(currentLevel+2));
        long expForCurrentLevel = 25*((currentLevel)*(currentLevel+1));
        long totalNeededExp = expForNextLevel - expForCurrentLevel;
        long overflowExp = exp - 25*((currentLevel)*(currentLevel+1));

        return new long[]{currentLevel, expForNextLevel, expForCurrentLevel, totalNeededExp, overflowExp};
    }

    /**
     *
     * @param elementExp
     * @return
     */
    public long[] getElementExpLevel(long elementExp){
        long currentLevel = (long) Math.floor(-0.5 + Math.sqrt(1 + ((4*elementExp)/25))/2);
        long expForNextLevel = 25*((currentLevel+1)*(currentLevel+2));
        long expForCurrentLevel = 25*((currentLevel)*(currentLevel+1));
        long totalNeededExp = expForNextLevel - expForCurrentLevel;
        long overflowExp = elementExp - 25*((currentLevel)*(currentLevel+1));

        return new long[]{currentLevel, expForNextLevel, expForCurrentLevel, totalNeededExp, overflowExp};
    }

    /**
     *
     */
    public void decreaseCooldowns(){
        sc.getServer().getScheduler().runTaskLater(sc, () -> {

            if(cooldownFireBall > 0) {
                cooldownFireBall -= 1;
            }
            if(cooldownIceWall > 0) {
                cooldownIceWall -= 1;
            }
            if(cooldownFlameThrow > 0) {
                cooldownFlameThrow -= 1;
            }
            if(cooldownWaterBlast > 0) {
                cooldownWaterBlast -= 1;
            }
            if(cooldownWindPush > 0) {
                cooldownWindPush -= 1;
            }
            if(cooldownRockWall > 0) {
                cooldownRockWall -= 1;
            }
            if(cooldownRockThrow > 0) {
                cooldownRockThrow -= 1;
            }
            if(cooldownSuffocate > 0) {
                cooldownSuffocate -= 1;
            }
            decreaseCooldowns();
        }, 1);
    }

    /**
     * Check is the SQL Database is not connected, if so, reconnect it.
     */
    public void checkSQLConnection(){
        try {
            if(sc.sqlManager.connection.isClosed()){
                sc.sqlManager.connectSQL();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}