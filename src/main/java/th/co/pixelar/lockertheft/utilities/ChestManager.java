package th.co.pixelar.lockertheft.utilities;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

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

    public static void removeLockDisplayFromChest(Block block) {
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
}
