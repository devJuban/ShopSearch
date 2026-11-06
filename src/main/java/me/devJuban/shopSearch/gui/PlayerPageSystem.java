package me.devJuban.shopSearch.gui;

import me.gypopo.economyshopgui.api.EconomyShopGUIHook;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.objects.shops.ShopSection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlayerPageSystem {

    public final String title = ChatColor.DARK_GRAY + "§lSearch result";

    public final int back_button_offset = 1;
    public final int next_button_offset = 4;
    public final int nether_star_button_offset = 5;
    public final int previous_button_offset = 6;
    public final int head_button_offset = 9;

    private final Player player;
    private final ArrayList<ArrayList<ItemStack>> pages = new ArrayList<>();
    private int currentPage = 1;
    private final Inventory inv;

    private final ArrayList<ItemStack> itemStacks;

    public PlayerPageSystem(Player player, ArrayList<ItemStack> itemStacks){
        this.player = player;
        this.itemStacks = itemStacks;

        if (itemStacks.size() <= 45){
            int slots = (int) (Math.ceil((double) itemStacks.size() / 9) * 9) + 9;
            this.inv = Bukkit.createInventory(null, slots, title);

            pages.add(itemStacks);
        } else {
            this.inv = Bukkit.createInventory(null, 54, title);

            ArrayList<ItemStack> newPage = new ArrayList<>();
            for (int i = 0; i < itemStacks.size(); i++) {

                if (
                        (i + 1) % 46 == 0 ||
                        i == itemStacks.size() - 1)
                {
                    pages.add(new ArrayList<>(newPage));
                    newPage.clear();
                    continue;
                }

                newPage.add(itemStacks.get(i));
            }

            inv.setItem(getNextSlot(), getNextArrow());
        }

        inv.setItem(getBackSlot(), getBackButton());
        inv.setItem(getNetherStarSlot(), getNetherStar());
        inv.setItem(getHeadSlot(), getHead(player));
    }

    public void openFirstPage(){
        this.currentPage = 1;

        ArrayList<ItemStack> page = pages.get(0);

        for (int i = 0; i < page.size(); i++){
            inv.setItem(i, page.get(i));
        }

        if (currentPage != pages.size()){
            inv.setItem(getNextSlot(), getNextArrow());
        }

        player.openInventory(inv);
    }

    public void openNextPage(){
        if (currentPage + 1 > pages.size()) return;
        this.currentPage++;

        ArrayList<ItemStack> page = pages.get(currentPage - 1);

        for (int i = 0; i < page.size(); i++){
            inv.setItem(i, page.get(i));
        }

        for (int i = page.size(); i < inv.getSize() - 9; i++){
            inv.setItem(i, null);
        }

        inv.setItem(getNetherStarSlot(), getNetherStar());
        inv.setItem(getPreviousSlot(), getBackArrow());

        if (currentPage != pages.size()){
            inv.setItem(getNextSlot(), getNextArrow());
        } else inv.setItem(getNextSlot(), null);
    }

    public void openPreviousPage(){
        if (currentPage == 1) return;
        this.currentPage--;

        ArrayList<ItemStack> page = pages.get(currentPage - 1);

        for (int i = 0; i < page.size(); i++){
            inv.setItem(i, page.get(i));
        }

        for (int i = page.size(); i < inv.getSize() - 9; i++){
            inv.setItem(i, null);
        }

        inv.setItem(getNetherStarSlot(), getNetherStar());
        inv.setItem(getNextSlot(), getNextArrow());

        if (currentPage != 1){
            inv.setItem(getPreviousSlot(), getBackArrow());
        } else inv.setItem(getPreviousSlot(), null);
    }

    public int getBackSlot(){
        return inv.getSize() - back_button_offset;
    }

    public int getNextSlot(){
        return inv.getSize() - next_button_offset;
    }

    public int getPreviousSlot(){
        return inv.getSize() - previous_button_offset;
    }

    public int getNetherStarSlot(){
        return inv.getSize() - nether_star_button_offset;
    }

    public int getHeadSlot(){
        return inv.getSize() - head_button_offset;
    }

    public ArrayList<ItemStack> getItemStacks(){
        return itemStacks;
    }

    public void closeGUI(){
        player.closeInventory();
    }

    @Nullable
    private ItemStack getNetherStar(){
        ItemStack it = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = it.getItemMeta();
        if (meta == null) return null;
        meta.setDisplayName(ChatColor.GRAY + String.format("%d/%d", currentPage, pages.size()));
        it.setItemMeta(meta);
        return it;
    }

    @Nullable
    private ItemStack getHead(Player player){
        ItemStack it = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) it.getItemMeta();
        if (meta == null) return null;
        meta.setDisplayName(player.getDisplayName());
        meta.setLore(List.of("§6Balance: §c" + "balance"));

        PlayerProfile profile = Bukkit.createPlayerProfile(player.getName());
        PlayerTextures textures = profile.getTextures();

        profile.setTextures(textures);
        meta.setOwnerProfile(profile);

        it.setItemMeta(meta);
        return it;
    }

    @Nullable
    private ItemStack getBackButton(){
        ItemStack it = new ItemStack(Material.ARROW);
        ItemMeta meta = it.getItemMeta();
        if (meta == null) return null;
        meta.setDisplayName("§4Back");
        it.setItemMeta(meta);
        return it;
    }

    @Nullable
    private ItemStack getNextArrow(){
        ItemStack it = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta meta = it.getItemMeta();
        if (meta == null) return null;
        meta.setDisplayName("§bNext page");
        it.setItemMeta(meta);
        return it;
    }

    @Nullable
    private ItemStack getBackArrow(){
        ItemStack it = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta meta = it.getItemMeta();
        if (meta == null) return null;
        meta.setDisplayName("§dPrevious page");
        it.setItemMeta(meta);
        return it;
    }

    @SuppressWarnings("ConstantConditions")
    public static ArrayList<ItemStack> getSearch(String search){
        ArrayList<ItemStack> itemStacks = new ArrayList<>();

        for (String section : EconomyShopGUIHook.getShopSections()){
            ShopSection shopSection = EconomyShopGUIHook.getShopSection(section);

            for (ShopItem shopItem : shopSection.getShopItems()){
                ItemStack itemStack = new ItemStack(shopItem.getItemToGive());
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta == null) continue;

                itemMeta.setLore(getLore(shopItem, itemStack, itemMeta));
                itemStack.setItemMeta(itemMeta);

                String name = itemMeta.hasDisplayName() ? itemMeta.getDisplayName() : itemStack.getType().name();
                name = name.replace("_", " ");

                if (name.toUpperCase().contains(search.toUpperCase())) itemStacks.add(itemStack);
            }
        }
        return itemStacks;
    }

    private static ArrayList<String> getLore(ShopItem shopItem, ItemStack itemStack, ItemMeta itemMeta){
        String buy = String.valueOf(EconomyShopGUIHook.getItemBuyPrice(shopItem, 1));
        String sell = String.valueOf(EconomyShopGUIHook.getItemSellPrice(shopItem, itemStack));

        ArrayList<String> list = new ArrayList<>(List.of(
                "§a§lBuy Price",
                String.format("§a§l■ §f%s", buy.equals("-1.0") ? "Cannot be bought" : "$" + buy),
                "",
                "§c§lSell Price",
                String.format("§c§l■ §f%s", sell.equals("-1.0") ? "Cannot be sold" : "$" + sell)
        ));

        if (itemMeta.hasLore()){
            list.addAll(itemMeta.getLore());
        }

        return list;
    }
}
