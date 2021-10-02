package me.TahaCheji.tasks;

import me.TahaCheji.Main;
import me.TahaCheji.gameData.Game;
import me.TahaCheji.gameData.GamePlayer;
import me.TahaCheji.playerData.PlayerLocation;
import me.TahaCheji.gameItems.*;
import me.TahaCheji.util.AbilityUtil;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;


public class GameRunTask extends BukkitRunnable {

    private Game game;
    private int startIn = 10;
    ActiveGameTask gameTask;

    public GameRunTask(Game game) {
        this.game = game;
        this.game.setState(Game.GameState.PREPARATION);
        for (GamePlayer gamePlayer : game.getPlayers()) {
            gamePlayer.setPlayerLocation(PlayerLocation.GAMELOBBY);
            gamePlayer.getPlayer().setGameMode(GameMode.ADVENTURE);
            gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 2, 1);
        }
        this.game.assignSpawnPositions();
        this.game.sendMessage(ChatColor.GOLD + "[Game Manager] "  + "You've been teleported.");
        this.game.sendMessage(ChatColor.GOLD + "[Game Manager] " + "The game will begin in " + this.startIn + " seconds...");
        this.game.setMovementFrozen(true);
    }

    @Override
    public void run() {
        if (startIn <= 1) {
            this.cancel();
            this.game.setState(Game.GameState.ACTIVE);
            this.game.sendMessage(ChatColor.GOLD + "[Game Manager] " +"The game has started.");
            this.game.setMovementFrozen(false);
            for(GamePlayer gamePlayer : game.getPlayers()) {

                ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
                ItemMeta helm = helmet.getItemMeta();
                ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
                ItemMeta chest = chestplate.getItemMeta();
                ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
                ItemMeta legg = leggings.getItemMeta();
                ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
                ItemMeta boot = boots.getItemMeta();

                helm.setDisplayName(ChatColor.YELLOW + "Helmatron");
                helm.addItemFlags();
                helm.setUnbreakable(true);
                helm.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                helm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                helmet.setItemMeta(helm);

                chest.setDisplayName(ChatColor.YELLOW + "Chestmatron");
                chest.addEnchant(Enchantment.DURABILITY, 5, true);
                chest.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                chest.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                chestplate.setItemMeta(chest);

                legg.setDisplayName(ChatColor.YELLOW + "Leggmatron");
                legg.addEnchant(Enchantment.DURABILITY, 5, true);
                legg.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                legg.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                leggings.setItemMeta(legg);

                boot.setDisplayName(ChatColor.YELLOW + "Bootmatron");
                boot.addEnchant(Enchantment.DURABILITY, 5, true);
                boot.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                boot.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                boots.setItemMeta(boot);


                ItemStack blocks = new ItemStack(Material.WHITE_TERRACOTTA, 64);

                ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
                ItemMeta meta3 = pickaxe.getItemMeta();

                meta3.setDisplayName(ChatColor.GREEN + "DIGO-3000");
                meta3.addEnchant(Enchantment.DURABILITY, 5, true);
                meta3.addEnchant(Enchantment.DIG_SPEED, 3, true);
                meta3.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta3.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                meta3.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                pickaxe.setItemMeta(meta3);

                Main.getInstance().setGame(gamePlayer.getPlayer(), game);
                gamePlayer.setPlayerLocation(PlayerLocation.GAME);
                gamePlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
                Earthquake earthquake = new Earthquake();
                earthquake.setGamePlayer(gamePlayer);

                WandOfRespiration wandOfRespiration = new WandOfRespiration();
                wandOfRespiration.setGamePlayer(gamePlayer);
                wandOfRespiration.getMasterAbility().setAbilityDamage(new AbilityUtil().abilityDamage(wandOfRespiration, gamePlayer.getLevels()));

                LightningWand lightningWand = new LightningWand();
                lightningWand.setGamePlayer(gamePlayer);
                lightningWand.getMasterAbility().setAbilityDamage(new AbilityUtil().abilityDamage(lightningWand, gamePlayer.getLevels()));

                MeteorStaff meteorStaff = new MeteorStaff();
                meteorStaff.setGamePlayer(gamePlayer);
                meteorStaff.getMasterAbility().setAbilityDamage(new AbilityUtil().abilityDamage(meteorStaff, gamePlayer.getLevels()));

                ShadowWarp shadowWarp = new ShadowWarp();
                shadowWarp.setGamePlayer(gamePlayer);

                Gapple gapple = new Gapple();
                gapple.setGamePlayer(gamePlayer);

                ItemStack gappls = gapple.getItem();
                gappls.setAmount(4);

                ItemStack shadow = shadowWarp.getItem();
                shadow.setAmount(2);


                Player player = gamePlayer.getPlayer();

                player.getInventory().setArmorContents(null);
                player.getInventory().setHelmet(helmet);
                player.getInventory().setChestplate(chestplate);
                player.getInventory().setLeggings(leggings);
                player.getInventory().setBoots(boots);

                player.getInventory().setItem(0, wandOfRespiration.getItem());
                player.getInventory().setItem(1, lightningWand.getItem());
                player.getInventory().setItem(2, meteorStaff.getItem());
                player.getInventory().setItem(3, blocks);
                player.getInventory().setItem(4, blocks);
                player.getInventory().setItem(5, pickaxe);
                player.getInventory().setItem(6, gappls);
                player.getInventory().setItem(7, shadow);
                player.getInventory().setItem(8, earthquake.getItem());
                player.updateInventory();
            }
            gameTask = new ActiveGameTask(game, game.getGameTime());
            gameTask.runTaskTimer(Main.getInstance(), 0, 20);
        } else {
            startIn -= 1;
            this.game.sendMessage(ChatColor.GOLD + "[Game Manager] " + "The game will begin in " + startIn + " second" + (startIn == 1 ? "" : "s"));
        }
    }

    public ActiveGameTask getGameTask() {
        return gameTask;
    }
}