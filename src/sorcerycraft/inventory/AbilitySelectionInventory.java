package sorcerycraft.inventory;

import net.minecraft.server.v1_16_R3.Item;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sorcerycraft.data.PlayerData;
import sorcerycraft.item.ItemManager;
import sorcerycraft.main.SorceryCraft;

public class AbilitySelectionInventory {

    public static String name = "" + ChatColor.WHITE + "Ability Selection Menu";

    private SorceryCraft sc = SorceryCraft.getPlugin(SorceryCraft.class);

    public void createInventory(Player player) {
        Inventory i = sc.getServer().createInventory(null, 54, name);
        PlayerData playerData = sc.playerDataMap.get(player.getUniqueId());

        switch(playerData.guild){
            case "water":
                for(int j = 0; j < ItemManager.abilityToItem.get("water").keySet().size(); j++){
                    ItemStack itemStack = ItemManager.abilityToItem.get("water").get(ItemManager.abilityToItem.get("water").keySet().toArray()[j]);
                    if(isCorrectLevel(playerData, itemStack, "water")){
                        i.setItem(j, itemStack);
                    }
                }
                break;
            case "fire":
                for(int j = 0; j < ItemManager.abilityToItem.get("fire").keySet().size(); j++){
                    ItemStack itemStack = ItemManager.abilityToItem.get("fire").get(ItemManager.abilityToItem.get("fire").keySet().toArray()[j]);
                    if(isCorrectLevel(playerData, itemStack, "fire")){
                        i.setItem(j, itemStack);
                    }
                }
                break;
            case "earth":
                for(int j = 0; j < ItemManager.abilityToItem.get("earth").keySet().size(); j++){
                    ItemStack itemStack = ItemManager.abilityToItem.get("earth").get(ItemManager.abilityToItem.get("earth").keySet().toArray()[j]);
                    if(isCorrectLevel(playerData, itemStack, "earth")){
                        i.setItem(j, itemStack);
                    }
                }
                break;
            case "air":
                for(int j = 0; j < ItemManager.abilityToItem.get("air").keySet().size(); j++){
                    ItemStack itemStack = ItemManager.abilityToItem.get("air").get(ItemManager.abilityToItem.get("air").keySet().toArray()[j]);
                    if(isCorrectLevel(playerData, itemStack, "air")){
                        i.setItem(j, itemStack);
                    }
                }
                break;
        }

        i.setItem(53, ItemManager.cancelItem);
        player.openInventory(i);
    }

    public boolean isCorrectLevel(PlayerData playerData, ItemStack itemStack, String guild){
        net.minecraft.server.v1_16_R3.ItemStack craftItemStack = CraftItemStack.asNMSCopy(itemStack);
        switch(guild){
            case "fire":
                if(craftItemStack.getTag().getInt("elementLevel") <= playerData.getElementExpLevel(playerData.fireexp)[0]){
                    return true;
                }
                break;
            case "water":
                if(craftItemStack.getTag().getInt("elementLevel") <= playerData.getElementExpLevel(playerData.waterexp)[0]){
                    return true;
                }
                break;
            case "earth":
                if(craftItemStack.getTag().getInt("elementLevel") <= playerData.getElementExpLevel(playerData.earthexp)[0]){
                    return true;
                }
                break;
            case "air":
                if(craftItemStack.getTag().getInt("elementLevel") <= playerData.getElementExpLevel(playerData.airexp)[0]){
                    return true;
                }
                break;
        }
        return false;
    }
}
