package th.co.pixelar.lockertheft.handlers;

import org.bukkit.configuration.file.YamlConfiguration;
import th.co.pixelar.lockertheft.LockerTheft;

import java.io.File;

public class ConfigLoader {
    public static String ACTION_LOCKED;
    public static String ACTION_PREVENT;
    public static String ACTION_INCORRECT_KEY;
    public static String ACTION_PICKING_FAIL;
    public static String ACTION_PICKING_SUCCESS;
    public static String PLUGIN_INFO;
    public static String PLUGIN_RELOAD_SUCCESS;
    public static String PLUGIN_RELOAD_FAIL;
    public static String ACTION_UNLOCKED;

    public static int CYLINDER_UNLOCK_INTERRUPT_CHANCE;
    public static int PICKER_MOVING_INTERRUPT_CHANCE;
    public static int PICKER_MOVING_INTERRUPT_RANDOM_CHANCE;
    public static int PICKER_CONSUME_CHANCE;
    public static int SCREW_DRIVER_DURABILITY_CONSUME_PER_USE;
    public static int THEFT_EXPERIENCE_LEVEL_MULTIPLIER;
    public static int THEFT_EXPERIENCE_PER_SUCCESS;
    public static int THEFT_EXPERIENCE_EFFECT_ON_REDUCING_INTERRUPTING;
    public static boolean PREVENT_HOPPER_USING_ON_LOCKED_CHEST;
    public static boolean PREVENT_EXPLOSION_DESTROY_LOCKED_CHEST;

    public ConfigLoader() {
        load();
    }

    public void load() {
        YamlConfiguration CONFIG = YamlConfiguration.loadConfiguration(new File(LockerTheft.PLUGIN_RESOURCE, "config.yml"));
        YamlConfiguration RESPONSE = YamlConfiguration.loadConfiguration(new File(LockerTheft.PLUGIN_RESOURCE, "response.yml"));

        ACTION_LOCKED                           = RESPONSE.getString("ACTION_LOCKED");
        ACTION_PREVENT                          = RESPONSE.getString("ACTION_PREVENT");
        ACTION_INCORRECT_KEY                    = RESPONSE.getString("ACTION_INCORRECT_KEY");
        ACTION_PICKING_FAIL                     = RESPONSE.getString("ACTION_PICKING_FAIL");
        ACTION_PICKING_SUCCESS                  = RESPONSE.getString("ACTION_PICKING_SUCCESS");
        PLUGIN_INFO                             = RESPONSE.getString("PLUGIN_INFO");
        PLUGIN_RELOAD_SUCCESS                   = RESPONSE.getString("PLUGIN_RELOAD_SUCCESS");
        PLUGIN_RELOAD_FAIL                      = RESPONSE.getString("PLUGIN_RELOAD_FAIL");

        CYLINDER_UNLOCK_INTERRUPT_CHANCE        = CONFIG.getInt("CYLINDER_UNLOCK_INTERRUPT_CHANCE");
        PICKER_MOVING_INTERRUPT_CHANCE          = CONFIG.getInt("PICKER_MOVING_INTERRUPT_CHANCE");
        PICKER_MOVING_INTERRUPT_RANDOM_CHANCE   = CONFIG.getInt("PICKER_MOVING_INTERRUPT_RANDOM_CHANCE");
        PICKER_CONSUME_CHANCE                   = CONFIG.getInt("PICKER_CONSUME_CHANCE");
        SCREW_DRIVER_DURABILITY_CONSUME_PER_USE = CONFIG.getInt("SCREW_DRIVER_DURABILITY_CONSUME_PER_USE");
        THEFT_EXPERIENCE_LEVEL_MULTIPLIER       = CONFIG.getInt("THEFT_EXPERIENCE_LEVEL_MULTIPLIER");
        THEFT_EXPERIENCE_PER_SUCCESS            = CONFIG.getInt("THEFT_EXPERIENCE_PER_SUCCESS");
        THEFT_EXPERIENCE_EFFECT_ON_REDUCING_INTERRUPTING
                                                = CONFIG.getInt("THEFT_EXPERIENCE_EFFECT_ON_REDUCING_INTERRUPTING");
        PREVENT_HOPPER_USING_ON_LOCKED_CHEST    = CONFIG.getBoolean("PREVENT_HOPPER_USING_ON_LOCKED_CHEST");
        PREVENT_EXPLOSION_DESTROY_LOCKED_CHEST  = CONFIG.getBoolean("PREVENT_EXPLOSION_DESTROY_LOCKED_CHEST");

    }

}
