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
import sorcerycraft.abilities.Ability;
import sorcerycraft.abilities.AbilityUpdater;
import sorcerycraft.main.SorceryCraft;

import java.util.ArrayList;
import java.util.List;

public class SuffocateAbility extends Ability {

    private final int deathTime = 300;
    private final int coolDown = 20 * 5;

    @Override
    public void doAbility() {
        if (this.lookedAtBlock != null && this.lookedAtBlock.getLocation().distance(player.getLocation()) < 20) {
            if (playerData.cooldownSuffocate <= 0) {
                playerData.cooldownSuffocate = coolDown;
                AbilityUpdater.abilities.add(this);
            } else {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GRAY + "Cooldown: " + playerData.cooldownSuffocate/20 + "s"));
            }
        }
    }

    @Override
    public void update() {
        if (this.timer % 5 == 0 && this.timer < 300) {
            spawnParticleSphere(lookedAtBlock.getLocation(), 5, 0, true, true, 5);
            damageAllEntitesInRadius(player, lookedAtBlock.getLocation().add(0, 5, 0), 5);
        }
        else if (this.timer == deathTime) {
            resetDamage();
        }
        this.timer++;
    }

    public static void spawnParticleSphere(Location loc, float r, float h, boolean hollow, boolean sphere, float plus_y) {
        float cx = loc.getBlockX();
        float cy = loc.getBlockY();
        float cz = loc.getBlockZ();
        for (float x = cx - r; x <= cx + r; x+=0.1){
            for (float z = cz - r; z <= cz + r; z+=0.1) {
                for (float y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y+=0.1) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        loc.getWorld().spawnParticle(Particle.ASH, loc.add(x, y + plus_y, z), 0, 0, 0, 0, 0.1);
                    }
                }
            }
        }
    }

    public static void damageAllEntitesInRadius(Player player, Location location, float radius){
        for (Entity entity : location.getWorld().getEntities()) {
            if(entity instanceof LivingEntity && location.distance(entity.getLocation()) <= radius){
                ((LivingEntity) entity).damage(3, player);
            }
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

    @Override
    public void resetDamage() {
        this.isScheduledForRemoved = true;
    }
}
