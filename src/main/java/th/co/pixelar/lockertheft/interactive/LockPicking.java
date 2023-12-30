package th.co.pixelar.lockertheft.interactive;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import th.co.pixelar.lockertheft.utilities.ComponentManager;

import static th.co.pixelar.lockertheft.LockerTheft.SERVER_INSTANCE;

public class LockPicking implements Listener {
    private final Inventory inv;
    public LockPicking() {
        inv = SERVER_INSTANCE.createInventory(null, 36, Component.text( ComponentManager.getStringOffset(-8) + "\uE401", ComponentManager.nonItalic(TextColor.color(255, 255, 255))));
        initializeItems();
    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {
    }



    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();

        // Using slots click is a best option for your inventory click's
        p.sendMessage("You clicked at slot " + e.getRawSlot());
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }
}
