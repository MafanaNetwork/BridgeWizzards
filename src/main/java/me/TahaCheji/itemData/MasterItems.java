package me.TahaCheji.itemData;

import de.tr7zw.nbtapi.NBTItem;
import me.TahaCheji.Main;
import me.TahaCheji.util.ItemUtil;
import me.TahaCheji.util.NBTUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class MasterItems {

    private final String name;
    private final Material itemMaterial;
    private final ItemType itemType;
    private final RarityType rarityType;
    private final boolean glow;
    private MasterAbility masterAbility;
    private final List<String> lore;
    private boolean oneTimeUse;
    private int UUID;


    public MasterItems(String name, Material itemMaterial, ItemType itemType, RarityType rarityType, boolean glow, MasterAbility masterAbility, boolean oneTimeUse, String... lore) {
        this.name = name;
        this.itemMaterial = itemMaterial;
        this.itemType = itemType;
        this.rarityType = rarityType;
        this.glow = glow;
        this.masterAbility = masterAbility;
        this.lore = Arrays.asList(lore);
        this.UUID = ItemUtil.stringToSeed(itemMaterial.name() + name + rarityType.toString());
        this.oneTimeUse = oneTimeUse;
        Main.allItems.add(this);
        Main.putItem(name, this);
    }

    public MasterItems(String name, Material itemMaterial, ItemType itemType, RarityType rarityType, boolean glow, String... lore) {
        this.name = name;
        this.itemMaterial = itemMaterial;
        this.itemType = itemType;
        this.rarityType = rarityType;
        this.glow = glow;
        this.lore = Arrays.asList(lore);
        this.UUID = ItemUtil.stringToSeed(itemMaterial.name() + name + rarityType.toString());
    }

    public boolean compare(ItemStack other) {
        int otherUUID = ItemUtil.getIntFromItem(other, "MasterUUID");
        return otherUUID == this.UUID;
    }


    public ItemStack getItem() {
        ItemStack item = new ItemStack(itemMaterial);
        ItemMeta meta = item.getItemMeta();
        List<String> list = new ArrayList<>();
        if (name != null || rarityType != null) {
            meta.setDisplayName(rarityType.getColor() + name);
        }
        if (lore != null) {
            if (masterAbility != null) {
                list.addAll(masterAbility.toLore());
            }
            list.add("");
            list.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Item Lore:");
            for (String string : lore) {
                list.add('"' + string + '"');
            }
            list.add("");
            list.add(rarityType.getLore() + itemType.getLore());
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        if (glow) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
        }
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setLore(list);
        item.setItemMeta(meta);
        if (masterAbility != null) {
            item = NBTUtils.setBoolean(item, "hasAbility", true);
        } else {
            item = NBTUtils.setBoolean(item, "hasAbility", false);
        }
        item = NBTUtils.setString(item, "ItemKey", item.getItemMeta().getDisplayName());
        item = NBTUtils.setString(item, "ItemType", itemType.getLore());
        item = NBTUtils.setString(item, "ItemRarity", rarityType.getLore());
        if (masterAbility != null) {
            item = NBTUtils.setInt(item, "ManaCost", masterAbility.getManaCost());
            item = NBTUtils.setString(item, "AbilityName", masterAbility.getName());
            item = NBTUtils.setInt(item, "AbilityDamage", masterAbility.getAbilityDamage());
        }
        item = ItemUtil.storeIntInItem(item, this.UUID, "MasterUUID");
        return item;

    }

    public void onItemUse(Player player, ItemStack item) {
        if (this.oneTimeUse && player.getGameMode() != GameMode.CREATIVE) {
            destroy(item, 1);
        }
    }

    public static void destroy(ItemStack item, int quantity) {
        if (item.getAmount() <= quantity) {
            item.setAmount(0);
        } else {
            item.setAmount(item.getAmount() - quantity);
        }

    }

    public abstract void onItemStackCreate(ItemStack var1);

    public abstract boolean leftClickAirAction(Player var1, ItemStack var2);

    public abstract boolean leftClickBlockAction(Player var1, PlayerInteractEvent var2, Block var3, ItemStack var4);

    public abstract boolean rightClickAirAction(Player var1, ItemStack var2);

    public abstract boolean rightClickBlockAction(Player var1, PlayerInteractEvent var2, Block var3, ItemStack var4);

    public abstract boolean shiftLeftClickAirAction(Player var1, ItemStack var2);

    public abstract boolean shiftLeftClickBlockAction(Player var1, PlayerInteractEvent var2, Block var3, ItemStack var4);

    public abstract boolean shiftRightClickAirAction(Player var1, ItemStack var2);

    public abstract boolean shiftRightClickBlockAction(Player var1, PlayerInteractEvent var2, Block var3, ItemStack var4);

    public abstract boolean middleClickAction(Player var1, ItemStack var2);

    public abstract boolean hitEntityAction(Player var1, EntityDamageByEntityEvent var2, Entity var3, ItemStack var4);

    public abstract boolean breakBlockAction(Player var1, BlockBreakEvent var2, Block var3, ItemStack var4);

    public abstract boolean clickedInInventoryAction(Player var1, InventoryClickEvent var2, ItemStack var3, ItemStack var4);

    public int getUUID() {
        return this.UUID;
    }

    public String getName() {
        return name;
    }

    public Material getItemMaterial() {
        return itemMaterial;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public RarityType getRarityType() {
        return rarityType;
    }

    public boolean isGlow() {
        return glow;
    }

    public MasterAbility getMasterAbility() {
        return masterAbility;
    }

    public List<String> getLore() {
        return lore;
    }
}
