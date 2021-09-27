package me.TahaCheji.events;

import me.TahaCheji.Main;
import me.TahaCheji.gameData.GamePlayer;
import me.TahaCheji.itemData.MasterItems;
import me.TahaCheji.util.ItemUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerUseMasterItem implements Listener {
    public PlayerUseMasterItem() {
    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    private void onPlayerUse(PlayerInteractEvent event) {
        GamePlayer gamePlayer = Main.getInstance().getPlayer(event.getPlayer());
        if (Main.getInstance().isInGame(gamePlayer.getPlayer()) || gamePlayer.getPlayer().isOp()) {
            if (ItemUtil.isMasterItem(event.getPlayer().getInventory().getItemInMainHand())) {
                this.useMasterItem(event, event.getPlayer().getInventory().getItemInMainHand());
            }
        } else {
            gamePlayer.getPlayer().sendMessage("You Cannot use this item out side of the game.");
        }
    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    private void onPlayerHit(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.PLAYER) {
            Player player = (Player) event.getDamager();
            ItemStack mainhand = player.getInventory().getItemInMainHand();
            ItemStack offhand = player.getInventory().getItemInOffHand();
            MasterItems uber;
            if (ItemUtil.isMasterItem(mainhand)) {
                uber = ItemUtil.getMasterItem(mainhand);
                if (uber != null) {

                    if (uber.hitEntityAction(player, event, event.getEntity(), mainhand)) {
                        uber.onItemUse(player, mainhand);
                    }
                }
            }

            if (ItemUtil.isMasterItem(offhand)) {
                uber = ItemUtil.getMasterItem(offhand);
                if (uber != null) {

                    if (uber.hitEntityAction(player, event, event.getEntity(), offhand)) {
                        uber.onItemUse(player, offhand);
                    }
                }
            }

        }
    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (ItemUtil.isMasterItem(item)) {
            MasterItems uber = ItemUtil.getMasterItem(item);
            if (uber != null) {
                if (uber.breakBlockAction(player, event, event.getBlock(), item)) {
                    uber.onItemUse(player, item);
                }
            }
        }
    }


    private void useMasterItem(PlayerInteractEvent event, ItemStack item) {
        Player player = event.getPlayer();
        MasterItems uber = ItemUtil.getMasterItem(item);
        if (uber != null) {
            if (event.getAction() == Action.LEFT_CLICK_AIR) {
                if (!player.isSneaking()) {
                    if (uber.leftClickAirAction(player, item)) {
                        uber.onItemUse(player, item);
                    }
                } else if (uber.shiftLeftClickAirAction(player, item)) {
                    uber.onItemUse(player, item);
                }
            } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {

                if (!player.isSneaking()) {
                    if (uber.leftClickBlockAction(player, event, event.getClickedBlock(), item)) {
                        uber.onItemUse(player, item);
                    }
                } else if (uber.shiftLeftClickBlockAction(player, event, event.getClickedBlock(), item)) {
                    uber.onItemUse(player, item);
                }
            } else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                if (!player.isSneaking()) {
                    if (uber.rightClickAirAction(player, item)) {
                        uber.onItemUse(player, item);
                    }
                } else if (uber.shiftRightClickAirAction(player, item)) {
                    System.out.println("2");
                    uber.onItemUse(player, item);
                }
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                if (!player.isSneaking()) {
                    if (uber.rightClickBlockAction(player, event, event.getClickedBlock(), item)) {
                        uber.onItemUse(player, item);
                    }
                } else if (uber.shiftRightClickBlockAction(player, event, event.getClickedBlock(), item)) {
                    uber.onItemUse(player, item);
                }
            }
        }
    }
}
