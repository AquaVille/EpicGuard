package io.github.polskistevek.epicguard.bukkit.gui;

import io.github.polskistevek.epicguard.bukkit.manager.UserManager;
import io.github.polskistevek.epicguard.bukkit.object.User;
import io.github.polskistevek.epicguard.bukkit.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import io.github.polskistevek.epicguard.bukkit.GuardBukkit;
import io.github.polskistevek.epicguard.utils.ChatUtil;
import io.github.polskistevek.epicguard.utils.GeoAPI;
import io.github.polskistevek.epicguard.utils.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuiPlayers {
    public static Inventory inv;

    public static void show(Player p) {
        try {
            int i = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {
                final ItemStack itemStack;
                final List<String> lore = new ArrayList<>();
                final User user = UserManager.getUser(player);
                lore.add("");
                lore.add(ChatUtil.fix("&8>> &7UUID: &a" + player.getUniqueId()));
                lore.add(ChatUtil.fix("&8>> &7First Join: &a" + new Date(player.getFirstPlayed())));
                lore.add(ChatUtil.fix("&8>> &7Country: &a" + GeoAPI.getDatabase().country(player.getAddress().getAddress()).getCountry().getIsoCode()));
                lore.add(ChatUtil.fix("&8>> &7OP: " + (player.isOp() ? "&aYes" : "&cNo")));
                lore.add(ChatUtil.fix("&8>> &7Client Brand: &f" + user.getBrand()));
                if (GuardBukkit.IP_HISTORY_ENABLE) {
                    if (user.getAdresses() != null) {
                        lore.add("");
                        lore.add(ChatUtil.fix("&8>> &7IP History:"));
                        for (String adress : user.getAdresses()) {
                            if (player.getAddress().getAddress().getHostAddress().equals(adress)) {
                                lore.add(ChatUtil.fix("&8-> &2" + adress + " &8(&aCurrent&8)"));
                            } else {
                                lore.add(ChatUtil.fix("&8-> &2" + adress));
                            }
                        }
                    }
                }
                if (player.isOp()) {
                    itemStack = new ItemBuilder(Material.DIAMOND_HELMET).setTitle("&8>> &c" + player.getName()).addLores(lore).build();
                } else {
                    itemStack = new ItemBuilder(Material.IRON_HELMET).setTitle("&8>> &a" + player.getName()).addLores(lore).build();
                }
                ItemStack back = new ItemBuilder(Material.ARROW).setTitle("&cBack to main menu").addLore("").addLore("&7Click to go back.").build();
                inv.setItem(i, itemStack);
                inv.setItem(35, back);
                i++;
            }
            p.openInventory(inv);
        } catch (Exception e) {
            Logger.throwException(e);
        }
    }
}
