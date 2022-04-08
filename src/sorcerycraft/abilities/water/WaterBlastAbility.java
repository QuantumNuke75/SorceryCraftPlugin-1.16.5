package sorcerycraft.abilities.water;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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

public class WaterBlastAbility extends Ability {

    private final int coolDown = 20*5;

    @Override
    public void doAbility() {
        if (playerData.cooldownWaterBlast <= 0){

            AbilityUpdater.abilities.add(this);

            playerData.cooldownWaterBlast = coolDown;
        }
        else{
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GRAY + "Cooldown: " + playerData.cooldownWaterBlast /20 + "s"));
        }
    }

    @Override
    public void update() {
        if (this.timer < 10) {
            if(player != null) {
                shootWaterBlast();
                if(this.timer % 3 == 0) {
                    world.playSound(player.getLocation(), Sound.AMBIENT_UNDERWATER_ENTER, 2, 2);
                }
            }
        }
        else{
            this.isScheduledForRemoved = true;
        }
        this.timer++;
    }

    private void shootWaterBlast() {
        Vector playerDirection = player.getLocation().getDirection();
        Vector particleVector = playerDirection.clone();

        playerDirection.multiply(100);

        double x = particleVector.getX();
        particleVector.setX(-particleVector.getZ());
        particleVector.setZ(x);
        particleVector.divide(new Vector(1, 1, 1));

        Location particleLocation = particleVector.toLocation(this.world).add(player.getLocation()).add(0, 1.05, 0);

        for (int i = 0; i < 16; i++) {
            shootSingleWater(playerDirection, particleLocation);
        }

        //Light entity on fire
        LivingEntity hitEntity = getTarget(player);
        if(hitEntity != null){
            hitEntity.damage(5, player);
            hitEntity.setFireTicks(0);
            hitEntity.setVelocity(hitEntity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize());
        }
    }

    private void shootSingleWater(Vector playerDirection, Location particleLocation) {
        Vector particlePath = playerDirection.clone();

        particlePath.add(new Vector(Math.random()*10 - Math.random(), Math.random()*10 - Math.random(), Math.random()*10 - Math.random())); // Add some randomness

        Location offsetLocation = particlePath.toLocation(this.world);

        this.world.spawnParticle(Particle.ITEM_CRACK, particleLocation, 0, offsetLocation.getX(), offsetLocation.getY(), offsetLocation.getZ(), 0.1, new ItemStack(Material.BLUE_STAINED_GLASS));

    }

    public LivingEntity getTarget(Player player) {
        int range = 15;
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
}
