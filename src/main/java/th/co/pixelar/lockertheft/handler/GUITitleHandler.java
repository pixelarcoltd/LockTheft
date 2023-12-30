package th.co.pixelar.lockertheft.handler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

public class GUITitleHandler {

    private HashMap<UUID, InventoryPlayer> inventoryPlayers = new HashMap<>();

    private ProtocolManager protocolManager;
    private JavaPlugin plugin;

    public GUITitleHandler(JavaPlugin plugin, ProtocolManager protocolManager) {
        this.plugin = plugin;
        this.protocolManager = protocolManager;
    }

    // -- Listening to packets --

    public void registerPacketListeners() {
        protocolManager.addPacketListener(getOpenWindowPacketListener());
        protocolManager.addPacketListener(getCloseWindowPacketListener());
    }

    private PacketListener getOpenWindowPacketListener() {
        return new PacketAdapter(plugin, ListenerPriority.HIGH, PacketType.Play.Server.OPEN_WINDOW) {
            @Override
            public void onPacketSending(PacketEvent event) {
                final UUID uuid = event.getPlayer().getUniqueId();

                final int windowId = event.getPacket().getIntegers().read(0);
                final Object containerType = event.getPacket().getStructures().readSafely(0);

                // Create our custom holder object (defined at the end of this class) and put it in a HashMap
                InventoryPlayer player = new InventoryPlayer(windowId, containerType);
                inventoryPlayers.put(uuid, player);
            }
        };
    }

    private PacketListener getCloseWindowPacketListener() {
        return new PacketAdapter(plugin, ListenerPriority.HIGH, PacketType.Play.Client.CLOSE_WINDOW) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                final UUID uuid = event.getPlayer().getUniqueId();

                // Remove the player from logged inventories, because a menu has been closed.
                inventoryPlayers.remove(uuid);
            }
        };
    }


    // -- API --

    public void setPlayerInventoryTitle(Player player, String title) {
        final InventoryType type = player.getOpenInventory().getType();
        if (type == InventoryType.CRAFTING || type == InventoryType.CREATIVE)
            return;

        InventoryPlayer inventoryPlayer = inventoryPlayers.getOrDefault(player.getUniqueId(), null);

        if (inventoryPlayer == null)
            return;

        final int windowId = inventoryPlayer.getWindowId();
        if (windowId == 0)
            return;

        final Object windowType = inventoryPlayer.getContainerType();

        // Send the packet
        sendOpenScreenPacket(player, windowId, windowType, title);
        // Update the inventory for the client (to show items)
        player.updateInventory();
    }


    // -- Utility --

    private void sendOpenScreenPacket(Player player, int windowId, Object windowType, String titleJson) {
        final WrappedChatComponent wrappedChatComponent = WrappedChatComponent.fromJson(titleJson);

        PacketContainer openScreen = new PacketContainer(PacketType.Play.Server.OPEN_WINDOW);
        openScreen.getIntegers().write(0, windowId);
        openScreen.getStructures().write(0, (InternalStructure) windowType);
        openScreen.getChatComponents().write(0, wrappedChatComponent);

        try {
            protocolManager.sendServerPacket(player, openScreen);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    class InventoryPlayer {
        private int windowId;
        private Object containerType;

        public InventoryPlayer(int windowId, Object containerType) {
            this.windowId = windowId;
            this.containerType = containerType;
        }

        public int getWindowId() {
            return windowId;
        }

        public Object getContainerType() {
            return containerType;
        }

    }


}
