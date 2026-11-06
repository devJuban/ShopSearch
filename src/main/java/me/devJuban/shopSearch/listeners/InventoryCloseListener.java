package me.devJuban.shopSearch.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.devJuban.shopSearch.ShopSearch.getPlayerPageSystems;

public class InventoryCloseListener implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        if (!getPlayerPageSystems().containsKey(event.getPlayer().getName())) return;
        getPlayerPageSystems().remove(event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        if (!getPlayerPageSystems().containsKey(event.getPlayer().getName())) return;
        getPlayerPageSystems().remove(event.getPlayer().getName());
    }
}
