package me.TahaCheji.itemData;

public enum ItemType {

    SWORD("Sword"),
    TOOL("Tool"),
    BOW("Bow"),
    ITEM("Item"),
    BOOTS("Boots"),
    LEGGGINGS("Leggings"),
    CHESTPLATE("Chestplate"),
    HELMET("Helmet"),
    SPELL("Spell"),
    MATERIAL( "Material");


    private final String lore;

    ItemType(String lore) {
        this.lore = lore;
    }

    public String getLore() {
        return lore;
    }



}
