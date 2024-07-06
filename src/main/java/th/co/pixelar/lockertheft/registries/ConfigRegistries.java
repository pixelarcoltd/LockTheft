package th.co.pixelar.lockertheft.registries;

import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.YamlConfiguration;
import th.co.pixelar.lockertheft.LockerTheft;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static th.co.pixelar.lockertheft.LockerTheft.SERVER_INSTANCE;

public class ConfigRegistries {
    private static final String VERSION = LockerTheft.VERSION;
    public ConfigRegistries() {
        try {
            createDefaultConfig();
            createDefaultResponseConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createDefaultConfig() throws IOException {
        File config = new File(LockerTheft.PLUGIN_RESOURCE, "config.yml");
        YamlConfiguration yamlConfiguration = new YamlConfiguration();

        SERVER_INSTANCE.sendMessage(Component.text(Objects.equals(yamlConfiguration.getString("VERSION"), VERSION)));

        if (Objects.equals(yamlConfiguration.getString("VERSION"), VERSION)) return;

        yamlConfiguration.set("VERSION", LockerTheft.VERSION);
        yamlConfiguration.set("CYLINDER_UNLOCK_INTERRUPT_CHANCE", 20);
        yamlConfiguration.set("PICKER_MOVING_INTERRUPT_CHANCE", 10);
        yamlConfiguration.set("PICKER_MOVING_INTERRUPT_RANDOM_CHANCE", 20);
        yamlConfiguration.set("PICKER_CONSUME_CHANCE", 75);
        yamlConfiguration.set("SCREW_DRIVER_DURABILITY_CONSUME_PER_USE", 1);

        yamlConfiguration.set("THEFT_EXPERIENCE_LEVEL_MULTIPLIER", 1);
        yamlConfiguration.set("THEFT_EXPERIENCE_PER_SUCCESS", 10);
        yamlConfiguration.set("THEFT_EXPERIENCE_EFFECT_ON_REDUCING_INTERRUPTING", 0);

        yamlConfiguration.set("PREVENT_HOPPER_USING_ON_LOCKED_CHEST", true);
        yamlConfiguration.set("PREVENT_EXPLOSION_DESTROY_LOCKED_CHEST", true);

        yamlConfiguration.save(config);
    }

    private void createDefaultResponseConfig() throws IOException {
        File config = new File(LockerTheft.PLUGIN_RESOURCE, "response.yml");
        YamlConfiguration yamlConfiguration = new YamlConfiguration();

        if (Objects.equals(yamlConfiguration.getString("VERSION"), VERSION)) return;

        yamlConfiguration.set("VERSION", LockerTheft.VERSION);
        yamlConfiguration.set("PLUGIN_INFO"                     , "LockTheft Made with <3 by PIXELAR");
        yamlConfiguration.set("PLUGIN_RELOAD_SUCCESS"       , "LockTheft has been reloaded");
        yamlConfiguration.set("PLUGIN_RELOAD_FAIL"             , "LockTheft has failed to reload");

        yamlConfiguration.set("ACTION_LOCKED"         , "The chest is now locked; the master key has been added to your inventory.");
        yamlConfiguration.set("ACTION_PREVENT"        , "This chest is locked. Please use the correct key to open it.");
        yamlConfiguration.set("ACTION_INCORRECT_KEY"  , "The key you used is incorrect. Please insert the correct key.");
        yamlConfiguration.set("ACTION_PICKING_FAIL"   , "You have failed to pick the lock. Better luck next time.");
        yamlConfiguration.set("ACTION_PICKING_SUCCESS", "You have successfully picked the chest. Enjoy!");
        yamlConfiguration.set("ACTION_UNLOCKED"       , "The chest is now unlocked; anyone can access it.");

        yamlConfiguration.save(config);
    }
    
}
