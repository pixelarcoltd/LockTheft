package th.co.pixelar.lockertheft.handlers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import th.co.pixelar.lockertheft.LockerTheft;
import th.co.pixelar.lockertheft.registries.ItemRegistries;
import th.co.pixelar.lockertheft.storages.LockAndKeyManager;
import th.co.pixelar.lockertheft.utilities.ComponentManager;
import th.co.pixelar.lockertheft.utilities.MathUtils;

import java.util.HashMap;

public class LockPickingGUI implements Listener {
    private final Inventory inv;
    private final static String BACKGROUND = "\uE401";
    private PIN_STAGE[] stages;
    private int pickerSlot;
    private static final HashMap<HumanEntity, Block> playerAttractedBlock = new HashMap<>();

    public LockPickingGUI() {
        pickerSlot = 0;
        stages = new PIN_STAGE[]{ PIN_STAGE.LOCKED, PIN_STAGE.LOCKED, PIN_STAGE.LOCKED, PIN_STAGE.LOCKED, PIN_STAGE.LOCKED };
        inv = Bukkit.createInventory(null, 36, getPinDisplay());
        initialisedHoverButtons();
    }

    private enum PIN_STAGE {
        LOCKED,
        LIFT_UP,
        UNLOCKED
    }

    private void initialisedHoverButtons() {
        inv.setItem(27, ItemRegistries.PREVIOUS_BUTTON);
        inv.setItem(35, ItemRegistries.NEXT_BUTTON);
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
        String separator = ComponentManager.getStringOffset(4)  + ComponentManager.getStringOffset(-3);
        return  Component.text(
                  ComponentManager.getStringOffset(-8) + BACKGROUND
                + ComponentManager.getStringOffset(-132)  + getPinStageDisplay(stages[0])
                + separator + getPinStageDisplay(stages[1])
                + separator + getPinStageDisplay(stages[2])
                + separator + getPinStageDisplay(stages[3])
                + separator + getPinStageDisplay(stages[4])
                + ComponentManager.getStringOffset(5)  + getPickerDisplay(pickerSlot),
                ComponentManager.nonItalic(TextColor.color(255, 255, 255))
                );
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity e, Block block) {
        playerAttractedBlock.put(e, block);
        e.openInventory(inv);
    }

    private boolean isCorrectInventory(InventoryView inventory) {
        return inventory.getOriginalTitle().contains(BACKGROUND);
    }

    private boolean isUnlocked() {
        int i = 0;
        for (PIN_STAGE stage : stages) {
            if (stage.equals(PIN_STAGE.UNLOCKED))
                i++;
        }
        return i == 5;
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!isCorrectInventory(e.getView())) return;
        if (!e.getWhoClicked().getInventory().getItemInMainHand().asOne().equals(ItemRegistries.LOCK_PICKER)) return;

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
                if (MathUtils.chanceOf(LockerTheft.CONFIG.getInt("cylinderUnlockingInterruptChance"))) {
                    stages[clickedSlot] = PIN_STAGE.LOCKED;
                }
            }
        }

        if (e.getRawSlot() == 27 || e.getRawSlot() == 35) {
            if (MathUtils.chanceOf(LockerTheft.CONFIG.getInt("pickerMovingInterruptChance"))) {
                stages[pickerSlot] = PIN_STAGE.LOCKED;
            }

            if (MathUtils.chanceOf(LockerTheft.CONFIG.getInt("pickerMovingInterruptRandomChance"))) {
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
        e.getWhoClicked().getInventory().getItemInMainHand().subtract();

        if (isUnlocked()) {
            e.getInventory().close();
            Block block = playerAttractedBlock.get(e.getWhoClicked());
            LockAndKeyManager lockAndKeyManager = new LockAndKeyManager(block);
            lockAndKeyManager.unlock();
        }
    }

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
