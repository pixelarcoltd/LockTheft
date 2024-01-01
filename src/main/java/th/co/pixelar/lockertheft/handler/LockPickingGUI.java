package th.co.pixelar.lockertheft.handler;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import th.co.pixelar.lockertheft.registries.ItemRegistries;
import th.co.pixelar.lockertheft.utilities.ComponentManager;

import static th.co.pixelar.lockertheft.LockerTheft.SERVER_INSTANCE;

public class LockPickingGUI implements Listener {
    private final Inventory inv;
    private int slotOneOffset = 0;
    private final String background = "\uE401";

    public LockPickingGUI() {
        inv = Bukkit.createInventory(null, 36,
                Component.text(
                        ComponentManager.getStringOffset(-8) + background
                                + ComponentManager.getStringOffset(-132)
                                + "\ue403",
                        ComponentManager.nonItalic(TextColor.color(255, 255, 255))));
        initializeItems();

        SERVER_INSTANCE.sendMessage(Component.text(inv.hashCode() + " (init)"));

    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {
        inv.setItem(27, ItemRegistries.LOCK);
    }

    private enum PIN_STAGE {
        LOCKED,
        LIFT_UP,
        UNLOCKED
    }

    private static Character getPinStageDisplay(PIN_STAGE stage) {
        return switch (stage) {
            case LOCKED ->   '\ue402';
            case LIFT_UP ->  '\ue403';
            case UNLOCKED -> '\ue404';
        };
    }

    private Component getPinDisplay() {
        return  Component.text(
                  ComponentManager.getStringOffset(-8)      + background
                + ComponentManager.getStringOffset(-132)  + "\ue404"
                + ComponentManager.getStringOffset(4)  + ComponentManager.getStringOffset(-3) +  "\ue404"
                + ComponentManager.getStringOffset(4)  + ComponentManager.getStringOffset(-3) + "\ue404"
                + ComponentManager.getStringOffset(4)  + ComponentManager.getStringOffset(-3) + "\ue404"
                + ComponentManager.getStringOffset(4)  + ComponentManager.getStringOffset(-3) + "\ue404",
                ComponentManager.nonItalic(TextColor.color(255, 255, 255))
                );
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    private boolean isCorrectInventory(InventoryView inventory) {
        return inventory.getOriginalTitle().contains(background);
    }
    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!isCorrectInventory(e.getView())) return;

        e.setCancelled(true);

//        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
//        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();


        // Using slots click is a best option for your inventory click's
        p.sendMessage("You clicked at slot " + e.getRawSlot());
        slotOneOffset -= e.getRawSlot();

        String name = ComponentManager.getStringOffset(-8) + "\uE401" + ComponentManager.getStringOffset(slotOneOffset) + "\ue403";
        e.getWhoClicked().getOpenInventory().setTitle(LegacyComponentSerializer.legacySection().serialize(getPinDisplay()));

        ;
//        inv = e.getWhoClicked().getInventory();

        p.sendMessage("" + "A" + " (0)");
        p.sendMessage("\uF801" + "A" + " (-3 Char)");
        p.sendMessage(ComponentManager.getStringOffset(-3) + "A" + " (-3 Offset)");
        p.sendMessage(ComponentManager.getStringOffset(2) + "A" + " (2 Offset)");

    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }
}
