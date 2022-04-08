package sorcerycraft.abilities.earth;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import sorcerycraft.abilities.Ability;
import sorcerycraft.abilities.AbilityUpdater;
import sorcerycraft.main.SorceryCraft;

import java.util.ArrayList;

public class RockWallAbility extends Ability {

    private SorceryCraft sc = SorceryCraft.getPlugin(SorceryCraft.class);
    private ArrayList<Block> replacedBlocks = new ArrayList<>();

    private final int deathTime = 600;
    private final int coolDown = 20 * 5;

    private double rotation;
    private int xAddition;
    private int zAddition;

    @Override
    public void doAbility() {
        if (this.clickedBlock != null) {
            if (playerData.cooldownRockWall <= 0) {
                playerData.cooldownRockWall = coolDown;
                AbilityUpdater.abilities.add(this);

                //Get player rotation
                rotation = (this.playerYaw - 180 ) %360;
                if (rotation < 0) {
                    rotation += 360.0;
                }

                xAddition = ((0 <= rotation && rotation < 45) || (135 <= rotation && rotation <= 225) || (315 <= rotation && rotation < 360))? 1 : 0;
                zAddition = !((0 <= rotation && rotation < 45) || (135 <= rotation && rotation <= 225) || (315 <= rotation && rotation < 360))? 1 : 0;

            } else {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GRAY + "Cooldown: " + playerData.cooldownRockWall/20 + "s"));
            }
        }
    }

    @Override
    public void update() {
        if (this.timer == 0) {
            placeWallLayer(1);
        }
        else if (this.timer == 5) {
            placeWallLayer(2);
        }
        else if (this.timer == 10) {
            placeWallLayer(3);
        }
        else if (this.timer == 15) {
            placeWallLayer(4);
        }
        else if (this.timer == deathTime) {
            resetDamage();
        }
        this.timer++;
    }

    public void placeWallLayer(int layer) {
        //Center Pillar
        placeStoneBlockAt(clickedBlock.getLocation(), 0, layer, 0);

        //Block to Right
        placeStoneBlockAt(clickedBlock.getLocation(), xAddition, layer, zAddition);

        //Block to Left
        placeStoneBlockAt(clickedBlock.getLocation(), -xAddition, layer, -zAddition);

        if (layer != 4){
            if (xAddition == 0) {
                //Block to Back Left
                placeStoneBlockAt(clickedBlock.getLocation(), 1 * ((0 <= rotation && rotation < 45) || (45 <= rotation && rotation <= 135) || (315 <= rotation && rotation < 360) ? -1 : 1), layer, -2 * zAddition);
                placeStoneBlockAt(clickedBlock.getLocation(), 1 * ((0 <= rotation && rotation < 45) || (45 <= rotation && rotation <= 135) || (315 <= rotation && rotation < 360) ? -1 : 1), layer, 2 * zAddition);
            } else if (xAddition == 1) {
                placeStoneBlockAt(clickedBlock.getLocation(), -2 * xAddition, layer, -1 * ((0 <= rotation && rotation < 45) || (45 <= rotation && rotation <= 135) || (315 <= rotation && rotation < 360) ? -1 : 1));
                placeStoneBlockAt(clickedBlock.getLocation(), 2 * xAddition, layer, -1 * ((0 <= rotation && rotation < 45) || (45 <= rotation && rotation <= 135) || (315 <= rotation && rotation < 360) ? -1 : 1));
            }
        }
    }

    public void placeStoneBlockAt(Location location, int xOffset, int yOffset, int zOffset) {
        Block block = this.world.getBlockAt(location.add(xOffset, yOffset, zOffset));
        if (!block.getType().isSolid()) {
            this.replacedBlocks.add(block);
            block.setType(Material.STONE);
            this.world.playSound(block.getLocation(), Sound.BLOCK_GLASS_PLACE, 1, 1);
            this.world.spawnParticle(Particle.ITEM_CRACK, block.getLocation(), 10, 1, 0.1, 0.1, 0.1, new ItemStack(Material.STONE));
        }
    }

    @Override
    public void resetDamage() {
        for (Block block : replacedBlocks) {
            if (block.getType() == Material.STONE) {
                block.setType(Material.AIR);
                this.world.spawnParticle(Particle.ITEM_CRACK, block.getLocation(), 10, 1, 0.1, 0.1, 0.1, new ItemStack(Material.STONE));
                this.world.playSound(block.getLocation(), Sound.BLOCK_STONE_BREAK, 1, 1);
            }
        }
        this.isScheduledForRemoved = true;
    }
}
