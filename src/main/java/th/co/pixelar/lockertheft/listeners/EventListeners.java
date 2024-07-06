package th.co.pixelar.lockertheft.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import th.co.pixelar.lockertheft.LockerTheft;
import th.co.pixelar.lockertheft.handlers.LockPickingGUI;
import th.co.pixelar.lockertheft.registries.ItemRegistries;
import th.co.pixelar.lockertheft.storages.LockAndKeyManager;
import th.co.pixelar.lockertheft.utilities.ChestManager;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static th.co.pixelar.lockertheft.LockerTheft.SERVER_INSTANCE;

public class EventListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractChest(PlayerInteractEvent event) {
        SERVER_INSTANCE.sendMessage(Component.text(event.getClickedBlock().getLocation().toString()));

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (Objects.requireNonNull(event.getClickedBlock()).getType() != Material.CHEST) return;

        ItemStack handheld = event.getItem();
        if (handheld == null) {
            handheld = new ItemStack(Material.AIR);
        }

        Block block = event.getClickedBlock();
        LockAndKeyManager lockAndKeyManager = new LockAndKeyManager(block);

        if (!lockAndKeyManager.isLocked) {
            if (handheld.getType().equals(Material.AIR) || !handheld.asOne().equals(ItemRegistries.LOCK)) return;

            event.setCancelled(true);

            ChestManager.setLockDisplayOnChest(block);

            handheld.subtract();

            lockAndKeyManager.lock();
            ItemStack key = ItemRegistries.KEY;
            key = lockAndKeyManager.addKey(key);

            event.getPlayer().getInventory().addItem(key);
            SERVER_INSTANCE.sendMessage(Component.text("NEW KEY ADDED TO PLAYER INVENTORY!"));

            return;
        }

        if (handheld.getType().equals(Material.AIR) || LockAndKeyManager.getKey(handheld.asOne()) == null) {
            event.setCancelled(true);
            if (handheld.asOne().equals(ItemRegistries.LOCK_PICKER)) {

                LockPickingGUI gui = new LockPickingGUI();
                gui.openInventory(event.getPlayer(), block);
                return;
            }

            SERVER_INSTANCE.sendMessage(Component.text("YOU DON'T HAVE KEY FOR THIS"));
            return;
        }

        if (!handheld.asOne().equals(lockAndKeyManager.addKey(ItemRegistries.KEY))) {
            SERVER_INSTANCE.sendMessage(Component.text("KEY IS NOT CORRECT"));
            SERVER_INSTANCE.sendMessage(Component.text(lockAndKeyManager.key + " (Lock)"));
            SERVER_INSTANCE.sendMessage(Component.text(LockAndKeyManager.getKey(handheld.asOne()) + " (Key)"));
            event.setCancelled(true);
            return;
        }

        if (event.getPlayer().isSneaking()) {
            lockAndKeyManager.unlock();
            ChestManager.removeLockDisplayFromChest(block);
            event.getPlayer().getInventory().addItem(ItemRegistries.LOCK);
            event.getPlayer().getInventory().remove(handheld.asOne());
            SERVER_INSTANCE.sendMessage(Component.text("THE LOCK HAS BEEN REMOVED FROM THE BLOCK"));
            event.setCancelled(true);
            return;
        }

        SERVER_INSTANCE.sendMessage(Component.text("IT IS LOCKED, BUT YOU HAVE KEY! YAY!"));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBreakLockedBlock(BlockBreakEvent event) {
        LockAndKeyManager lockAndKeyManager = new LockAndKeyManager(event.getBlock());
        if (!lockAndKeyManager.isLocked) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlaceChest(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        if (block.getType() != Material.CHEST) return;

        Block twinBlock = ChestManager.getTwinChest(block);

        LockAndKeyManager lockAndKeyManagerTwin = new LockAndKeyManager(twinBlock);

        if (lockAndKeyManagerTwin.isLocked) {
            LockAndKeyManager lockAndKeyManager = new LockAndKeyManager(block);
            lockAndKeyManager.lock(twinBlock);

            ChestManager.setLockDisplayOnChest(block);

            SERVER_INSTANCE.sendMessage(Component.text("LOCKED" + lockAndKeyManager.key + " =? " + lockAndKeyManagerTwin.key));
        }

    }

}
