package me.devJuban.shopSearch.listeners;

import me.devJuban.shopSearch.gui.PlayerPageSystem;
import me.gypopo.economyshopgui.api.EconomyShopGUIHook;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.util.Transaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import static me.devJuban.shopSearch.ShopSearch.*;

public class InventoryClickListener implements Listener {
    @EventHandler
    @SuppressWarnings("ConstantConditions")
    public void onInventoryClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();

        if (getPlayerPageSystems().containsKey(player.getName())){
            event.setCancelled(true);

            Inventory inv = event.getClickedInventory();
            if (inv == null) return;
            if (inv instanceof PlayerInventory) return;
            ItemStack item = event.getCurrentItem();
            int slot = event.getSlot();
            int slotOffset = inv.getSize();

            PlayerPageSystem system = getPlayerPageSystems().get(player.getName());

            if (slot < slotOffset - 9){
                Transaction.Mode mode = event.getClick().isRightClick() ? Transaction.Mode.SELL : Transaction.Mode.BUY;
                ShopItem shopItem = EconomyShopGUIHook.getShopItem(player, item);
                EconomyShopGUIHook.openTransactionScreen(player, shopItem, mode);
                return;
            }

            // back button
            if (slot == system.getBackSlot()){
                system.closeGUI();
                return;
            }

            // nether star
            if (slot == system.getNetherStarSlot()){
                system.closeGUI();
                player.performCommand("economyshopgui:shop");
                return;
            }

            // next button
            if (slot == system.getNextSlot()){
                system.openNextPage();
                return;
            }

            // previous button
            if (slot == system.getPreviousSlot()){
                system.openPreviousPage();
            }

        }
    }
}
