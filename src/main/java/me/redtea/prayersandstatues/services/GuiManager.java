package me.redtea.prayersandstatues.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.AllArgsConstructor;
import me.redtea.prayersandstatues.tools.Message;
import me.redtea.prayersandstatues.types.God;
import me.redtea.prayersandstatues.types.Prayer;
import me.redtea.prayersandstatues.types.UpgradeTree;
import me.redtea.prayersandstatues.types.User;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Singleton
@AllArgsConstructor(onConstructor_ = @Inject)
public class GuiManager {

    private final ItemLocalizeUtil itemLocalizeUtil;

    private final DatabaseManager databaseManager;

    public void openGodGui(Player player, God god) {
        Gui gui = Gui.gui()
                .title(Component.text("X")) //TODO
                .rows(6)
                .create();
        gui.disableAllInteractions();
        for(UpgradeTree tree : god.getTrees()) {
            GuiItem guiItem = ItemBuilder.from(Objects.requireNonNull(Material.getMaterial(tree.getIcon()))).asGuiItem(
                    event -> { openTreeGui(player, tree, god);});
            ItemStack itemStack = guiItem.getItemStack();
            ItemMeta itemMeta = itemStack.getItemMeta();
            assert itemMeta != null;
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', tree.getName()));
            itemStack.setItemMeta(itemMeta);
            guiItem.setItemStack(itemStack);
            gui.setItem(tree.getSlot(), guiItem);
        }
        gui.open(player);
    }

    public void openTreeGui(Player player, UpgradeTree tree, God god) {
        Gui gui = Gui.gui()
                .title(Component.text("X")) //TODO
                .rows(3)
                .create();
        gui.disableAllInteractions();
        for(Prayer prayer : tree.getPrayers()) {
            GuiItem guiItem = ItemBuilder.from(Objects.requireNonNull(Material.getMaterial(tree.getIcon()))).asGuiItem(
                    event -> onPrayer(player, tree, god, prayer, gui));
            ItemStack itemStack = guiItem.getItemStack();
            ItemMeta itemMeta = itemStack.getItemMeta();
            assert itemMeta != null;
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', tree.getName() + " " + prayer.getLevel()));
            List<String> lore;
            User user = databaseManager.getUserData(player.getUniqueId().toString());
            if((!user.getData().isEmpty() && user.getData().get(god.getName()) != null && user.getData().get(god.getName()).get(tree.getTag()) != null && user.getData().get(god.getName()).get(tree.getTag()) == prayer.getLevel() -1) || (prayer.getLevel() == 1 && (user.getData().isEmpty() || user.getData().get(god.getName()) == null))) {
                var ref = new Object() {
                    String price = "";
                };
                prayer.getItems().entrySet().forEach((i) -> {
                    ref.price += (itemLocalizeUtil.getLocalizedName(i.getKey()) + " " + i.getValue()) + ", ";
                });
                ref.price = ref.price.substring(0, ref.price.length()-2);
                lore = Message.prayerOpenLore.replace("%price%", ref.price).toList();
            } else if(!user.getData().isEmpty() && user.getData().get(god.getName()).get(tree.getTag()) >= prayer.getLevel()) {
                lore = Message.prayerGotLore.toList();
            } else lore = Message.prayerLockedLore.toList();
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            guiItem.setItemStack(itemStack);
            gui.addItem(guiItem);
        }
        gui.open(player);
    }

    public void onPrayer(Player player, UpgradeTree tree, God god, Prayer prayer, Gui gui) {
        User user = databaseManager.getUserData(player.getUniqueId().toString());

        if((!user.getData().isEmpty() && user.getData().get(god.getName()).get(tree.getTag()) != null && user.getData().get(god.getName()).get(tree.getTag()) == prayer.getLevel() -1) || (prayer.getLevel() == 1 && (user.getData().isEmpty() || user.getData().get(god.getName()) == null))) {
            boolean success = true;
            for(Map.Entry<String, Integer> entry : prayer.getItems().entrySet()) {
                ItemStack itemStack = new ItemStack(Objects.requireNonNull(Material.getMaterial(entry.getKey())), entry.getValue());
                if(!player.getInventory().contains(Material.getMaterial(entry.getKey()))) {
                    success = false;
                } else {
                    for(ItemStack i : player.getInventory()) {
                        try {
                            if(i.getType().equals(Material.getMaterial(entry.getKey()))) {
                                if(i.getAmount() < entry.getValue()) success = false;
                            }
                        } catch (Throwable e) {}
                    }
                }
            }
            if(success) {
                for(Map.Entry<String, Integer> entry : prayer.getItems().entrySet()) {
                    player.getInventory().removeItem(new ItemStack[]{new ItemStack(Material.getMaterial(entry.getKey()), entry.getValue())});
                }
                Message.prayerSuccess.replace("%prayer%", tree.getName() + " " + prayer.getLevel()).send(player);
                user.setLevel(god.getName(), tree.getTag(), prayer.getLevel());
            } else Message.notEnoughItems.send(player);
        } else Message.prayerError.send(player);
        gui.close(player);
    }

}
