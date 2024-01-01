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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import th.co.pixelar.lockertheft.registries.ItemRegistries;
import th.co.pixelar.lockertheft.utilities.ComponentManager;
import th.co.pixelar.lockertheft.utilities.MathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static th.co.pixelar.lockertheft.LockerTheft.SERVER_INSTANCE;

public class LockPickingGUI implements Listener {
    private final Inventory inv;
    private final static String BACKGROUND = "\uE401";
    private PIN_STAGE[] stages;
    private int pickerSlot;

    public LockPickingGUI() {
        pickerSlot = 0;
        stages = new PIN_STAGE[]{ PIN_STAGE.LOCKED, PIN_STAGE.LOCKED, PIN_STAGE.LOCKED, PIN_STAGE.LOCKED, PIN_STAGE.LOCKED };
        inv = Bukkit.createInventory(null, 36, getPinDisplay());
        initializeItems();
    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {

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

    private static String getPickerDisplay(int slot) {
        int offset = slot * 18;
        return ComponentManager.getStringOffset(-184) + ComponentManager.getStringOffset(offset) + "\ue405";
    }

    private Component getPinDisplay() {
        return  Component.text(
                  ComponentManager.getStringOffset(-8) + BACKGROUND
                + ComponentManager.getStringOffset(-132)  + getPinStageDisplay(stages[0])
                + ComponentManager.getStringOffset(4)  + ComponentManager.getStringOffset(-3) + getPinStageDisplay(stages[1])
                + ComponentManager.getStringOffset(4)  + ComponentManager.getStringOffset(-3) + getPinStageDisplay(stages[2])
                + ComponentManager.getStringOffset(4)  + ComponentManager.getStringOffset(-3) + getPinStageDisplay(stages[3])
                + ComponentManager.getStringOffset(4)  + ComponentManager.getStringOffset(-3) + getPinStageDisplay(stages[4])
                + ComponentManager.getStringOffset(5)  + getPickerDisplay(pickerSlot),
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

        // slots are consisted of 0, 1, 2, 3 and 4
        int clickedSlot = e.getRawSlot() - 29;

        if (clickedSlot >= 0 && clickedSlot <= 4) {

            if (stages[clickedSlot] == PIN_STAGE.LIFT_UP) {
                stages[clickedSlot] = PIN_STAGE.UNLOCKED;
            }

            if (stages[clickedSlot] == PIN_STAGE.LOCKED) {
                stages[clickedSlot] = PIN_STAGE.LIFT_UP;
            }

            if (pickerSlot != clickedSlot) {
                stages[MathUtils.randomBetweenInteger(0, 4)] = PIN_STAGE.LOCKED;
            }

            if (clickedSlot > 0) {
                if (MathUtils.chanceOf(20)) {
                    stages[clickedSlot] = PIN_STAGE.LOCKED;
                }
            }
        }

        if (e.getRawSlot() == 27 || e.getRawSlot() == 35) {
            if (MathUtils.chanceOf(20)) {
                stages[pickerSlot] = PIN_STAGE.LOCKED;
            }

            if (MathUtils.chanceOf(10)) {
                stages[MathUtils.randomBetweenInteger(0, pickerSlot)] = PIN_STAGE.LOCKED;
            }

            if (e.getRawSlot() == 27) {
                pickerSlot -= 1;
                if (pickerSlot < 0) pickerSlot = 0;

            }

            if (e.getRawSlot() == 35) {
                pickerSlot += 1;
                if (pickerSlot > 4) pickerSlot = 4;

            }

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

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        if (!isCorrectInventory(e.getView())) return;
        stages = new PIN_STAGE[]{ PIN_STAGE.LOCKED, PIN_STAGE.LOCKED, PIN_STAGE.LOCKED, PIN_STAGE.LOCKED, PIN_STAGE.LOCKED };
        pickerSlot = 0;
    }
}
