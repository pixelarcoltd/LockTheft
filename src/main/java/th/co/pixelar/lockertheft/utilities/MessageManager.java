package th.co.pixelar.lockertheft.utilities;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class MessageManager {
    private final Player player;
    private String message;
    private Type messageType;
    public enum Type {
        INFO,
        WARNING,
        ERROR,
        OTHER
    }

    public MessageManager(Player player, String message, Type type) {
        this.player = player;
        this.message = message;
        this.messageType = type;
    }

    public MessageManager(Player player, String message) {
        this.player = player;
        this.message = message;
        this.messageType = Type.OTHER;
    }

    public MessageManager(Player player) {
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
        if (message == null) message = "";
        player.sendMessage(Component.text(message));
    }


}
