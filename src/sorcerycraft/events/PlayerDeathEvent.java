package sorcerycraft.events;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftItem;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import sorcerycraft.item.ItemManager;
import sorcerycraft.main.SorceryCraft;

public class PlayerDeathEvent implements Listener {

    private SorceryCraft sc;

    public PlayerDeathEvent(SorceryCraft sc) {
        this.sc = sc;
    }

    @EventHandler
    public void onPlayerDeathDrop(org.bukkit.event.entity.PlayerDeathEvent e) {

        //Checks if each item is an ability item, if so, remove from drops
        for(int i = e.getDrops().size() - 1; i >= 0; i--) {
            if(isAbilityItem(e.getDrops().get(i))) {
                e.getDrops().remove(e.getDrops().get(i));
            }
        }
    }

    /**
     * Checks whether the given ItemStack is an ability item
     *
     * @param itemStack given ItemStack to check
     * @return returns whether the ItemStack contains an NBTTag with "abilityItem" as true
     */
    public static boolean isAbilityItem(ItemStack itemStack){
        net.minecraft.server.v1_16_R3.ItemStack craftItemStack = CraftItemStack.asNMSCopy(itemStack);
        if(craftItemStack.hasTag() && craftItemStack.getTag().hasKey("abilityItem") && craftItemStack.getTag().getBoolean("abilityItem")){
            return true;
        }
        return false;
    }

}

