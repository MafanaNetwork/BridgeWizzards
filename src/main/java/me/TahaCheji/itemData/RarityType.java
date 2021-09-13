package me.TahaCheji.itemData;

import org.bukkit.ChatColor;

public enum RarityType {

    WOOD(ChatColor.GRAY, ChatColor.GRAY+ "Wooden "),
    COAL(ChatColor.GRAY, ChatColor.GRAY + "Coaled "),
    IRON(ChatColor.WHITE, ChatColor.WHITE + "Irony "),
    LAPIS(ChatColor.AQUA, ChatColor.AQUA + "Lapsed "),
    GOLD(ChatColor.GOLD, ChatColor.GOLD + "Golden "),
    REDSTONE(ChatColor.RED, ChatColor.RED + "Redstone "),
    DIAMOND(ChatColor.BLUE, ChatColor.BLUE + "Diamond "),
    OBSIDAIN(ChatColor.DARK_GRAY, ChatColor.DARK_GRAY + "Obsidian "),
    ADMIN(ChatColor.DARK_RED, ChatColor.RED + "Admired ");

    private final ChatColor color;

    private final String lore;

    RarityType(ChatColor color, String lore) {
        this.color = color;
        this.lore = lore;
    }


    public ChatColor getColor() {
        return color;
    }

    public String getLore() {
        return lore;
    }

}
