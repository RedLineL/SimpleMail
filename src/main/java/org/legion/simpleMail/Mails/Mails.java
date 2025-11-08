package org.legion.simpleMail.Mails;

import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.legion.simpleMail.Utils.itemToBase64;

public class Mails {

    private final Connection connection;

    public Mails(Connection connection) {
        this.connection = connection;
        createTable();
    }

    private void createTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS mail (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    sender TEXT NOT NULL,
                    receiver TEXT NOT NULL,
                    message TEXT,
                    item TEXT
                );
            """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendMail(UUID sender, UUID receiver, String message, ItemStack item) {
        String itemBase64 = item != null ? itemToBase64(item) : null;
        try (PreparedStatement ps = connection.prepareStatement("""
            INSERT INTO mail (sender, receiver, message, item)
            VALUES (?, ?, ?, ?);
        """)) {
            ps.setString(1, sender.toString());
            ps.setString(2, receiver.toString());
            ps.setString(3, message);
            ps.setString(4, itemBase64);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MailEntry> getMail(UUID receiver) {
        List<MailEntry> mails = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM mail WHERE receiver = ?")) {
            ps.setString(1, receiver.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MailEntry mail = new MailEntry(
                        rs.getInt("id"),
                        UUID.fromString(rs.getString("sender")),
                        UUID.fromString(rs.getString("receiver")),
                        rs.getString("message"),
                        rs.getString("item")
                );
                mails.add(mail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mails;
    }

    public void deleteMail(int id) {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM mail WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearMail(UUID receiver) {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM mail WHERE receiver = ?")) {
            ps.setString(1, receiver.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
