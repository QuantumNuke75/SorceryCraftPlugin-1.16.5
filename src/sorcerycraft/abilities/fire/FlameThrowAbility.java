package sorcerycraft.abilities.fire;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import sorcerycraft.abilities.Ability;
import sorcerycraft.abilities.AbilityUpdater;

import java.util.ArrayList;
import java.util.List;

public class FlameThrowAbility extends Ability {

    private final int coolDown = 20*5;
    private final int flameTime = 20*3;

    @Override
    public void doAbility() {
        if (playerData.cooldownFlameThrow <= 0){

            AbilityUpdater.abilities.add(this);

            playerData.cooldownFlameThrow = coolDown;
        }
        else{
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GRAY + "Cooldown: " + playerData.cooldownFlameThrow /20 + "s"));
        }
    }

    @Override
    public void update() {
        if (this.timer < flameTime) {
            if(player != null) {
                shootFire();
                world.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.5f, 1);
            }
        }
        else{
            this.isScheduledForRemoved = true;
        }
        this.timer++;
    }

    private void shootFire() {
        Vector playerDirection = player.getLocation().getDirection();
        Vector particleVector = playerDirection.clone();

        playerDirection.multiply(8);

        double x = particleVector.getX();
        particleVector.setX(-particleVector.getZ());
        particleVector.setZ(x);
        particleVector.divide(new Vector(3, 3, 3));

        Location particleLocation = particleVector.toLocation(this.world).add(player.getLocation()).add(0, 1.05, 0);

        for (int i = 0; i < 6; i++) {
            shootSingleFlame(playerDirection, particleLocation);
        }

        //Light entity on fire
        LivingEntity hitEntity = getTarget(player);
        if(hitEntity != null){
            hitEntity.setFireTicks(40);
        }

        if (Math.random() < 0.40) {
            Block lookingBlock = player.getTargetBlock(null, 15);
            if (lookingBlock != null && lookingBlock.getType().isBlock()) {
                Block upBlock = lookingBlock.getRelative(BlockFace.UP);
                if (upBlock != null && (upBlock.getType() == Material.AIR || upBlock.getType() == Material.CAVE_AIR)) {
                    upBlock.setType(Material.FIRE);
                }
            }
        }
    }

    private void shootSingleFlame(Vector playerDirection, Location particleLocation) {
        Vector particlePath = playerDirection.clone();

        particlePath.add(new Vector(Math.random() - Math.random(), Math.random() - Math.random(), Math.random() - Math.random()));

        Location offsetLocation = particlePath.toLocation(this.world);
        this.world.spawnParticle(Particle.FLAME, particleLocation, 0, offsetLocation.getX(), offsetLocation.getY(), offsetLocation.getZ(), 0.1);
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
