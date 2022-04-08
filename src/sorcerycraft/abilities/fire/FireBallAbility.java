package sorcerycraft.abilities.fire;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import sorcerycraft.abilities.Ability;
import sorcerycraft.data.PlayerData;

public class FireBallAbility extends Ability {

    private final int coolDown = 20*5;

    @Override
    public void doAbility() {
        if (playerData.cooldownFireBall <= 0){
            Fireball fireBall = player.launchProjectile(Fireball.class);
            fireBall.setIsIncendiary(false);
            fireBall.setBounce(false);
            fireBall.setYield(2);
            fireBall.setShooter(player);

            playerData.cooldownFireBall = coolDown;
        }
        else{
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GRAY + "Cooldown: " + playerData.cooldownFireBall/20 + "s"));
        }
    }
}
