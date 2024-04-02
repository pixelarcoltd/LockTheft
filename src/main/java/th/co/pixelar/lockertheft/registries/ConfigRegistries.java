package th.co.pixelar.lockertheft.registries;

import org.bukkit.configuration.file.YamlConfiguration;
import th.co.pixelar.lockertheft.LockerTheft;

import java.io.File;
import java.io.IOException;

public class ConfigRegistries {
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
        if (config.exists()) return;
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.set("version", LockerTheft.VERSION);
        yamlConfiguration.set("cylinderUnlockingInterruptChance", 20);
        yamlConfiguration.set("pickerMovingInterruptChance", 10);
        yamlConfiguration.set("pickerMovingInterruptRandomChance", 20);
        yamlConfiguration.set("pickerConsumeChance", 75);
        yamlConfiguration.set("screwDriverDurabilityConsumePerUse", 1);

        yamlConfiguration.set("theftExperienceLevelMultiplier", 1);
        yamlConfiguration.set("theftExperiencePerSuccess", 10);
        yamlConfiguration.set("theftExperienceEffectOnReducingInterrupting", 0);

        yamlConfiguration.save(config);
    }

    private void createDefaultResponseConfig() throws IOException {
        File config = new File(LockerTheft.PLUGIN_RESOURCE, "response.yml");
        if (config.exists()) return;
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.set("version", LockerTheft.VERSION);
        yamlConfiguration.set("plugin.info",               "LockTheft Made with <3 by PIXELAR");
        yamlConfiguration.set("command.reload.successful", "LockTheft has been reloaded");
        yamlConfiguration.set("command.reload.fail      ", "LockTheft has failed to reload");
        yamlConfiguration.save(config);
    }
}
