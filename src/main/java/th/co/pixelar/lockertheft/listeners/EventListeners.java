package th.co.pixelar.lockertheft.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import th.co.pixelar.lockertheft.registries.ItemRegistries;

import java.util.Collection;
import java.util.Objects;

public class EventListeners implements Listener {

    private enum CHEST_TYPE {
        SINGLE,
        TWIN_LEFT,
        TWIN_RIGHT
    }

    private BlockFace getChestFacingDirection(BlockData data) {
        return ((Chest) data).getFacing();
    }
    private Vector getLockDisplayDirection(Block block) {
        Vector vector = new Vector();
        switch (getChestFacingDirection(block.getBlockData())) {
            case EAST -> {
                return new Vector(0.97,0.40,0.5); //
            }
            case WEST -> {
                return new Vector(0.03,0.40,0.5); //
            }
            case NORTH -> {
                return new Vector(0.5,0.40,0.03); //
            }
            case SOUTH -> {
                return new Vector(0.5,0.40,0.97); //
            }
        }

        return vector;
    }

    private void removeLockDisplay(Block block, int radius) {
        Collection<Entity> entities = block.getWorld().getNearbyEntities(block.getLocation(), radius, radius, radius);
        for (Entity entity : entities) {
            if (entity.getType() == EntityType.ITEM_DISPLAY) entity.remove();
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractChest(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (Objects.requireNonNull(event.getClickedBlock()).getType() != Material.CHEST) return;

        {
            Block block = event.getClickedBlock();
            Vector vector = getLockDisplayDirection(block);

            ItemDisplay display = block.getWorld().spawn(block.getLocation().add(vector), ItemDisplay.class);
            display.setItemStack(ItemRegistries.LOCK);
            Transformation transformation = display.getTransformation();
            transformation.getScale().set(0.35D);
            transformation.getLeftRotation().x = 0F;
            transformation.getLeftRotation().z = 0F;
            transformation.getLeftRotation().y = 0F;
            if (getChestFacingDirection(block.getBlockData()).equals(BlockFace.EAST) || getChestFacingDirection(block.getBlockData()).equals(BlockFace.WEST)) {
                transformation.getLeftRotation().y = -0.6999999f;
                transformation.getLeftRotation().w = 0.7f;
            }

            display.setTransformation(transformation);
        }
    }

}
