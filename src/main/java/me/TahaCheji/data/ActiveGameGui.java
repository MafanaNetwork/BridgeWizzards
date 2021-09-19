package me.TahaCheji.data;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.TahaCheji.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ActiveGameGui implements Listener {




    public PaginatedGui getGameGui() {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(ChatColor.GOLD + "Active Game"))
                .rows(6)
                .pageSize(54)
                .disableAllInteractions()
                .create();

        List<String> lore = new ArrayList<>();
        ItemStack greystainedglass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta newmeta = greystainedglass.getItemMeta();
        newmeta.setDisplayName(ChatColor.GRAY + " ");
        newmeta.setLore(lore);
        greystainedglass.setItemMeta(newmeta);

        gui.setItem(0, new GuiItem(greystainedglass));
        gui.setItem(1,new GuiItem(greystainedglass));
        gui.setItem(2,new GuiItem(greystainedglass));
        gui.setItem(3,new GuiItem(greystainedglass));
        gui.setItem(4,new GuiItem(greystainedglass));
        gui.setItem(5,new GuiItem(greystainedglass));
        gui.setItem(6,new GuiItem(greystainedglass));
        gui.setItem(7,new GuiItem(greystainedglass));
        gui.setItem(8,new GuiItem(greystainedglass));
        gui.setItem(17,new GuiItem(greystainedglass));
        gui.setItem(26,new GuiItem(greystainedglass));
        gui.setItem(35,new GuiItem(greystainedglass));
        gui.setItem(45,new GuiItem(greystainedglass));
        gui.setItem(53,new GuiItem(greystainedglass));
        gui.setItem(52,new GuiItem(greystainedglass));
        gui.setItem(51,new GuiItem(greystainedglass));
        gui.setItem(50,new GuiItem(greystainedglass));
        gui.setItem(48,new GuiItem(greystainedglass));
        gui.setItem(47,new GuiItem(greystainedglass));
        gui.setItem(46,new GuiItem(greystainedglass));
        gui.setItem(44,new GuiItem(greystainedglass));
        gui.setItem(36,new GuiItem(greystainedglass));
        gui.setItem(27,new GuiItem(greystainedglass));
        gui.setItem(18,new GuiItem(greystainedglass));
        gui.setItem(9,new GuiItem(greystainedglass));
        gui.setItem(49, new GuiItem(greystainedglass));
        gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName(ChatColor.DARK_GRAY + "Previous").asGuiItem(event -> gui.previous()));
        gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName(ChatColor.DARK_GRAY + "Next").asGuiItem(event -> gui.next()));

        for(Game game : Main.getInstance().getActiveGames()) {
            ItemStack itemStack = game.getGameIcon();
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.GOLD + game.getName());
            List<String> itemLore = new ArrayList<>();
            itemLore.add("");
            itemLore.add(ChatColor.GOLD + "Click To Join The Active Game");
            itemMeta.setLore(itemLore);
            itemStack.setItemMeta(itemMeta);
            gui.addItem(new GuiItem(itemStack));
        }
        return gui;
    }


    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(!event.getView().getTitle().contains("Active Game")) {
            return;
        }
        event.setCancelled(true);
        if(event.getCurrentItem() == null) {
            return;
        }
        if(event.getCurrentItem().getItemMeta() == null) {
            return;
        }
        if(event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE || event.getCurrentItem().getType() == Material.PAPER) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        GamePlayer gamePlayer = Main.getInstance().getPlayer(player);
        Game game = Main.getInstance().getGame(event.getCurrentItem().getItemMeta().getDisplayName());
        if(game == null) {
            return;
        }
        gamePlayer.setGame(game);
        game.joinAdmin(gamePlayer);

    }


}
