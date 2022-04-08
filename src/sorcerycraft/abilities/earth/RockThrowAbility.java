package sorcerycraft.abilities.earth;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.IBlockData;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import sorcerycraft.abilities.Ability;
import sorcerycraft.abilities.AbilityUpdater;
import sorcerycraft.entity.CustomFallingBlock;
import sorcerycraft.main.SorceryCraft;

import java.util.ArrayList;

public class RockThrowAbility extends Ability {

    private SorceryCraft sc = SorceryCraft.getPlugin(SorceryCraft.class);

    private final int deathTime = 20 * 30;
    private final int coolDown = 20 * 5;

    private CustomFallingBlock customFallingBlock;

    @Override
    public void doAbility() {
        if (this.clickedBlock != null) {
            if (playerData.cooldownRockWall <= 0) {
                playerData.cooldownRockThrow = coolDown;
                AbilityUpdater.abilities.add(this);

                customFallingBlock = new CustomFallingBlock(clickedBlock.getLocation(), fromMaterial(Material.STONE));
                customFallingBlock.spawn();

            } else {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GRAY + "Cooldown: " + playerData.cooldownRockWall/20 + "s"));
            }
        }
    }

    public IBlockData fromMaterial(Material m) {
        net.minecraft.server.v1_16_R3.Block nmsBlock = CraftMagicNumbers.getBlock(m);
        return nmsBlock.getBlockData();
    }

    @Override
    public void update() {
        if (this.timer < 5) {

        }
        else if (this.timer == 5){
            this.customFallingBlock.canThrow = true;
        }
        else if (this.timer == deathTime) {
            this.world.spawnParticle(Particle.ITEM_CRACK, new Location(this.world, customFallingBlock.locX(), customFallingBlock.locY(), customFallingBlock.locZ()), 10, 1, 0.1, 0.1, 0.1, new ItemStack(Material.STONE));
            customFallingBlock.killEntity();
            this.isScheduledForRemoved = true;
            return;
        }
        this.timer++;
    }
}
