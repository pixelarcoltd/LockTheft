package th.co.pixelar.lockertheft.utilities;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class MessageLogger {
    private final Player player;
    private String message;
    private Type messageType;
    public enum Type {
        INFO,
        WARNING,
        ERROR,
        OTHER
    }

    public MessageLogger(Player player, String message, Type type) {
        this.player = player;
        this.message = message;
        this.messageType = type;
    }

    public MessageLogger(Player player, String message) {
        this.player = player;
        this.message = message;
        this.messageType = Type.OTHER;
    }

    public MessageLogger(Player player) {
        this.player = player;
        this.messageType = Type.OTHER;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessageType(Type messageType) {
        this.messageType = messageType;
    }

    public void sentMessage() {
        player.sendMessage(Component.text(message));
    }


}
