package me.devJuban.shopSearch.command;

import me.devJuban.shopSearch.gui.PlayerPageSystem;
import me.gypopo.economyshopgui.api.EconomyShopGUIHook;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.devJuban.shopSearch.ShopSearch.getPlayerPageSystems;

public class CommandManager implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String @NotNull [] args) {
        if (sender instanceof ConsoleCommandSender){
            if (args.length == 2) Bukkit.dispatchCommand(sender, String.format("economyshopgui:shop %s %s", args[0], args[1]));
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0){
            player.performCommand("economyshopgui:shop");
            return true;
        }

        if ((args.length == 1 && args[0].equalsIgnoreCase("search"))){
            if (!player.hasPermission("EconomyShopGUI.shop.search")){
                player.sendMessage("§9§lShop §8§l>> §cYou do not have permissions for that.");
                return true;
            }

            player.sendMessage("§9§lShop §8§l>> §r/shop search [search]");
            return true;
        }

        if ((args.length == 1 && !args[0].equalsIgnoreCase("search"))){
            player.performCommand(String.format("economyshopgui:shop %s", args[0]));
            return true;
        }

        if ((args.length >= 2 && args[0].equalsIgnoreCase("search"))){
            if (!player.hasPermission("EconomyShopGUI.shop.search")){
                player.sendMessage("§9§lShop §8§l>> §cYou do not have permissions for that.");
                return true;
            }

            StringBuilder search = new StringBuilder();
            for (int i = 1; i < args.length - 1; i++){
                search.append(args[i]).append(" ");
            }
            search.append(args[args.length - 1]);

            ArrayList<ItemStack> itemStacks = PlayerPageSystem.getSearch(search.toString());

            if (itemStacks.isEmpty()){
                player.sendMessage(String.format("§9§lShop §8§l>> §cFailed to find any item with name: %s.", search));
            } else {
                PlayerPageSystem system = new PlayerPageSystem(player, itemStacks);
                getPlayerPageSystems().putIfAbsent(player.getName(), system);

                system.openFirstPage();
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1){
            ArrayList<String> tab = new ArrayList<>();

            if (sender.hasPermission("EconomyShopGUI.shop.search")){
                tab.add("search");
            }

            for (String section : EconomyShopGUIHook.getShopSections()){
                if (sender.hasPermission("EconomyShopGUI.shop." + section)){
                    tab.add(section);
                }
            }

            tab.addAll(EconomyShopGUIHook.getShopSections());
            return tab;
        } else return Collections.emptyList();
    }

}
