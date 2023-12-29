package th.co.pixelar.lockertheft.listeners;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTTileEntity;
import net.kyori.adventure.text.Component;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import th.co.pixelar.lockertheft.registries.ItemRegistries;

import java.util.Collection;
import java.util.Objects;

import static th.co.pixelar.lockertheft.LockerTheft.SERVER_INSTANCE;

public class EventListeners implements Listener {

    private BlockFace getChestFacingDirection(BlockData data) {
        return ((Chest) data).getFacing();
    }

    private Chest.Type getChestType(BlockData data) {
        return ((Chest) data).getType();
    }

    private Vector getLockDisplayDirection(Block block) {

        Vector vector = new Vector();
        Chest.Type type = getChestType(block.getBlockData());

        switch (getChestFacingDirection(block.getBlockData())) {
            case EAST -> {
                if (type.equals(Chest.Type.LEFT)) {
                    return new Vector(0.97,0.40,1); //
                }
                if (type.equals(Chest.Type.RIGHT)){
                    return new Vector(0.97,0.40,0); //
                }

                return new Vector(0.97,0.40,0.5); //
            }
            case WEST -> {
                if (type.equals(Chest.Type.LEFT)) {
                    return new Vector(0.03,0.40,0); //
                }
                if (type.equals(Chest.Type.RIGHT)){
                    return new Vector(0.03,0.40,1); //
                }

                return new Vector(0.03,0.40,0.5); //
            }
            case NORTH -> {
                if (type.equals(Chest.Type.LEFT)) {
                    return new Vector(1.0,0.40,0.03); //
                }
                if (type.equals(Chest.Type.RIGHT)){
                    return new Vector(0,0.40,0.97); //
                }
                return new Vector(0.5,0.40,0.03); //
            }
            case SOUTH -> {
                if (type.equals(Chest.Type.LEFT)) {
                    return new Vector(0,0.40,0.97); //
                }
                if (type.equals(Chest.Type.RIGHT)){
                    return new Vector(1.0,0.40,0.97); //
                }

                return new Vector(0.5,0.40,0.97); //
            }
        }

        return vector;
    }

    private void removeLockDisplay(Block block) {
        if (block instanceof Chest chest) {
            Chest.Type type = chest.getType();

            float radius = 1.02f;
            if (type.equals(Chest.Type.SINGLE)) {
                radius = 0.51f;
            }

            Collection<Entity> entities = block.getWorld().getNearbyEntities(block.getLocation().toCenterLocation(), radius, radius, radius);
            for (Entity entity : entities) {
                if (entity.getType() == EntityType.ITEM_DISPLAY) entity.remove();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractChest(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (Objects.requireNonNull(event.getClickedBlock()).getType() != Material.CHEST) return;

        ItemStack handheld = event.getItem();
        if (handheld == null) return;



        Block block = event.getClickedBlock();
        Vector vector = getLockDisplayDirection(block);
        BlockFace facing = getChestFacingDirection(block.getBlockData());



        NBTTileEntity nbt= new NBTTileEntity(block.getState());
        NBTCompound compound = (NBTCompound) nbt.getCompound();
        compound.resolveOrCreateCompound("Tags");

        SERVER_INSTANCE.sendMessage(Component.text(compound + " e1"));
        SERVER_INSTANCE.sendMessage(Component.text(nbt.getCompound() + " e2"));

        if (handheld.asOne().equals(ItemRegistries.LOCK)) {

            removeLockDisplay(block);

            //DISPLAY LOCK ON CHEST
            ItemDisplay display = block.getWorld().spawn(block.getLocation().add(vector), ItemDisplay.class);

            display.setItemStack(ItemRegistries.LOCK);
            Transformation transformation = display.getTransformation();

            transformation.getScale().set(0.35D, 0.35D, 0.75D);
            transformation.getLeftRotation().x = 0F;
            transformation.getLeftRotation().z = 0F;
            transformation.getLeftRotation().y = 0F;
            if (facing.equals(BlockFace.EAST) || facing.equals(BlockFace.WEST)) {
                transformation.getLeftRotation().y = -0.6999999f;
                transformation.getLeftRotation().w = 0.7f;
            }

            display.setTransformation(transformation);

            handheld.subtract();
            event.setCancelled(true);

        }
    }

}
