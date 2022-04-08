package sorcerycraft.main;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import sorcerycraft.abilities.AbilityUpdater;
import sorcerycraft.commands.SorceryCommand;
import sorcerycraft.data.PlayerData;
import sorcerycraft.events.*;
import sorcerycraft.item.ItemManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class SorceryCraft extends JavaPlugin {

    /**
     * The UUID to PlayerData relation map.
     */
    public HashMap<UUID, PlayerData> playerDataMap = new HashMap<>();

    /**
     * The prefix for the plugin.
     */
    public String pluginPrefix = "" + ChatColor.RED + ChatColor.BOLD + "SC " + ChatColor.GRAY;

    /**
     * The Ability updater for animations and other operations.
     */
    public AbilityUpdater abilityUpdater;

    /**
     * The SQL Database manager.
     */
    public SQLManager sqlManager;

    /**
     * The Tab List manager.
     */
    public TabListManager tabListManager;

    /**
     * Runs when the server starts up. Calls registry methods, loads the configuration file, and connects the SQL database.
     */
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(pluginPrefix + "SorceryCraft has been enabled.");

        //Registers all of the events
        registerEvents();

        //Registers all of the commands
        registerCommands();

        //Create and saves the configuration file
        loadConfig();

        //Init SQL connection
        sqlManager = new SQLManager();
        sqlManager.connectSQL();


        //Sync all player and guild data on a regular basis
        sqlManager.startDataSync();

        //Create all custom items
        ItemManager.initItems();

        //Start updating all ongoing abilities
        abilityUpdater = new AbilityUpdater();
        abilityUpdater.startUpdater();


        //Init tablist manager
        tabListManager = new TabListManager();
        tabListManager.startTabListSync();

    }

    /**
     * Runs when server shuts down. All data from every player and guild is saved.
     */
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(pluginPrefix + "SorceryCraft has been disabled.");
        sqlManager.shutdownSaveData();
    }

    /**
     * Initializes all events needed.
     */
    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new PlayerJoinEvent(this), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveEvent(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractEvent(this), this);
        getServer().getPluginManager().registerEvents(new ItemDropEvent(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickEvent(this), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnEvent(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathEvent(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractEntityEvent(this), this);
    }

    /**
     * Initializes all command needed.
     */
    public void registerCommands(){
        getCommand("sorcery").setExecutor(new SorceryCommand(this));
        getCommand("sc").setExecutor(new SorceryCommand(this));
    }

    /**
     * Loads the configuration file, or create one if it does not exist.
     */
    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}
