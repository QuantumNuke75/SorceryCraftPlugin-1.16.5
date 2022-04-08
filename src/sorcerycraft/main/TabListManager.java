package sorcerycraft.main;

import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TabListManager {

    /**
     * The current instance of the running plugin.
     */
    private SorceryCraft sc = SorceryCraft.getPlugin(SorceryCraft.class);

    boolean titleChanged = false;

   public TabListManager(){}

    /**
     * Send tab list packet to all players.
     */
    public void startTabListSync(){
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        new BukkitRunnable() {

            @Override
            public void run() {
                if (titleChanged) {
                    packet.header = new ChatComponentText(ChatColor.RED + "SorceryCraft\n" + ChatColor.GRAY + "Welcome!");
                    titleChanged = false;

                } else {
                    packet.header = new ChatComponentText(ChatColor.RED + "SorceryCraft\n" + ChatColor.GRAY + "Alpha");
                    titleChanged = true;
                }
                packet.footer = new ChatComponentText(ChatColor.GRAY+ "Players Online: " + ChatColor.RED + Bukkit.getServer().getOnlinePlayers().size());;

                if (Bukkit.getOnlinePlayers().size() == 0) return;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                }
            }
        }.runTaskTimer(sc, 0, 20);
    }

}
