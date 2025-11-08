package org.legion.simpleMail.Mails;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import static org.legion.simpleMail.Utils.itemFromBase64;

public class MailEntry {

    private final int id;
    private final UUID sender;
    private final UUID receiver;
    private final String message;
    private final String itemBase64;

    public MailEntry(int id, UUID sender, UUID receiver, String message, String itemBase64) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.itemBase64 = itemBase64;
    }

    public int getId() {
        return id;
    }

    public UUID getSender() {
        return sender;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public ItemStack getItem() {
        if (itemBase64 == null) return null;
        return itemFromBase64(itemBase64);
    }
}

