package th.co.pixelar.lockertheft.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import static th.co.pixelar.lockertheft.LockerTheft.SERVER_INSTANCE;

public class ComponentManager {
    public static @NotNull Style nonItalic(@NotNull TextColor color) {
        return Style.style().color(color).decoration(TextDecoration.ITALIC, false).build();
    }

    public static List<String> getUUIDParts(UUID uuid) {
        return new ArrayList<>(List.of(uuid.toString().split(Pattern.quote("-"))));

    }

    public static ItemStack addLore(ItemStack item, String lore) {
        ItemMeta meta = item.getItemMeta();
        List<String> lores = new ArrayList<>();
        lores.add(ChatColor.translateAlternateColorCodes('&', lore));
        meta.setLore(lores);
        item.setItemMeta(meta);

        return item;
    }

    public static String getStringOffset(Integer offset) {
        String result = "";
        List<String> type = new ArrayList<>();
        if (offset < 0) {
            //                  -3        -4        -6        -10       -18       -34       -66
            type = List.of("\uF801", "\uF802", "\uF804", "\uF808", "\uF809", "\uF80A", "\uF80B");
        }

        if (offset > 0) {
            //                  1     -3         1         3         7         15        31         63
            type = List.of("\uF822\uF801", "\uF822", "\uF824", "\uF828", "\uF829", "\uF82A", "\uF82B");
        }

        offset = Math.abs(offset);
        int offset_test = 0;

        while (offset > 1) {
            if (offset -64 >= 0) {
                result = String.join("", result, type.get(6));
                offset -= 64;
            }

            if (offset - 32 >= 0) {
                result = String.join("", result, type.get(5));
                offset -= 32;
            }

            if (offset - 16 >= 0) {
                result = String.join("", result, type.get(4));
                offset -= 16;
            }

            if (offset - 8 >= 0) {
                result = String.join("", result, type.get(3));
                offset -= 8;
            }

            if (offset - 4 >= 0) {
                result = String.join("", result, type.get(2));
                offset -= 4;
            }

            if (offset - 2 >= 0) {
                result = String.join("", result, type.get(1));
                offset -= 2;
            }

            if (offset - 1 >= 0) {
                result = String.join("", result, type.get(0));
                offset -= 1;
            }
        }

        return result;
    }

}
