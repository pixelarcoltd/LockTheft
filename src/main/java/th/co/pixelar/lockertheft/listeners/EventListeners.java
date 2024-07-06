package th.co.pixelar.lockertheft.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import th.co.pixelar.lockertheft.handlers.ConfigLoader;
import th.co.pixelar.lockertheft.handlers.LockPickingGUI;
import th.co.pixelar.lockertheft.registries.ItemRegistries;
import th.co.pixelar.lockertheft.storages.LockAndKeyManager;
import th.co.pixelar.lockertheft.utilities.ChestManager;
import th.co.pixelar.lockertheft.utilities.MessageManager;

import java.util.List;
import java.util.Objects;

public class EventListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractChest(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (Objects.requireNonNull(event.getClickedBlock()).getType() != Material.CHEST) return;

        ItemStack handheld = event.getItem();
        if (handheld == null) {
            handheld = new ItemStack(Material.AIR);
        }

        Block block = event.getClickedBlock();
        LockAndKeyManager lockAndKeyManager = new LockAndKeyManager(block);
        Player player = event.getPlayer();

        if (!lockAndKeyManager.isLocked) {
            if (handheld.getType().equals(Material.AIR) || !handheld.asOne().equals(ItemRegistries.LOCK)) return;

            event.setCancelled(true);

            ChestManager.setLockDisplayOnChest(block);

            handheld.subtract();

            lockAndKeyManager.lock();
            ItemStack key = ItemRegistries.KEY;
            key = lockAndKeyManager.addKey(key);

            player.getInventory().addItem(key);
            new MessageManager(player, ConfigLoader.ACTION_LOCKED).sentMessage();

            return;
        }

        if (handheld.getType().equals(Material.AIR) || LockAndKeyManager.getKey(handheld.asOne()) == null) {
            event.setCancelled(true);
            if (handheld.asOne().equals(ItemRegistries.LOCK_PICKER)) {

                LockPickingGUI gui = new LockPickingGUI();
                gui.openInventory(event.getPlayer(), block);
                return;
            }

            new MessageManager(player, ConfigLoader.ACTION_PREVENT).sentMessage();
            return;
        }

        if (!handheld.asOne().equals(lockAndKeyManager.addKey(ItemRegistries.KEY))) {
            new MessageManager(player, ConfigLoader.ACTION_INCORRECT_KEY).sentMessage();
            event.setCancelled(true);
            return;
        }

        if (event.getPlayer().isSneaking()) {
            lockAndKeyManager.unlock();
            ChestManager.removeLockDisplayFromChest(block);
            event.getPlayer().getInventory().addItem(ItemRegistries.LOCK);
            event.getPlayer().getInventory().remove(handheld.asOne());
            new MessageManager(player, ConfigLoader.ACTION_UNLOCKED).sentMessage();
            event.setCancelled(true);
        }

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
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHopperPassingItem(InventoryMoveItemEvent event) {
        if (!ConfigLoader.PREVENT_HOPPER_USING_ON_LOCKED_CHEST) return;
        Block block = Objects.requireNonNull(event.getInitiator().getLocation()).getBlock().getRelative(BlockFace.UP);
        LockAndKeyManager lockAndKeyManager = new LockAndKeyManager(block);
        if (lockAndKeyManager.isLocked) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onExplosion(EntityExplodeEvent event) {
        if (!ConfigLoader.PREVENT_EXPLOSION_DESTROY_LOCKED_CHEST) return;
        List<Block> blocks = event.blockList();
        for (Block block: blocks) {
            if (block.getType() != Material.CHEST) continue;
            if(new LockAndKeyManager(block).isLocked) {
                event.blockList().remove(block);
            }
        }
    }
}
