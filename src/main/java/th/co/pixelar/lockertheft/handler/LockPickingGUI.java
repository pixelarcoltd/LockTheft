package th.co.pixelar.lockertheft.handler;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang3.mutable.MutableObject;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static th.co.pixelar.lockertheft.LockerTheft.SERVER_INSTANCE;

public class LockPickingGUI implements Listener {
    private final Inventory inv;
    private int slotOneOffset = 0;
    private final static String BACKGROUND = "\uE401";

    private List<PIN_STAGE> stage;
    private PIN_STAGE[] stages;

    public LockPickingGUI() {
        stage = List.of(PIN_STAGE.LOCKED, PIN_STAGE.LOCKED, PIN_STAGE.LOCKED, PIN_STAGE.LOCKED, PIN_STAGE.LOCKED);
        inv = Bukkit.createInventory(null, 36, getPinDisplay());
        initializeItems();
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
                  ComponentManager.getStringOffset(-8) + BACKGROUND
                + ComponentManager.getStringOffset(-132)  + getPinStageDisplay(stage.get(0))
                + ComponentManager.getStringOffset(4)  + ComponentManager.getStringOffset(-3) + getPinStageDisplay(stage.get(1))
                + ComponentManager.getStringOffset(4)  + ComponentManager.getStringOffset(-3) + getPinStageDisplay(stage.get(2))
                + ComponentManager.getStringOffset(4)  + ComponentManager.getStringOffset(-3) + getPinStageDisplay(stage.get(3))
                + ComponentManager.getStringOffset(4)  + ComponentManager.getStringOffset(-3) + getPinStageDisplay(stage.get(4)),
                ComponentManager.nonItalic(TextColor.color(255, 255, 255))
                );
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    private boolean isCorrectInventory(InventoryView inventory) {
        return inventory.getOriginalTitle().contains(BACKGROUND);
    }
    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!isCorrectInventory(e.getView())) return;
        e.setCancelled(true);

//        stage.set(0, PIN_STAGE.LIFT_UP);
        SERVER_INSTANCE.sendMessage(Component.text(e.getRawSlot() + " "));



        if (e.getRawSlot() == 29) {
            stage = List.of(PIN_STAGE.LIFT_UP, stage.get(1), stage.get(2), stage.get(3), stage.get(4));
        }

        e.getWhoClicked().getOpenInventory().setTitle(LegacyComponentSerializer.legacySection().serialize(getPinDisplay()));


    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }
}
