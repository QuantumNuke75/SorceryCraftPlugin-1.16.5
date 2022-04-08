package sorcerycraft.events;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import sorcerycraft.data.PlayerData;
import sorcerycraft.inventory.AbilitySelectionInventory;
import sorcerycraft.inventory.GuildSelectionInventory;
import sorcerycraft.item.ItemManager;
import sorcerycraft.main.SorceryCraft;

import java.sql.SQLException;

public class InventoryClickEvent implements Listener {

    private SorceryCraft sc;

    public InventoryClickEvent(SorceryCraft sc) {
        this.sc = sc;
    }

    @EventHandler
    public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        PlayerData playerData = sc.playerDataMap.get(player.getUniqueId());
        ClickType clickType = e.getClick();
        Inventory openedInventory = e.getClickedInventory();
        ItemStack itemStack = e.getCurrentItem();

        if(openedInventory == null){
            return;
        }
        //Guild Selection Inventory
        else if(e.getView().getTitle().equals(GuildSelectionInventory.name)){

            //Prevent taking out items
            e.setCancelled(true);

            //Return if the item is null or does not have metadata
            if(itemStack == null || !itemStack.hasItemMeta()){
                return;
            }
            if(itemStack.getItemMeta().getDisplayName().equals(ItemManager.airItem.getItemMeta().getDisplayName())){
                player.sendMessage("" + ChatColor.GRAY + ChatColor.BOLD + "You have joined the " + ChatColor.WHITE + ChatColor.BOLD + "AIR" + ChatColor.GRAY + ChatColor.BOLD + " guild!");
                playerData.guild = "air";
                wipeAbilityItems(player, playerData);
            }
            else if(itemStack.getItemMeta().getDisplayName().equals(ItemManager.fireItem.getItemMeta().getDisplayName())){
                player.sendMessage("" + ChatColor.GRAY + ChatColor.BOLD + "You have joined the " + ChatColor.RED + ChatColor.BOLD + "FIRE" + ChatColor.GRAY + ChatColor.BOLD + " guild!");
                playerData.guild = "fire";
                wipeAbilityItems(player, playerData);
            }
            else if(itemStack.getItemMeta().getDisplayName().equals(ItemManager.waterItem.getItemMeta().getDisplayName())){
                player.sendMessage("" + ChatColor.GRAY + ChatColor.BOLD + "You have joined the " + ChatColor.AQUA + ChatColor.BOLD + "WATER" + ChatColor.GRAY + ChatColor.BOLD + " guild!");
                playerData.guild = "water";
                wipeAbilityItems(player, playerData);
            }
            else if(itemStack.getItemMeta().getDisplayName().equals(ItemManager.earthItem.getItemMeta().getDisplayName())){
                player.sendMessage("" + ChatColor.GRAY + ChatColor.BOLD + "You have joined the " + ChatColor.YELLOW + ChatColor.BOLD + "EARTH" + ChatColor.GRAY + ChatColor.BOLD + " guild!");
                playerData.guild = "earth";
                wipeAbilityItems(player, playerData);
            }
            else if(itemStack.getItemMeta().equals(ItemManager.cancelItem)){
                player.closeInventory();
            }
        }
        //Ability Selection Inventory
        else if(e.getView().getTitle().equals(AbilitySelectionInventory.name)){

            //Prevent taking out items
            e.setCancelled(true);

            //Return if the item is null or does not have metadata
            if(itemStack == null || !itemStack.hasItemMeta()){
                return;
            }
            //Checking if the item has a NBTTagCompound
            if(CraftItemStack.asNMSCopy(itemStack).hasTag()){

                NBTTagCompound compound = CraftItemStack.asNMSCopy(itemStack).getTag();

                if(compound.getBoolean("abilityItem")){

                    if(player.getInventory().getItemInMainHand().getType() == Material.AIR || (player.getInventory().getItemInMainHand() != null && isAbilityItem(player.getInventory().getItemInMainHand()))){
                        player.getInventory().setItemInMainHand(itemStack);
                        playerData.hotbarItems[player.getInventory().getHeldItemSlot()] = CraftItemStack.asNMSCopy(player.getInventory().getItemInMainHand()).getTag().getInt("abilityID");
                        player.closeInventory();
                    }
                    else{
                        player.sendMessage("" + ChatColor.GRAY + ChatColor.BOLD + "That slot is occupied! " + player.getInventory().getItemInMainHand());
                        player.closeInventory();
                    }
                }
            }
            else if(itemStack.getItemMeta().equals(ItemManager.cancelItem)){
                player.closeInventory();
            }
        }
        //Prevent interactions with certain items in any inventory
        else if(itemStack != null &&
                CraftItemStack.asNMSCopy(itemStack).hasTag() &&
                CraftItemStack.asNMSCopy(itemStack).getTag().hasKey("abilityItem") &&
                CraftItemStack.asNMSCopy(itemStack).getTag().getBoolean("abilityItem")){
            e.setCancelled(true);
        }
    }

    /**
     * Checks whether the given ItemStack is an ability item
     *
     * @param itemStack given ItemStack to check
     * @return returns whether the ItemStack contains an NBTTag with "abilityItem" as true
     */
    public boolean isAbilityItem(ItemStack itemStack){
        net.minecraft.server.v1_16_R3.ItemStack craftItemStack = CraftItemStack.asNMSCopy(itemStack);
        if(craftItemStack.hasTag() && craftItemStack.getTag().hasKey("abilityItem") && craftItemStack.getTag().getBoolean("abilityItem")){
            return true;
        }
        return false;
    }

    /**
     *
     * @param player
     * @param playerData
     */
    public void wipeAbilityItems(Player player, PlayerData playerData){
        playerData.hotbarItems = new int[]{-1,-1,-1,-1,-1,-1,-1,-1,-1};
        for(int i = 0; i < 9; i++){
            net.minecraft.server.v1_16_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(player.getInventory().getItem(i));
            if(itemStack != null && itemStack.hasTag() && itemStack.getTag().hasKey("abilityItem")){
                player.getInventory().setItem(i, new ItemStack(Material.AIR));
            }
        }
    }
}
