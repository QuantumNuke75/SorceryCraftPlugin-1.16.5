package sorcerycraft.events;

import net.minecraft.server.v1_16_R3.IBlockData;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import sorcerycraft.abilities.Ability;
import sorcerycraft.data.PlayerData;
import sorcerycraft.item.ItemManager;
import sorcerycraft.main.SorceryCraft;


public class PlayerInteractEvent implements Listener {

    private SorceryCraft sc;

    public PlayerInteractEvent(SorceryCraft sc) {
        this.sc = sc;
    }

    @EventHandler
    public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent e) {
        Player player = e.getPlayer();
        PlayerData playerData = sc.playerDataMap.get(player.getUniqueId());

        if(e.getItem() != null){

            ItemStack craftItemStack = CraftItemStack.asNMSCopy(e.getItem());

            if(craftItemStack.hasTag()) {
                NBTTagCompound compound = craftItemStack.getTag();
                int abilityID = compound.getInt("abilityID");

                Class<? extends Ability> useAbilityClass = null;

                if (abilityID > 300) {
                    useAbilityClass = ItemManager.abilityToAbilityClass.get("air").get(abilityID);
                } else if (abilityID > 200) {
                    useAbilityClass = ItemManager.abilityToAbilityClass.get("earth").get(abilityID);
                } else if (abilityID > 100) {
                    useAbilityClass = ItemManager.abilityToAbilityClass.get("water").get(abilityID);
                } else if (abilityID > 0) {
                    useAbilityClass = ItemManager.abilityToAbilityClass.get("fire").get(abilityID);
                }

                if (useAbilityClass != null){
                    e.setCancelled(true);
                    Ability useAbility = null;
                    try {
                        useAbility = useAbilityClass.newInstance();
                        useAbility.doAbility(player, playerData, e.getClickedBlock(), e.getAction(), player.getWorld());
                    } catch (InstantiationException ex) {
                        ex.printStackTrace();
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}
