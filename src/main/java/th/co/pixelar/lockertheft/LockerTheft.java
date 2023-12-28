package th.co.pixelar.lockertheft;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import th.co.pixelar.lockertheft.listeners.EventListeners;
import th.co.pixelar.lockertheft.registries.RecipeRegistries;

public final class LockerTheft extends JavaPlugin {

    public static Server SERVER_INSTANCE;

    @Override
    public void onEnable() {
        SERVER_INSTANCE = getServer();

        new RecipeRegistries(SERVER_INSTANCE);

        SERVER_INSTANCE.getPluginManager().registerEvents(new EventListeners(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
