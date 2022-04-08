package sorcerycraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sorcerycraft.data.PlayerData;
import sorcerycraft.inventory.AbilitySelectionInventory;
import sorcerycraft.inventory.GuildSelectionInventory;
import sorcerycraft.main.SorceryCraft;

public class SorceryCommand implements CommandExecutor {

    private SorceryCraft sc;

    public SorceryCommand(SorceryCraft sc) {
        this.sc = sc;
    }


    //THIS IS A WORK IN PROGRESS TEMP CLASS

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        switch(strings.length){
            case 0:
                sender.sendMessage("Please use a valid command!");
                break;
            case 1:
                if(sender instanceof Player) {
                    switch (strings[0].toLowerCase()) {
                        case "guild":
                            GuildSelectionInventory inventory = new GuildSelectionInventory();
                            inventory.createInventory(((Player)sender));
                            break;
                        case "level":
                            PlayerData playerData = sc.playerDataMap.get(((Player) sender).getUniqueId());
                            long[] expInfo = playerData.getExpLevel();

                            sender.sendMessage("" + ChatColor.GRAY + ChatColor.BOLD + "You are level " + ChatColor.GREEN + ChatColor.BOLD + expInfo[0] + ChatColor.GRAY + ChatColor.BOLD + "!");
                            sender.sendMessage("" + ChatColor.GRAY + ChatColor.BOLD + "Experience: " + ChatColor.GREEN + ChatColor.BOLD + expInfo[4]  + "/" + expInfo[3] + ChatColor.GRAY + ChatColor.BOLD);
                            break;
                        case "wipeexp":
                            playerData = sc.playerDataMap.get(((Player) sender).getUniqueId());
                            playerData.exp = 0;
                            playerData.waterexp = 0;
                            playerData.fireexp = 0;
                            playerData.airexp = 0;
                            playerData.earthexp = 0;
                            break;
                        case "wipeguild":
                            playerData = sc.playerDataMap.get(((Player) sender).getUniqueId());
                            playerData.guild = "none";
                        case "ability":
                            AbilitySelectionInventory i = new AbilitySelectionInventory();
                            i.createInventory(((Player)sender));
                            sender.sendMessage("" + ChatColor.GRAY + ChatColor.BOLD + "Select your abilities!");
                            break;
                        case "wipe":
                            playerData = sc.playerDataMap.get(((Player) sender).getUniqueId());
                            playerData.hotbarItems[((Player) sender).getInventory().getHeldItemSlot()] = -1;
                            break;
                    }
                    break;
                }
            case 2:
                if(sender instanceof Player) {
                    switch (strings[0].toLowerCase()) {
                        case "addexp":
                            sc.playerDataMap.get(((Player) sender).getUniqueId()).addExp(Integer.parseInt(strings[1]));
                            break;
                    }
                    break;
                }
        }

        return false;
    }
}
