package th.co.pixelar.lockertheft;

import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import th.co.pixelar.lockertheft.commands.LockTheftCommand;
import th.co.pixelar.lockertheft.handlers.LockPickingGUI;
import th.co.pixelar.lockertheft.listeners.EventListeners;
import th.co.pixelar.lockertheft.registries.ConfigRegistries;
import th.co.pixelar.lockertheft.registries.RecipeRegistries;

import java.io.File;
import java.util.Objects;

public final class LockerTheft extends JavaPlugin {

    public static Server SERVER_INSTANCE;
    public static Plugin PLUGIN;
    public static String PLUGIN_RESOURCE = "plugins/LockerTheft";
    public static YamlConfiguration CONFIG;
    public static YamlConfiguration RESPONSE;
    public static String VERSION;
    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        SERVER_INSTANCE = getServer();
        PLUGIN = this;
        VERSION = getDescription().getVersion();

        new ConfigRegistries();

        CONFIG = YamlConfiguration.loadConfiguration(new File(PLUGIN_RESOURCE, "config.yml"));
        RESPONSE = YamlConfiguration.loadConfiguration(new File(PLUGIN_RESOURCE, "response.yml"));

        Objects.requireNonNull(this.getCommand("locktheft")).setExecutor(new LockTheftCommand());

        new RecipeRegistries(SERVER_INSTANCE);

        SERVER_INSTANCE.getPluginManager().registerEvents(new EventListeners(), this);
        SERVER_INSTANCE.getPluginManager().registerEvents(new LockPickingGUI(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static @NotNull String getResponse(String path) {
        String response = LockerTheft.RESPONSE.getString(path);
        if (response == null) return "Sorry, but it seems that there is an issue with the response configuration. If you are not the administrator, please kindly notify your admin about this error.";
        return response;
    }
}
