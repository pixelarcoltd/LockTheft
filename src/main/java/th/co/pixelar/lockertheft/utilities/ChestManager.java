package th.co.pixelar.lockertheft.utilities;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import th.co.pixelar.lockertheft.registries.ItemRegistries;

import java.util.Collection;

public class ChestManager {
    public static BlockFace getChestFacingDirection(BlockData data) {
        return ((Chest) data).getFacing();
    }

    public static Chest.Type getChestType(BlockData data) {
        return ((Chest) data).getType();
    }

    public static Vector getLockDisplayDirectionOnChest(Block block) {

        Vector vector = new Vector();
        Chest.Type type = ChestManager.getChestType(block.getBlockData());

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
                    return new Vector(0,0.40,0.03); //
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

    public static void setLockDisplayOnChest(Block block) {
        Vector vector = ChestManager.getLockDisplayDirectionOnChest(block);
        BlockFace facing = ChestManager.getChestFacingDirection(block.getBlockData());

        ChestManager.removeLockDisplayFromChest(block);

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
    }

    public static void removeLockDisplayFromChest(Block block) {
        if (block.getBlockData() instanceof Chest chest) {
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

    public static @NotNull Block getTwinChest(Block block) {
        Chest.Type type = ChestManager.getChestType(block.getBlockData());
        if (type.equals(Chest.Type.SINGLE)) return block;

        BlockFace blockFace = ChestManager.getChestFacingDirection(block.getBlockData());

        Block twinBlock = block;

        int multiplier = -1;
        if (type.equals(Chest.Type.RIGHT)) {
            multiplier = 1;
        }

        switch (blockFace) {
            case EAST -> {
                twinBlock = block.getWorld().getBlockAt(block.getLocation().add(0, 0, -1 * multiplier));
            }
            case WEST -> {
                twinBlock = block.getWorld().getBlockAt(block.getLocation().add(0, 0, multiplier));
            }
            case NORTH -> {
                twinBlock = block.getWorld().getBlockAt(block.getLocation().add(-1 * multiplier, 0, 0));
            }
            case SOUTH -> {
                twinBlock = block.getWorld().getBlockAt(block.getLocation().add(multiplier, 0, 0));
            }
        }

        return twinBlock;

    }
}
