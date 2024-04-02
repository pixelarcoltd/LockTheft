package th.co.pixelar.lockertheft.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import th.co.pixelar.lockertheft.LockerTheft;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

import static th.co.pixelar.lockertheft.LockerTheft.getResponse;

public class LockTheftCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        YamlConfiguration response = LockerTheft.RESPONSE;
        if (commandSender instanceof Player player) {

            if (args.length == 0) {
                player.sendMessage(getResponse("plugin.info"));
                return true;
            }
            if (args[0].equals("reload")) {
                LockerTheft.CONFIG = YamlConfiguration.loadConfiguration(new File(LockerTheft.PLUGIN_RESOURCE, "config.yml"));
                LockerTheft.RESPONSE = YamlConfiguration.loadConfiguration(new File(LockerTheft.PLUGIN_RESOURCE, "response.yml"));
                player.sendMessage(getResponse(("command.reload.successful")));
                return true;
            }

            return true;
        }

        if (args[0].equals("reload")) {
            LockerTheft.CONFIG = YamlConfiguration.loadConfiguration(new File(LockerTheft.PLUGIN_RESOURCE, "config.yml"));
            LockerTheft.RESPONSE = YamlConfiguration.loadConfiguration(new File(LockerTheft.PLUGIN_RESOURCE, "response.yml"));
            Bukkit.getLogger().info(getResponse("command.reload.successful"));
            return true;
        }

        return true;
    }
}
