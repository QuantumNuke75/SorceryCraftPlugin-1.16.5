package sorcerycraft.entity;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.util.Vector;
import sorcerycraft.data.PlayerData;
import sorcerycraft.main.SorceryCraft;

import java.util.List;


public class CustomFallingBlock extends EntityFallingBlock {

    public boolean canThrow = false;
    public boolean shouldDamageEntities = false;
    public Player thrower;

    public CustomFallingBlock(Location location, IBlockData iBlockData) {

        super(((CraftWorld) location.getWorld()).getHandle(), location.getX() + 0.5, location.getY() + 2.5, location.getZ() + 0.5, iBlockData);
    }

    @Override
    public void tick() {
        super.tick();
        if(shouldDamageEntities) {
            List<org.bukkit.entity.Entity> entities = this.getBukkitEntity().getNearbyEntities(1, 1, 1);

            for (org.bukkit.entity.Entity entity : entities) {
                if (entity instanceof LivingEntity) {
                    ((LivingEntity) entity).damage(10f, thrower);
                }
            }
        }
    }

    @Override
    public EnumInteractionResult a(EntityHuman entityhuman, Vec3D vec3d, EnumHand enumhand) {

        PlayerData playerData = SorceryCraft.getPlugin(SorceryCraft.class).playerDataMap.get(entityhuman.getUniqueID());


        if(playerData.guild == "earth" && canThrow){
            Vector vector = entityhuman.getBukkitEntity().getLocation().getDirection().normalize().multiply(2);
            this.setNoGravity(false);
            this.velocityChanged = true;
            this.setMot(vector.getX(), vector.getY(),vector.getZ());
            this.shouldDamageEntities = true;

        }
        return EnumInteractionResult.SUCCESS;
    }

    public void spawn() {
        this.ticksLived = 1;
        this.world.addEntity(this, SpawnReason.CUSTOM);
        this.setNoGravity(true);
        this.dropItem = false;
    }
}