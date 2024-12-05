package th.co.pixelar.lockertheft;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import th.co.pixelar.lockertheft.bstats.Metrics;
import th.co.pixelar.lockertheft.commands.LockTheftCommand;
import th.co.pixelar.lockertheft.handlers.ConfigLoader;
import th.co.pixelar.lockertheft.handlers.LockPickingGUI;
import th.co.pixelar.lockertheft.listeners.EventListeners;
import th.co.pixelar.lockertheft.registries.ConfigRegistries;
import th.co.pixelar.lockertheft.registries.RecipeRegistries;

import java.util.Objects;

public final class LockerTheft extends JavaPlugin {

    public static Server SERVER_INSTANCE;
    public static Plugin PLUGIN;
    public static String PLUGIN_RESOURCE = "plugins/LockerTheft";
    public static ConfigLoader CONFIG;
    public static String VERSION;
    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        SERVER_INSTANCE = getServer();
        PLUGIN = this;
        VERSION = this.getPluginMeta().getVersion();

        metricBstatsInit();

        new ConfigRegistries();

        CONFIG = new ConfigLoader();

        Objects.requireNonNull(this.getCommand("locktheft")).setExecutor(new LockTheftCommand());

        new RecipeRegistries(SERVER_INSTANCE);

        SERVER_INSTANCE.getPluginManager().registerEvents(new EventListeners(), this);
        SERVER_INSTANCE.getPluginManager().registerEvents(new LockPickingGUI(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void metricBstatsInit() {
        // All you have to do is adding the following two lines in your onEnable method.
        // You can find the plugin ids of your plugins on the page https://bstats.org/what-is-my-plugin-id
        int pluginId = 24081; // <-- Replace with the id of your plugin!
        new Metrics(this, pluginId);

    }

}
