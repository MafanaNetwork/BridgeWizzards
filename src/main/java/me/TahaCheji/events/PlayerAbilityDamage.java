package me.TahaCheji.events;

import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import de.tr7zw.nbtapi.NBTItem;
import me.TahaCheji.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerAbilityDamage implements Listener {


    @EventHandler
    public void onDamageHit(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof Projectile)) {
            e.setCancelled(true);
            Player player = (Player) e.getDamager();
            player.sendMessage(ChatColor.RED + "You can only do damage with ability");
            return;
        }
        Projectile projectile = (Projectile) e.getDamager();
        Player player = (Player) projectile.getShooter();
        if(player == null) {
            return;
        }
        ItemStack item = player.getInventory().getItemInMainHand();

        if(item.getItemMeta() == null) {
            return;
        }
        if(new NBTItem(item).getBoolean("hasAbility")) {
            int damage = new NBTItem(item).getInteger("AbilityDamage") / 5;
            e.setDamage(damage);
            com.gmail.filoghost.holograms.api.Hologram h = (com.gmail.filoghost.holograms.api.Hologram) HolographicDisplaysAPI.createHologram
                    (Main.getInstance(), e.getEntity().getLocation().add(getRandomOffset(), 2, getRandomOffset()),  ChatColor.DARK_PURPLE + "✧" + damage + "✧" );
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                h.delete();
            }, 20); // Time in ticks (20 ticks = 1 second)
        } else {
            com.gmail.filoghost.holograms.api.Hologram h = (com.gmail.filoghost.holograms.api.Hologram) HolographicDisplaysAPI.createHologram
                    (Main.getInstance(), e.getEntity().getLocation().add(getRandomOffset(), 2, getRandomOffset()),  ChatColor.RED + "" + e.getDamage());
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                h.delete();
            }, 20); // Time in ticks (20 ticks = 1 second)
        }
    }


    private double getRandomOffset() {
        double random = Math.random();
        if (Math.random() > 0.5) random *= -1;
        return random;
    }

}
