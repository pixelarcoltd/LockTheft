package th.co.pixelar.lockertheft;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import th.co.pixelar.lockertheft.handler.GUITitleHandler;
import th.co.pixelar.lockertheft.handler.LockPickingGUI;
import th.co.pixelar.lockertheft.listeners.EventListeners;
import th.co.pixelar.lockertheft.registries.RecipeRegistries;

public final class LockerTheft extends JavaPlugin {

    public static Server SERVER_INSTANCE;
    public static Plugin PLUGIN;

    private ProtocolManager PROTOCOL_MANAGER;
    public static GUITitleHandler GUI_TITLE_HANDLER;

    @Override
    public void onLoad() {
        this.PROTOCOL_MANAGER = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        SERVER_INSTANCE = getServer();
        PLUGIN = this;

        new RecipeRegistries(SERVER_INSTANCE);

        SERVER_INSTANCE.getPluginManager().registerEvents(new EventListeners(), this);
        SERVER_INSTANCE.getPluginManager().registerEvents(new LockPickingGUI(), this);

        GUI_TITLE_HANDLER = new GUITitleHandler(this, PROTOCOL_MANAGER);
        GUI_TITLE_HANDLER.registerPacketListeners();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
