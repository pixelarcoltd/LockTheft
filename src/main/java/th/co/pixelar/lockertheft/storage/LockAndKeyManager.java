package th.co.pixelar.lockertheft.storage;

import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class LockAndKeyManager {
    private final Block block;
    public UUID key = null;
    public boolean isLocked;
    public LockAndKeyManager(Block block) {
        this.block = block;
        this.key = getBlockKey();
        this.isLocked = isLocked();
    }
    private static NBTCompound getNBTCompound(Block block) {
        NBTBlock nbtBlock = new NBTBlock(block);
        return nbtBlock.getData();
    }

    public void lock() {
        UUID key = UUID.randomUUID();
        NBTCompound data = getNBTCompound(this.block);
        data.setBoolean("lock", true);
        data.setUUID("key", key);
        this.key = getBlockKey();
    }

    public void unlock() {
        NBTCompound data = getNBTCompound(this.block);
        data.removeKey("key");
        data.setBoolean("lock", false);

        this.key = null;
        this.isLocked = isLocked();
    }

    public ItemStack addKey(ItemStack keyItem) {
        if (this.key == null) return keyItem;
        NBTItem nbtItem = new NBTItem(keyItem);
        nbtItem.setUUID("key", this.key);
        nbtItem.applyNBT(keyItem);
        return keyItem;
    }

    public static UUID getKey(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getUUID("key");
    }

    private UUID getBlockKey() {
        return getNBTCompound(this.block).getUUID("key");
    }

    private boolean isLocked() {
        NBTCompound data = getNBTCompound(this.block);
        return (data.getBoolean("lock"));
    }
}
