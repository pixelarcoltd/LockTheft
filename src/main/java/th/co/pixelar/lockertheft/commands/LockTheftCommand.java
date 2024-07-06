package th.co.pixelar.lockertheft.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import th.co.pixelar.lockertheft.LockerTheft;
import th.co.pixelar.lockertheft.handlers.ConfigLoader;
import th.co.pixelar.lockertheft.utilities.MessageManager;


public class LockTheftCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player player) {

            if (args.length == 0) {
                new MessageManager(player, ConfigLoader.PLUGIN_INFO).sentMessage();
                return true;
            }
            if (args[0].equals("reload")) {
                LockerTheft.CONFIG.load();
                new MessageManager(player, ConfigLoader.PLUGIN_RELOAD_SUCCESS).sentMessage();
                return true;
            }

            return true;
        }

        if (args[0].equals("reload")) {
            LockerTheft.CONFIG.load();
            Bukkit.getLogger().info(ConfigLoader.PLUGIN_RELOAD_SUCCESS);
            return true;
        }

        return true;
    }
}
