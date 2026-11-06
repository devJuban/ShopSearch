package me.devJuban.shopSearch;

import me.devJuban.shopSearch.command.CommandManager;
import me.devJuban.shopSearch.gui.PlayerPageSystem;
import me.devJuban.shopSearch.listeners.InventoryClickListener;
import me.devJuban.shopSearch.listeners.InventoryCloseListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class ShopSearch extends JavaPlugin {

    private static ShopSearch instance;

    public static final HashMap<String, PlayerPageSystem> playerPageSystems = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        // Register Events
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), this);

        // Configure command
        CommandManager manager = new CommandManager();
        PluginCommand command = getCommand("shop");
        if (command == null){ setEnabled(false); return; }

        command.setExecutor(manager);
        command.setTabCompleter(manager);

        sendMessage("[ShopSearch] &aSuccessfully enabled ShopSearch!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ShopSearch getInstance(){
        return instance;
    }
    public static HashMap<String, PlayerPageSystem> getPlayerPageSystems() {
        return playerPageSystems;
    }
    public static void sendMessage(String msg){
        Bukkit.getConsoleSender().sendMessage(msg.replace("&", "§"));
    }
}
