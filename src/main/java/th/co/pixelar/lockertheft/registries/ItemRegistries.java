package th.co.pixelar.lockertheft.registries;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import th.co.pixelar.lockertheft.utilities.ComponentManager;

public class ItemRegistries {
    public static ItemStack LOCK = itemLock();
    public static ItemStack LOCK_PICKER = itemLockPicker();
    public static ItemStack KEY = itemKey();
    public static ItemStack SCREW_DRIVER = itemScrewDriver();
    private static ItemStack itemLock() {
        ItemStack itemLock = new ItemStack(Material.FLINT);
        ItemMeta itemMeta = itemLock.getItemMeta();
        itemMeta.setCustomModelData(1);
        itemMeta.displayName(Component.text("Lock", ComponentManager.nonItalic(TextColor.color(255, 255, 255))));
        itemLock.setItemMeta(itemMeta);

        return itemLock;
    }

    private static ItemStack itemLockPicker() {
        ItemStack itemPicker = new ItemStack(Material.FLINT);
        ItemMeta itemMeta = itemPicker.getItemMeta();
        itemMeta.setCustomModelData(101);
        itemMeta.displayName(Component.text("Lock Picker", ComponentManager.nonItalic(TextColor.color(255, 255, 255))));
        itemPicker.setItemMeta(itemMeta);

        return itemPicker;
    }

    private static ItemStack itemKey() {
        ItemStack itemKey = new ItemStack(Material.FLINT);
        ItemMeta itemMeta = itemKey.getItemMeta();
        itemMeta.setCustomModelData(201);
        itemMeta.displayName(Component.text("Key", ComponentManager.nonItalic(TextColor.color(255, 255, 255))));
        itemKey.setItemMeta(itemMeta);

        return itemKey;
    }

    private static ItemStack itemScrewDriver() {
        ItemStack itemKey = new ItemStack(Material.WOODEN_HOE);
        ItemMeta itemMeta = itemKey.getItemMeta();
        itemMeta.setCustomModelData(1);
        itemMeta.displayName(Component.text("Key", ComponentManager.nonItalic(TextColor.color(255, 255, 255))));
        itemKey.setItemMeta(itemMeta);

        return itemKey;
    }


}
