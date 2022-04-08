package sorcerycraft.abilities.air;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import sorcerycraft.abilities.Ability;
import sorcerycraft.abilities.AbilityUpdater;

import java.util.ArrayList;
import java.util.List;

public class WindPushAbility extends Ability {

    private final int coolDown = 20*5;

    @Override
    public void doAbility() {
        if (playerData.cooldownWindPush <= 0){

            AbilityUpdater.abilities.add(this);

            world.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);

            playerData.cooldownWindPush = coolDown;
            LivingEntity hitEntity = getTarget(player);
            Block hitBlock = getTargetBlock(player, 10);

            //boost entity
            if(hitEntity != null && !hitBlock.getType().isSolid()){
                hitEntity.setVelocity(hitEntity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(2));
            }
            //boost player
            else if(hitEntity == null && hitBlock.getType().isSolid()){
                player.setVelocity(player.getLocation().toVector().subtract(hitBlock.getLocation().add(0.5,-0.5,0.5).toVector()).normalize().multiply(1));
            }
            else if(hitEntity != null && hitBlock.getType().isSolid()){
                if(hitBlock.getLocation().distance(player.getLocation()) < hitEntity.getLocation().distance(player.getLocation())){
                    player.setVelocity(player.getLocation().toVector().subtract(hitBlock.getLocation().add(0.5,-0.5,0.5).toVector()).normalize().multiply(1));
                }
                else{
                    hitEntity.setVelocity(hitEntity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(2));
                }
            }
        }
        else{
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GRAY + "Cooldown: " + playerData.cooldownWindPush /20 + "s"));
        }
    }

    public LivingEntity getTarget(Player player) {
        int range = 8;
        List<Entity> nearbyE = player.getNearbyEntities(range, range, range);
        ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();

        for (Entity e : nearbyE) {
            if (e instanceof LivingEntity) {
                livingE.add((LivingEntity) e);
            }
        }

        LivingEntity target = null;
        BlockIterator bItr = new BlockIterator(player, range);
        Block block;
        Location loc;
        int bx, by, bz;
        double ex, ey, ez;

        while (bItr.hasNext()) {
            block = bItr.next();
            bx = block.getX();
            by = block.getY();
            bz = block.getZ();

            for (LivingEntity e : livingE) {
                loc = e.getLocation();
                ex = loc.getX();
                ey = loc.getY();
                ez = loc.getZ();
                if ((bx - .75 <= ex && ex <= bx + 1.75)
                        && (bz - .75 <= ez && ez <= bz + 1.75)
                        && (by - 1 <= ey && ey <= by + 2.5)) {
                    target = e;
                    break;
                }
            }
        }
        return target;
    }

    public final Block getTargetBlock(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (!lastBlock.getType().isSolid()) {
                continue;
            }
            break;
        }
        return lastBlock;
    }
}
