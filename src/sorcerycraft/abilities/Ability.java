package sorcerycraft.abilities;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sorcerycraft.abilities.fire.FireBallAbility;
import sorcerycraft.data.PlayerData;
import sorcerycraft.item.ItemManager;
import sorcerycraft.main.SorceryCraft;

import java.util.Arrays;

public abstract class Ability {

    /**
     * The timer for updating the ability if the ability is time based.
     */
    public int timer;

    /**
     * The current instance of the running plugin.
     */
    public SorceryCraft sc = SorceryCraft.getPlugin(SorceryCraft.class);

    /**
     * The player using the ability.
     */
    public Player player;

    /**
     * The player's data.
     */
    public PlayerData playerData;

    /**
     * The block the player has clicked, if it exists.
     */
    public Block clickedBlock;

    /**
     * Whether the player has right or left clicked on air, a block, or an entity.
     */
    public Action action;

    /**
     * Whether the ability is scheduled for termination.
     */
    public boolean isScheduledForRemoved = false;

    /**
     * The yaw value of the player.
     */
    public double playerYaw;

    /**
     * The world the ability is taking place in.
     */
    public World world;

    /**
     * The block the player is looking at, if there is one within a certain radius.
     */
    public Block lookedAtBlock;

    /**
     *
     *
     *
     * @param player
     * @param playerData
     * @param clickedBlock
     * @param action
     * @param world
     */
    public void doAbility(Player player, PlayerData playerData, Block clickedBlock, Action action, World world){
        this.player = player;
        this.playerData = playerData;
        this.clickedBlock = clickedBlock;
        this.action = action;
        this.playerYaw = player.getLocation().getYaw();
        this.world = world;
        this.lookedAtBlock = player.getTargetBlockExact(100);
        this.timer = 0;
        doAbility();
    }

    /**
     * The inherited function for beginning the ability.
     */
    public void doAbility(){}

    /**
     *
     * @param material
     * @param displayName
     * @param lore
     * @param element
     * @param abilityID
     * @param elementLevel
     * @return
     */
    public ItemStack setupAbility(Material material, String displayName, String[] lore, String element, int abilityID, int elementLevel, Class<? extends Ability> abilityClass){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        //Adding item meta
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.setDisplayName(displayName);
        itemMeta.addEnchant(Enchantment.LUCK, 1, true);
        itemMeta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(itemMeta);

        //Adding custom item data
        net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();

        //Setting custom data
        compound.setBoolean("abilityItem", true);
        compound.setString("element", element);
        compound.setInt("abilityID", abilityID);
        compound.setInt("elementLevel", elementLevel);

        nmsStack.setTag(compound);
        itemStack = CraftItemStack.asBukkitCopy(nmsStack);

        //Add to ItemManager
        ItemManager.abilityToItem.get(element).put(abilityID, itemStack);
        ItemManager.abilityToAbilityClass.get(element).put(abilityID, abilityClass);

        return itemStack;
    }

    /**
     * The update loop called for any updates to an ability.
     */
    public void update(){}

    /**
     * Used to remove the ability and all effects had upon the world.
     */
    public void resetDamage(){}

}
