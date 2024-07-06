package th.co.pixelar.lockertheft.storages;

import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Chest;
import org.bukkit.inventory.ItemStack;
import th.co.pixelar.lockertheft.utilities.ChestManager;
import th.co.pixelar.lockertheft.utilities.ComponentManager;

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

        if (!ChestManager.getChestType(this.block.getBlockData()).equals(Chest.Type.SINGLE)) {
            NBTCompound twinData = getNBTCompound(ChestManager.getTwinChest(this.block));
            twinData.setBoolean("lock", true);
            twinData.setUUID("key", key);
        }

        this.key = getBlockKey();
    }

    public void lock(Block twinBlock) {
        LockAndKeyManager lockAndKeyManager = new LockAndKeyManager(twinBlock);
        NBTCompound data = getNBTCompound(this.block);
        data.setBoolean("lock", true);
        data.setUUID("key", lockAndKeyManager.getBlockKey());
    }


    public void unlock() {
        NBTCompound data = getNBTCompound(this.block);
        data.removeKey("key");
        data.setBoolean("lock", false);

        if (!ChestManager.getChestType(this.block.getBlockData()).equals(Chest.Type.SINGLE)) {
            NBTCompound twinData = getNBTCompound(ChestManager.getTwinChest(this.block));
            twinData.removeKey("key");
            twinData.setBoolean("lock", false);
        }

        this.key = null;
        this.isLocked = isLocked();

        ChestManager.removeLockDisplayFromChest(this.block);
    }

    public ItemStack addKey(ItemStack item) {
        if (this.key == null) return item;

        item = ComponentManager.addLore(item, "&7#" + ComponentManager.getUUIDParts(this.key).get(0));

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setUUID("key", this.key);
        nbtItem.applyNBT(item);

        return item;
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
