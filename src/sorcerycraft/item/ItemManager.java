package sorcerycraft.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sorcerycraft.abilities.Ability;
import sorcerycraft.abilities.air.SuffocateAbility;
import sorcerycraft.abilities.air.WindPushAbility;
import sorcerycraft.abilities.earth.RockThrowAbility;
import sorcerycraft.abilities.earth.RockWallAbility;
import sorcerycraft.abilities.fire.FireBallAbility;
import sorcerycraft.abilities.fire.FlameThrowAbility;
import sorcerycraft.abilities.water.IceWallAbility;
import sorcerycraft.abilities.water.WaterBlastAbility;

import java.util.HashMap;

public class ItemManager {

    //Guild Items
    public static ItemStack waterItem;
    public static ItemStack fireItem;
    public static ItemStack earthItem;
    public static ItemStack airItem;

    //All Ability Items
    public static HashMap<String, HashMap<Integer, ItemStack>> abilityToItem = new HashMap<>();
    public static HashMap<String, HashMap<Integer, Class<? extends Ability>>> abilityToAbilityClass = new HashMap<>();

    //Empty Item
    public static ItemStack emptyItem;

    //Cancel Item
    public static ItemStack cancelItem;

    //Fire Items
    public static ItemStack fireBallItem;
    public static ItemStack flameThrowItem;

    //Water Items
    public static ItemStack iceWallItem;
    public static ItemStack waterBlastItem;

    //Wind Items
    public static ItemStack windPushItem;
    public static ItemStack suffocateItem;

    //Earth Items
    public static ItemStack rockWallItem;
    public static ItemStack rockThrowItem;

    //IMPORTANT ITEM IDS
    // 1-100 - Fire
    // 101 - 200 - Water
    // 201 - 300 - Earth
    // 301 - 400 - Air


    /**
     *
     */
    public static void initItems(){

        abilityToItem.put("fire", new HashMap<>());
        abilityToItem.put("water", new HashMap<>());
        abilityToItem.put("earth", new HashMap<>());
        abilityToItem.put("air", new HashMap<>());

        abilityToAbilityClass.put("fire", new HashMap<>());
        abilityToAbilityClass.put("water", new HashMap<>());
        abilityToAbilityClass.put("earth", new HashMap<>());
        abilityToAbilityClass.put("air", new HashMap<>());

        //Guild Selection Items
        waterItem = createGuildItem(Material.KELP, ChatColor.AQUA, "Water");
        fireItem = createGuildItem(Material.FIRE_CHARGE, ChatColor.RED, "Fire");
        earthItem = createGuildItem(Material.CLAY_BALL, ChatColor.YELLOW, "Earth");
        airItem = createGuildItem(Material.FEATHER, ChatColor.WHITE, "Air");

        createEmptyItem();
        createCancelItem();

        //Fire Ability Items
        fireBallItem = new FireBallAbility().setupAbility(Material.BLAZE_ROD, "" + ChatColor.YELLOW + "Fireball Launcher", new String[]{"Cooldown: 5s"}, "fire", 1, 0, FireBallAbility.class);
        flameThrowItem = new FireBallAbility().setupAbility(Material.BLAZE_POWDER, "" + ChatColor.YELLOW + "Flamethrower", new String[]{"Cooldown: 60s"}, "fire", 2, 0, FlameThrowAbility.class);

        //Water Ability Items
        iceWallItem = new IceWallAbility().setupAbility(Material.SUGAR, "" + ChatColor.AQUA + "Ice Wall", new String[]{"Cooldown: 30s"}, "water", 101, 0, IceWallAbility.class);
        waterBlastItem = new WaterBlastAbility().setupAbility(Material.PRISMARINE_SHARD, "" + ChatColor.AQUA + "Water Blast", new String[]{"Cooldown: 30s"}, "water", 102, 0, WaterBlastAbility.class);

        //Earth Ability Items
        rockWallItem = new RockWallAbility().setupAbility(Material.CLAY_BALL, "" + ChatColor.AQUA + "Rock Wall", new String[]{"Cooldown: 30s"}, "earth", 201, 0, RockWallAbility.class);
        rockThrowItem = new RockThrowAbility().setupAbility(Material.COAL, "" + ChatColor.AQUA + "Rock Throw", new String[]{"Cooldown: 30s"}, "earth", 202, 0, RockThrowAbility.class);

        //Air Ability Items
        windPushItem = new WindPushAbility().setupAbility(Material.NETHER_STAR, "" + ChatColor.WHITE + "Wind Push", new String[]{"Cooldown: 30s"}, "air", 301, 0, WindPushAbility.class);
        suffocateItem = new SuffocateAbility().setupAbility(Material.GRAY_DYE, "" + ChatColor.WHITE + "Suffocate", new String[]{"Cooldown: 30s"}, "air", 302, 0, SuffocateAbility.class);
    }

    /**
     *
     * Gets the ItemStack from the given abilityID.
     *
     * @param abilityID - The ID of the ability.
     * @return - The matching ItemStack.
     */
    public static ItemStack getItemStackFromID(int abilityID){
        if(abilityID > 300){
            return abilityToItem.get("air").get(abilityID);
        }
        else if(abilityID > 200){
            return abilityToItem.get("earth").get(abilityID);
        }
        else if(abilityID > 100){
            return abilityToItem.get("earth").get(abilityID);
        }
        else if(abilityID > 0){
            return abilityToItem.get("fire").get(abilityID);
        }
        return null;
    }

    /**
     *
     * Creates a Guild selection item stack.
     *
     * @param material - The material of the ItemStack.
     * @param color - The color of the ItemStack's name.
     * @param name - The ItemStack's name.
     * @return - The Guild ItemStack.
     */
    private static ItemStack createGuildItem(Material material, ChatColor color, String name){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        //Adding item meta
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.setDisplayName("" + color + ChatColor.BOLD + name);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    /**
     * Initializes Empty item stack.
     */
    private static void createEmptyItem(){
        ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();

        //Adding item meta
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.setDisplayName("Empty");
        itemStack.setItemMeta(itemMeta);
        emptyItem = itemStack;
    }

    /**
     * Initializes Cancel item stack.
     */
    private static void createCancelItem(){
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack.getItemMeta();

        //Adding item meta
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.setDisplayName("Cancel");
        itemStack.setItemMeta(itemMeta);
        cancelItem = itemStack;
    }

}
